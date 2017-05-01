package org.dog;

import org.dog.db.statement.Table;
import org.dog.util.Closer;
import org.dog.util.Reflection;
import org.dog.util.SQLPrinter;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Query<T> {

    private Connection connection;

    private Class<T> entity;
    private Table table;

    public Query(Connection connection, Class<T> entity) {
        this.connection = connection;
        this.entity = entity;
        table = Table.getTable(entity);
    }

    public boolean create() throws SQLException {
        String createPS = table.create();
        return executeSimpleStatement(createPS);
    }

    public boolean drop() throws SQLException {
        String dropPS = table.drop();
        return executeSimpleStatement(dropPS);
    }

    private boolean executeSimpleStatement(String pss) throws SQLException {
        return (boolean) doInPreparedStatement(pss, ps -> {
            ps.execute();
            SQLPrinter.printSqlStatement(ps);
            return true;
        });
    }

    public boolean insert(T t) throws SQLException {
        String insertPS = table.insert();

        return (boolean) doInPreparedStatement(insertPS, (PreparedStatement ps) -> {
            Reflection.setXXXToStatement(t, ps);
            Field idField = Reflection.getIdField(t);
            if (ps.executeUpdate() != 1) {
                return false;
            }
            ResultSet id = ps.getGeneratedKeys();
            SQLPrinter.printSqlStatement(ps);
            if (id.next()) {
                long value = id.getLong(1);
                idField.set(t, value);
                return true;
            }
            return false;
        });
    }

    public boolean update(T t) throws SQLException {
        String updatePS = table.update();

        return (boolean) doInPreparedStatement(updatePS, (PreparedStatement ps) -> {
            Class<?> psClass = ps.getClass().getSuperclass();
            Field idField = Reflection.getIdField(t);
            int i = Reflection.setXXXToStatement(t, ps);
            Method method = Reflection.getSetXXXMethod(psClass, idField);
            method.invoke(ps, i++, idField.get(t));
            if (ps.executeUpdate() != 1) {
                return false;
            }
            SQLPrinter.printSqlStatement(ps);
            return true;
        });
    }

    public boolean delete(T t) throws SQLException {
        String deleteOne = table.deleteOne();

        return (boolean) doInPreparedStatement(deleteOne, (PreparedStatement ps) -> {
            Class<?> psClass = ps.getClass().getSuperclass();
            Field idField = Reflection.getIdField(t);
            int i = 1;
            Method method = Reflection.getSetXXXMethod(psClass, idField);
            method.invoke(ps, i++, idField.get(t));
            if (ps.executeUpdate() != 1) {
                return false;
            }
            idField.set(t, 0L);
            SQLPrinter.printSqlStatement(ps);
            return true;
        });
    }

    @SuppressWarnings("unchecked")
    public T select(T t)throws SQLException {
        String selectPS = table.selectOne();

        Object result = doInPreparedStatement(selectPS, (PreparedStatement ps) -> {
            Class<?> psClass = ps.getClass().getSuperclass();
            Field idField = Reflection.getIdField(t);
            int i = 1;
            Method method = Reflection.getSetXXXMethod(psClass, idField);
            method.invoke(ps, i++, idField.get(t));
            ResultSet trs = ps.executeQuery();
            if (trs.next()) {
                Reflection.setFields(t, trs);
            }
            SQLPrinter.printSqlStatement(ps);
            return t;
        });

        if (result != null) {
            return (T) result;
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public List<T> select()throws SQLException {
        String selectPS = table.select();

        Object result = doInPreparedStatement(selectPS, (PreparedStatement ps) -> {
            ResultSet trs = ps.executeQuery();
            List<T> ts = new ArrayList<>();
            while (trs.next()) {
                T t = entity.newInstance();
                Reflection.setFields(t, trs);
                ts.add(t);
            }

            SQLPrinter.printSqlStatement(ps);
            return ts;
        });
        return (List<T>) (result != null ? result : Collections.emptyList());
    }

    @FunctionalInterface
    private interface DoInStatement {
        Object act(PreparedStatement ps) throws SQLException, NoSuchMethodException,
                IllegalAccessException, InvocationTargetException, InstantiationException;
    }

    private Object doInPreparedStatement(String sql, DoInStatement action) throws SQLException {
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            return action.act(ps);
        } catch (NoSuchMethodException | IllegalAccessException
                | InvocationTargetException | InstantiationException e) {
            throw new InvocationMethodException("NO SUCH METHOD", e);
        } finally {
            Closer.close(ps);
        }
    }

}

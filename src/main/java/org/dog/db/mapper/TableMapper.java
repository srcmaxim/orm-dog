package org.dog.db.mapper;

import javax.persistence.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public final class TableMapper {

    private TableMapper() {
    }

    public static void link(Class clazz) {

        Entity entity = getEntity(clazz);
        Table table = getTable(clazz);

        String entityName = entity.name();
        String schema = table.schema();
        String tableName = table.name();

        org.dog.db.statement.Table tableStatement =
                org.dog.db.statement.Table.newInstance(clazz, schema, tableName);

        Field[] fields = clazz.getDeclaredFields();
        List<org.dog.db.statement.Column> columns = getColumns(fields);

        tableStatement.getColumns().addAll(columns);
    }

    private static List<org.dog.db.statement.Column> getColumns(Field[] fields) {
        List<org.dog.db.statement.Column> columns = new ArrayList<>();
        for (Field field : fields) {
            org.dog.db.statement.Column column = getColumn(field);
            columns.add(column);
        }
        return columns;
    }

    private static org.dog.db.statement.Column getColumn(Field field) {
        GetColumnData cd = new GetColumnData(field).invoke();
        return new org.dog.db.statement.Column(cd.getColumnName(), cd.getFieldType(),
                cd.isAutoIncremment(), cd.isNotNull(), cd.isPrimaryKey(), cd.isUnique());
    }

    private static Entity getEntity(Class clazz) {
        Entity annotation = (Entity) clazz.getAnnotation(Entity.class);
        if (annotation == null) {
            throw new MappingException("Class: " + clazz.getName() + " don't have Entity annotation");
        }
        return annotation;
    }

    private static Table getTable(Class clazz) {
        Table annotation = (Table) clazz.getAnnotation(Table.class);
        if (annotation == null) {
            throw new MappingException("Class: " + clazz.getName() + " don't have Table annotation");
        }
        return annotation;
    }

    private static class GetColumnData {
        private Field field;
        private String fieldName;
        private String fieldType;
        private String columnName;
        private boolean notNull;
        private boolean unique;
        private boolean primaryKey;
        private boolean autoIncremment;

        GetColumnData(Field field) {
            this.field = field;
        }

        String getFieldType() {
            return fieldType;
        }

        boolean isPrimaryKey() {
            return primaryKey;
        }

        boolean isAutoIncremment() {
            return autoIncremment;
        }

        String getColumnName() {
            return columnName;
        }

        boolean isNotNull() {
            return notNull;
        }

        boolean isUnique() {
            return unique;
        }

        GetColumnData invoke() {
            fieldType = field.getType().getSimpleName();
            columnName = fieldName = field.getName();

            /* set credentials for id */
            Id id = field.getAnnotation(Id.class);
            primaryKey = id != null;
            GeneratedValue ai = field.getAnnotation(GeneratedValue.class);
            autoIncremment = ai != null;

            /* set credentials for every column */
            Column column = field.getAnnotation(Column.class);
            if (column == null) {
                return this;
            }
            columnName = column.name();
            if (columnName.isEmpty()) {
                columnName = fieldName;
            }
            notNull = !column.nullable();
            unique = column.unique();


            return this;
        }
    }
}

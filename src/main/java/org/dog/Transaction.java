package org.dog;

import java.sql.Connection;
import java.sql.SQLException;

public class Transaction {

    private Connection connection;

    public Transaction(Connection connection) {
        this.connection = connection;
    }

    @FunctionalInterface
    public interface DoInTransaction {
        void act(Connection connection) throws SQLException;
    }

    public void doInTransaction(DoInTransaction action) throws SQLException {
        try {
            connection.setAutoCommit(false);
            try {
                action.act(connection);
            } catch (SQLException e) {
                connection.rollback();
                throw e;
            }
            connection.commit();
        } finally {
            connection.setAutoCommit(true);
        }
    }

}

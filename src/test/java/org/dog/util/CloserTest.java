package org.dog.util;

import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

public class CloserTest {
    @Test
    public void closeConnection() throws Exception {
        Connection mock = mock(Connection.class);
        doThrow(SQLException.class).when(mock).close();

        Closer.close(mock);
    }

    @Test
    public void closeStatement() throws Exception {
        Statement mock = mock(Statement.class);
        doThrow(SQLException.class).when(mock).close();

        Closer.close(mock);
    }
}
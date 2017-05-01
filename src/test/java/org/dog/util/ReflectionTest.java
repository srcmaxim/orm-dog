package org.dog.util;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import javax.persistence.Id;
import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.util.Calendar;
import java.util.Map;

import static org.hamcrest.CoreMatchers.endsWith;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Category(Util.class)
public class ReflectionTest {

    @Test
    public void getSetXXXMethod() throws Exception {
        Field idField = Clazz.class.getField("idField");
        Method getInt = Reflection.getSetXXXMethod(Clazz.class, idField);
        String s = getInt.toString();
        assertThat(s, endsWith("Clazz.setInt(int,int)"));
    }

    @Test
    public void getGetXXXMethod() throws Exception {
        Field idField = Clazz.class.getField("idField");
        Method getInt = Reflection.getGetXXXMethod(Clazz.class, idField);
        String actualMethodName = getInt.toString();
        assertThat(actualMethodName, endsWith("Clazz.getInt(int)"));
    }

    @Test
    public void setXXXToStatement() throws Exception {
        Object t = new Clazz();
        PreparedStatement mock = mock(TestStatement.class);

        int i = Reflection.setXXXToStatement(t, mock);

        assertEquals(2, i);
    }

    @Test
    public void getIdField() throws Exception {
        Clazz t = new Clazz();
        Field idField = Reflection.getIdField(t);
        String actualFieldName = idField.getName();

        assertEquals("idField", actualFieldName);
    }

    @Test
    public void setFields() throws Exception {
        Clazz t = new Clazz();
        ResultSet mock = mock(TestResultSet.class);
        int expected = Integer.MAX_VALUE;
        when(mock.getInt(1)).thenReturn(expected);

        Reflection.setFields(t, mock);

        assertEquals(expected, (int)(t.idField));
    }

    private static abstract class TestStatement implements PreparedStatement {
        @Override
        public void setInt(int a, int b) throws SQLException {}
    }

    private static abstract class TestResultSet implements ResultSet {
        @Override
        public int getInt(int columnIndex) throws SQLException {
            return 0;
        }
    }

    private static class Clazz {
        @Id
        public Integer idField;
        public Integer firstField = 0;
        public void setInt(int iterator, int value){}
        public void getInt(int value){}
    }
}
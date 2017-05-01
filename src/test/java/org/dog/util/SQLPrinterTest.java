package org.dog.util;

import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.sql.PreparedStatement;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Category(Util.class)
public class SQLPrinterTest {
    @Test
    public void printSqlStatement() throws Exception {
        PreparedStatement mock = mock(PreparedStatement.class);
        String expectedSQL = "INSERT INTO `orm_dog`.`books` (title, author, price) VALUES ('Title', 'Author', 111)";
        String toBeReturned = "com.mysql.jdbc.JDBC4PreparedStatement@36d64342: " + expectedSQL;
        when(mock.toString()).thenReturn(toBeReturned);

        String actual = SQLPrinter.printSqlStatement(mock);

        Assert.assertEquals(expectedSQL, actual);
    }
}
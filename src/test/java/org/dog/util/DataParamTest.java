package org.dog.util;

import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
@Category(Util.class)
public class DataParamTest {

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"byte", "INT"},
                {"short", "INT"},
                {"int", "INT"},
                {"long", "INT"},
                {"Byte", "INT"},
                {"Short", "INT"},
                {"Integer", "INT"},
                {"Long", "INT"},
                {"char", "CHAR(1)"},
                {"Character", "CHAR(1)"},
                {"String", "VARCHAR(45)"},
                {"float", "DOUBLE"},
                {"double", "DOUBLE"},
                {"Float", "DOUBLE"},
                {"Double", "DOUBLE"},
                {"boolean", "BIT"},
                {"Boolean", "BIT"},
                {"Data", "DATA"},
                {"Time", "TIME"},
                {"Blob", "BLOB"},
                {"Clob", "CLOB"}
        });
    }

    @Parameterized.Parameter
    public String input;
    @Parameterized.Parameter(1)
    public String expected;

    @Test
    public void toSQLType() throws Exception {
        String actual = Data.toSQLType(input);
        Assert.assertEquals(expected, actual);
    }
}
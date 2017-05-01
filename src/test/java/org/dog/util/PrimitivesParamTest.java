package org.dog.util;

import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Collection;

import static java.util.Arrays.asList;

@RunWith(Parameterized.class)
@Category(Util.class)
public class PrimitivesParamTest {

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return asList(new Object[][]{
                {Boolean.class, boolean.class},
                {Byte.class, byte.class},
                {Short.class, short.class},
                {Character.class, char.class},
                {Integer.class, int.class},
                {Long.class, long.class},
                {Float.class, float.class},
                {Double.class, double.class}
        });
    }

    @Parameterized.Parameter
    public Class input;
    @Parameterized.Parameter(1)
    public Class expected;

    @Test
    @SuppressWarnings("unchecked")
    public void wrap() throws Exception {
        Class actual = Primitives.wrap(input);
        Assert.assertEquals(expected, actual);
    }

}
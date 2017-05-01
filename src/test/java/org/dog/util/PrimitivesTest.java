package org.dog.util;

import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import static org.junit.Assert.*;

@Category(Util.class)
public class PrimitivesTest {
    @Test
    public void wrapPrimitive() throws Exception {
        Class<Class> wrapper = Class.class;
        Class<Class> primitive = Primitives.wrap(wrapper);

        Assert.assertSame(wrapper, primitive);
    }

    @Test
    public void wrapWrapper() throws Exception {
        Class<?> wrapperInteger = Integer.class;
        Class<?> primitiveInt = Primitives.wrap(wrapperInteger);

        Assert.assertNotSame(wrapperInteger, primitiveInt);
    }
}
package org.dog.util;

import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(Util.class)
public class DataTest {
    @Test(expected = RuntimeException.class)
    public void toSQLTypeNoSuchType() throws Exception {
        Data.toSQLType("No such type");
    }
}

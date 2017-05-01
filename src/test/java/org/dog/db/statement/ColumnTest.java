package org.dog.db.statement;

import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class ColumnTest {

    private static Column column;
    public static final String ID_NAME = "id";

    @BeforeClass
    public static void setup() {
        column = new Column(ID_NAME, "Integer", true, true, true, true);
    }

    @Test
    public void isPrimaryKey() throws Exception {
        assertTrue(column.isPrimaryKey());
    }

    @Test
    public void getName() throws Exception {
        assertEquals(ID_NAME, column.getName());
    }

    @Test
    public void create() throws Exception {
        String s = column.create();
        String expected = "`id` INT NOT NULL AUTO_INCREMENT";
        assertEquals(expected, s);
    }

    @Test
    public void createFooter() throws Exception {
        String footer = column.createFooter();
        String expected = "PRIMARY KEY (`id`),\n" +
                "UNIQUE INDEX `id_UNIQUE` (`id` ASC)";
        assertEquals(expected, footer);
    }

}
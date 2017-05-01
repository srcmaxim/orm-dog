package org.dog.db.statement;

import org.junit.BeforeClass;
import org.junit.Test;

import static java.util.Arrays.asList;
import static org.junit.Assert.*;

public class TableTest {

    static Table table;

    @BeforeClass
    public  static void initialize() throws Exception {
        table = Table.newInstance(Class.class, "orm_dog", "books");
        table.getColumns().addAll(asList(
            new Column("id", Long.class.getSimpleName(), true, true, true, false),
                new Column("title", String.class.getSimpleName(), false, true, false, false),
                new Column("author", String.class.getSimpleName(), false, true, false, false),
                new Column("price", Integer.class.getSimpleName(), false, true, false, false)
        ));
    }

    @Test
    public void create() throws Exception {
        String expected = "CREATE TABLE `orm_dog`.`books` (\n" +
                "`id` INT NOT NULL AUTO_INCREMENT,\n" +
                "`title` VARCHAR(45) NOT NULL,\n" +
                "`author` VARCHAR(45) NOT NULL,\n" +
                "`price` INT NOT NULL,\n" +
                "PRIMARY KEY (`id`))";

        String actual = table.create();

        assertEquals("Create Table script error", expected, actual);
    }

    @Test
    public void drop() throws Exception {
        String expected = "DROP TABLE `orm_dog`.`books`";

        String actual = table.drop();

        assertEquals("Drop Table script error", expected, actual);
    }

    @Test
    public void selectOne() throws Exception {
        String expected = "SELECT id, title, author, price FROM `orm_dog`.`books` WHERE `id`=?";

        String actual = table.selectOne();

        assertEquals("Drop Table script error", expected, actual);
    }

    @Test
    public void select() throws Exception {
        String expected = "SELECT id, title, author, price FROM `orm_dog`.`books`";

        String actual = table.select();

        assertEquals("Drop Table script error", expected, actual);
    }

    @Test
    public void insert() throws Exception {
        String expected = "INSERT INTO `orm_dog`.`books` (title, author, price) VALUES (?, ?, ?)";

        String actual = table.insert();

        assertEquals("Drop Table script error", expected, actual);
    }

    @Test
    public void update() throws Exception {
        String expected = "UPDATE `orm_dog`.`books` SET title=?, author=?, price=? WHERE `id`=?";

        String actual = table.update();

        assertEquals("Drop Table script error", expected, actual);
    }

    @Test
    public void delete() throws Exception {
        String expected = "DELETE FROM `orm_dog`.`books` WHERE `id`=?";

        String actual = table.deleteOne();

        assertEquals("Drop Table script error", expected, actual);
    }

}
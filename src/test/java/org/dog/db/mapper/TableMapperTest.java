package org.dog.db.mapper;

import org.dog.db.statement.Table;
import org.dog.entity.Book;
import org.junit.Test;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import static org.junit.Assert.assertEquals;

public class TableMapperTest {
    @Test
    public void link() throws Exception {
        TableMapper.link(Clazz.class);
        Table book = Table.getTable(Clazz.class);
        String createBook = book.create();

        String expected = "CREATE TABLE `AAA`.`BBB` (\n" +
                "`id` INT AUTO_INCREMENT,\n" +
                "`a` VARCHAR(45) NOT NULL,\n" +
                "`b` VARCHAR(45),\n" +
                "`ccc` VARCHAR(45),\n" +
                "PRIMARY KEY (`id`),\n" +
                "UNIQUE INDEX `b_UNIQUE` (`b` ASC))";

        assertEquals("Don't link class to proper table", expected, createBook);
    }

    @Entity
    @javax.persistence.Table(schema = "AAA", name = "BBB")
    private static class Clazz {
        @Id
        @GeneratedValue
        Long id;

        @Column(nullable = false)
        String a;

        @Column(unique = true)
        String b;

        @Column(name = "ccc")
        String c;
    }

    @Test(expected = MappingException.class)
    public void linkClassWithoutEntityAnnotation() throws Exception {
        TableMapper.link(ClazzWithoutEntityAnnotation.class);
    }

    @javax.persistence.Table()
    private static class ClazzWithoutEntityAnnotation{}

    @Test(expected = MappingException.class)
    public void linkClazzWithoutTableAnnotation() throws Exception {
        TableMapper.link(ClazzWithoutTableAnnotation.class);
    }

    @Entity
    private static class ClazzWithoutTableAnnotation{}


}
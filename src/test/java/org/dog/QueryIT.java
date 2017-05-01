package org.dog;

import org.dog.entity.Book;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;

@Category(Integration.class)
public class QueryIT {
    private DogORM orm;
    private Book book1 = new Book("Title1", "Author1", 111);
    private Book book2 = new Book("Title2", "Author2", 222);
    private Book book3 = new Book("Title3", "Author3", 333);
    private Book book4 = new Book("Title4", "Author4", 444);

    @Before
    public void connect() throws Exception {
        orm = DogORM.builder()
                .setUrl("jdbc:mysql://localhost:3306/")
                .setUser("root")
                .setPassword("root")
                .loadClasses("org.dog.entity")
                .build();

        try {
            orm.transaction().doInTransaction(connection -> {
                Query<Book> queryBook = new Query<>(connection, Book.class);
                /* drop all tables */
                queryBook.drop();
            });
        } catch (SQLException e) {
            /*throws exception if table doesn't exist */
        }

        orm.transaction().doInTransaction(connection -> {
            Query<Book> queryBook = new Query<>(connection, Book.class);
            /* create all tables */
            queryBook.create();
        });

        orm.transaction().doInTransaction(connection -> {
            Query<Book> queryBook = new Query<>(connection, Book.class);
            queryBook.insert(book1);
            queryBook.insert(book2);
            queryBook.insert(book3);
        });
    }

    @After
    public void disconnect() throws Exception {
        orm.close();
    }


    @Test
    public void insert() throws Exception {
        final int[] size = new int[1];
        final int[] newSize = new int[1];
        orm.transaction().doInTransaction(connection -> {
            Query<Book> queryBook = new Query<>(connection, Book.class);
            size[0] = queryBook.select().size();
        });
        orm.transaction().doInTransaction(connection -> {
            Query<Book> queryBook = new Query<>(connection, Book.class);
            queryBook.insert(book4);
        });
        orm.transaction().doInTransaction(connection -> {
            Query<Book> queryBook = new Query<>(connection, Book.class);
            newSize[0] = queryBook.select().size();
        });

        int expectedSize = size[0] + 1;
        assertEquals(expectedSize, newSize[0]);
    }

    @Test
    public void update() throws Exception {
        final Book[] firstSelect = new Book[1];
        final Book[] secondSelect = new Book[1];
        String newAuthor = "New Author";
        orm.transaction().doInTransaction(connection -> {
            Query<Book> queryBook = new Query<>(connection, Book.class);
            firstSelect[0] = queryBook.select(new Book(1L, "", "", 0));
            book1.setAuthor(newAuthor);
            queryBook.update(book1);
        });
        orm.transaction().doInTransaction(connection -> {
            Query<Book> queryBook = new Query<>(connection, Book.class);
            secondSelect[0] = queryBook.select(new Book(1L, "", "", 0));
        });
        assertNotEquals(firstSelect, secondSelect);
        assertEquals(newAuthor, secondSelect[0].getAuthor());
    }

    @Test
    public void delete() throws Exception {
        final Book[] firstSelect = new Book[1];
        final Book[] secondSelect = new Book[1];
        orm.transaction().doInTransaction(connection -> {
            Query<Book> queryBook = new Query<>(connection, Book.class);
            firstSelect[0] = queryBook.select(new Book(2L, "", "", 0));
            queryBook.delete(firstSelect[0]);
        });
        Book book = new Book(2L, "", "", 0);
        orm.transaction().doInTransaction(connection -> {
            Query<Book> queryBook = new Query<>(connection, Book.class);
            secondSelect[0] = queryBook.select(book);
        });

        assertNotEquals(firstSelect[0], secondSelect[0]);
        assertEquals(book, secondSelect[0]);
    }
}

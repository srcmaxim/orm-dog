package org.dog;

import org.dog.entity.Book;
import org.junit.*;
import org.junit.experimental.categories.Category;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@Category(Integration.class)
public class TransactionIT {

    private DogORM orm;

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
    }

    @After
    public void disconnect() throws Exception {
        orm.close();
    }


    @Test
    public void allEntitiesWillBeWritten() throws Exception {
        Book book1 = new Book("Book Name 1", "Author Name 1", 111);
        Book book2 = new Book("Book Name 2", "Author Name 2", 222);

        insertBooks(book1, book2);
        List<Book> books = queryBooks();

        assertEquals("Entity list is empty", 2, books.size());
    }

    @Test
    public void allEntitiesWontBeWritten() throws Exception {
        /* restriction in db: unique title so it cause exception */
        Book book1 = new Book("Same Name", "Author Name 1", 111);
        Book book2 = new Book("Same Name", "Author Name 2", 222);

        String why = null;
        try {
            insertBooks(book1, book2);
        } catch (SQLException e) {
            why = e.getMessage();
        }

        List<Book> books = queryBooks();

        assertEquals("Duplicate entry 'Same Name' for key 'title_UNIQUE'", why);
        assertTrue("Entity list is not empty", books.isEmpty());
    }

    private void insertBooks(Book book1, Book book2) throws SQLException {
        orm.transaction().doInTransaction(connection -> {
            Query<Book> queryBook = new Query<>(connection, Book.class);

            /* insert to tables */
            queryBook.insert(book1);
            queryBook.insert(book2);
        });
    }

    private List<Book> queryBooks() throws SQLException {
        List<Book> books = new ArrayList<>();
        orm.transaction().doInTransaction(connection -> {
            Query<Book> queryBook = new Query<>(connection, Book.class);
            books.addAll(queryBook.select());
        });
        return books;
    }

}
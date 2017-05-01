# ORM Dog. ORM Framework for Java

<div style="width: 300px; height: 300px; margin: 0 auto">
    <img src="http://www.clipartsfree.net/svg/other-dog_Clipart_svg_File.svg"></img>
</div>

### How it works      

1. Loads entities from your package.
2. For each entity declarated with @javax.persistent.Entity and @javax.persistent.Entity
creates Table with Columns. Also it scans for Id, GeneratedValue, Column annotations in Class.
3. Connects to DB using JDBC.
4. For every new query calls orm.transaction().doInTransaction([query])
    4.1 Query gets Table definition with all string for using in PreparedStatement.
    4.2 Query sets params for PreparedStatement (u can use many queries in transaction).
5. Depending on situation transaction will be committed or rolled back.

### Using API

```java
orm = DogORM.builder()
    .setUrl("dc connection url")
    .setUser("user")
    .setPassword("password")
    .loadClasses("package where entities are")
    .build();

try {
    orm.transaction().doInTransaction(connection -> {
        Query<Book> queryBook = new Query<>(connection, Book.class);
        queryBook.create();
        queryBook.insert(new Book("Book title", "Author", 100));
        queryBook.select().stream()
            .map(Object::toString).forEach(System.out::println)
    });
} catch(SqlException e) {
    e.printStackTrace();
}
```

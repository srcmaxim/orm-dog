package org.dog.entity;

import javax.persistence.*;

@Entity(name = "BOOK")
@Table(schema = "orm_dog", name = "books")
public class Book {

    @Id
    @GeneratedValue
    public Long id;

    @Column(nullable = false, unique = true)
    public String title;

    @Column(nullable = false)
    public String author;

    @Column(nullable = false)
    public Integer price;


    public Book(Long id, String title, String author, Integer price) {
        this(title, author, price);
        this.id = id;
    }

    public Book(String title, String author, Integer price) {
        this.title = title;
        this.author = author;
        this.price = price;
    }

    public Book() {
    }

    public Long getId() {
        return id;
    }

    public Book setId(Long id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public Book setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getAuthor() {
        return author;
    }

    public Book setAuthor(String author) {
        this.author = author;
        return this;
    }

    public Integer getPrice() {
        return price;
    }

    public Book setPrice(Integer price) {
        this.price = price;
        return this;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", price=" + price +
                '}';
    }
}

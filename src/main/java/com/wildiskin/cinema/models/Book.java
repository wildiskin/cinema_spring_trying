package com.wildiskin.cinema.models;


import jakarta.persistence.*;
import org.hibernate.annotations.Cascade;

@Entity
@Table(name="book")
public class Book {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name="name")
    private String name;

    @Column(name = "author")
    private String author;

    @Column(name = "genre")
    private String genre;

    @OneToOne(mappedBy = "sourceBook")
    private Movie movieChild;

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", author='" + author + '\'' +
                ", genre='" + genre + '\'' +
                ", movieChildId=" + movieChild +
                '}';
    }

    public Book() {
    }

    public Book(String name) {this.name = name;}

    public Book(String name, String author, String genre) {
        this.name = name;
        this.author = author;
        this.genre = genre;
    }

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public Movie getMovieChild() {
        return movieChild;
    }

    public void setMovieChildId(Movie movieChild) {
        this.movieChild = movieChild;
    }
}

package com.wildiskin.cinema.models;

import jakarta.persistence.*;

@Entity
@Table(name = "movie")
public class Movie {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name="name")
    private String name;

    @ManyToOne
    @JoinColumn(name="director_id", referencedColumnName = "id")
    private Director director;

    @Column(name="year")
    private int year;

    @Column(name = "description")
    private String description;

    @OneToOne()
    @JoinColumn(name = "source_book_id", referencedColumnName = "id")
    private Book sourceBook;

    public Movie() {}

    public Movie(String name, int year, String description) {
        this.name = name;
        this.year = year;
        this.description = description;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", director=" + director +
                ", year=" + year +
                ", description='" + description + '\'' +
                ", SourceBook=" + sourceBook +
                '}';
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

    public Director getDirector() {
        return director;
    }

    public void setDirector(Director director) {
        this.director = director;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Book getSourceBook() {
        return sourceBook;
    }

    public void setSourceBook(Book sourceBook) {
        this.sourceBook = sourceBook;
    }
}

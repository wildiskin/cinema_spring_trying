package com.wildiskin.cinema.DTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.Length;

public class BookDTO {

    private long id;

    @Size(min = 1, max = 50, message = "Book's name should be between 1 and 50 characters")
    @NotNull(message = "Book shall have a name")
    private String name;

    @Size(min = 2, max = 50, message = "Genre's name should be between 1 and 50 characters")
    private String genre;

    @Size(min = 1, max = 50, message = "Author's name should be between 1 and 50 characters")
    private String author;

    private String movieChildName;

    public BookDTO() {
    }

    public BookDTO(long id, String name, String genre, String author) {
        this.id = id;
        this.name = name;
        this.genre = genre;
        this.author = author;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMovieChildName() {
        return movieChildName;
    }

    public void setMovieChildName(String movieChildName) {
        this.movieChildName = movieChildName;
    }
}

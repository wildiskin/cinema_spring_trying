package com.wildiskin.cinema.DTO;

import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public class BookDTO {

    private long id;

    @Length(max = 50, message = "Название не должно превышать 50 символов")
    @NotNull(message = "У книги должно быть указано название")
    private String name;

    @Length(max = 50, message = "Жанр книги не должен превышать 50 символов")
    private String genre;

    @Length(max = 50, message = "Имя автора не должно превышать 50 символов")
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

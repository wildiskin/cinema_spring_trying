package com.wildiskin.cinema.DTO;

import com.wildiskin.cinema.models.Book;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public class BookDTO implements DtoI{
    @Length(max = 50, message = "Название не должно превышать 50 символов")
    @NotNull(message = "У книги должно быть указано название")
    private String name;

    @Length(max = 50, message = "Жанр книги не должен превышать 50 символов")
    private String genre;

    @Length(max = 50, message = "Имя автора не должно превышать 50 символов")
    private String author;

    public BookDTO() {
    }

    public BookDTO(String name, String genre, String author) {
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
}

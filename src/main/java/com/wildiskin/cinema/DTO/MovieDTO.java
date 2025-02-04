package com.wildiskin.cinema.DTO;

import com.wildiskin.cinema.util.BookNameId;
import com.wildiskin.cinema.util.DirectorNameId;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public class MovieDTO {

    private long id;

    @NotNull(message = "Название фильма - обязательное поле")
    @Length(min = 1, max = 50, message = "Название фильма должно быть от 1 до 50 символов")
    private String name;

    private DirectorNameId director;
    private BookNameId sourceBook;

    @NotNull
    @Digits(integer = 4, fraction = 0, message = "Укажите реальный год")
    @Min(value = 1895L, message = "Фильмов еще не существовало")
    @Max(value = 2025L, message = "Фильм должен быть вышедшим") //TODO my own annotation for comparing with current date
    private int year;

    @Length(max = 500, message = "Описание не должно занимать более 150 символов")
    private String description;

    public MovieDTO(long id, String name, int year, String description) {
        this.id = id;
        this.name = name;
        this.year = year;
        this.description = description;
    }

    public MovieDTO() {}

    public MovieDTO(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DirectorNameId getDirector() {
        return director;
    }

    public void setDirector(DirectorNameId director) {
        this.director = director;
    }

    public BookNameId getSourceBook() {
        return sourceBook;
    }

    public void setSourceBook(BookNameId sourceBook) {
        this.sourceBook = sourceBook;
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}

package com.wildiskin.cinema.DTO;

import com.wildiskin.cinema.util.BookNameId;
import com.wildiskin.cinema.util.DirectorNameId;
import jakarta.validation.constraints.*;


public class MovieDTO {

    private long id;

    @NotNull(message = "Название фильма - обязательное поле")
    @Size(min = 1, max = 50, message = "Название фильма должно быть от 1 до 50 символов")
    private String name;

    private DirectorNameId director;
    private BookNameId sourceBook;

    private String year;

    @Size(max = 500, message = "Описание не должно занимать более 150 символов")
    private String description;

    public MovieDTO(long id, String name, String year, String description) {
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

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
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

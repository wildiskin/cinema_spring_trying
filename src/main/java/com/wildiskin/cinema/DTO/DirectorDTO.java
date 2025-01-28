package com.wildiskin.cinema.DTO;

import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.*;

import java.util.ArrayList;
import java.util.List;

public class DirectorDTO {

    private long id;

    @Length(min = 2, max = 50, message = "Имя режиссера должно быть от 2 до 50 символов")
    @NotNull
    private String name;

    private List<String> movies;

    public DirectorDTO() {
        this.movies = new ArrayList<>();
    }

    public DirectorDTO(long id, String name) {
        this.id = id;
        this.name = name;
        this.movies = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getMovies() {
        return movies;
    }

    public void setMovies(List<String> movies) {
        this.movies = movies;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}



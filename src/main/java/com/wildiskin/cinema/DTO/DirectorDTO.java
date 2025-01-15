package com.wildiskin.cinema.DTO;

import org.hibernate.validator.constraints.*;

import java.util.ArrayList;
import java.util.List;

public class DirectorDTO {
    @Length(min = 2, max = 50, message = "Имя режиссера должно быть от 2 до 50 символов")
    String name;

    List<String> movies;

    public DirectorDTO() {
        this.movies = new ArrayList<>();
    }

    public DirectorDTO(String name) {
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
}



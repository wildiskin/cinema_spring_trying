package com.wildiskin.cinema.DTO;

import com.wildiskin.cinema.models.Movie;
import com.wildiskin.cinema.util.MovieNameId;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.*;

import java.util.ArrayList;
import java.util.List;

public class DirectorDTO {

    private long id;

    @Length(min = 2, max = 50, message = "Director's name should be between 2 and 50 characters")
    @NotNull
    private String name;

    private List<MovieNameId> movies;

    public DirectorDTO() {
        this.movies = new ArrayList<>();
    }

    public DirectorDTO(long id, String name) {
        this.id = id;
        this.name = name;
        this.movies = new ArrayList<>();
    }

    public DirectorDTO(long id, String name, List<MovieNameId> movies) {
        this.id = id;
        this.name = name;
        this.movies = movies;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<MovieNameId> getMovies() {
        return movies;
    }

    public void setMovies(List<MovieNameId> movies) {
        this.movies = movies;
    }

    public void setMoviesFromMovies(List<Movie> movies) {
        List<MovieNameId> list = new ArrayList<>(movies.size());
        for (Movie m : movies) {
            MovieNameId res = new MovieNameId(m.getId(), m.getName());
            list.add(res);
        }
        this.movies = list;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}



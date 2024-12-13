package com.wildiskin.cinema.DTO;

import com.wildiskin.cinema.models.Movie;
import org.hibernate.validator.constraints.*;

import java.util.List;

public class DirectorDTO implements DtoI{
    @Length(min = 2, max = 50, message = "Имя режиссера должно быть от 2 до 50 символов")
    String name;
    List<String> ownMovies;

    public DirectorDTO() {}

    public DirectorDTO(String name, List<String> ownMovies) {
        this.name = name;
        this.ownMovies = ownMovies;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getOwnMovies() {
        return ownMovies;
    }

    public void setOwnMovies(List<String> ownMovies) {
        this.ownMovies = ownMovies;
    }
}

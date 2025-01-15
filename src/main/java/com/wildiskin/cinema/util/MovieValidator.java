package com.wildiskin.cinema.util;

import com.wildiskin.cinema.DTO.MovieDTO;
import com.wildiskin.cinema.models.Movie;
import com.wildiskin.cinema.repositories.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class MovieValidator implements Validator {
    private MovieRepository movieRepository;

    @Autowired
    public void MovieValidator(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return MovieDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        MovieDTO movieDTO = (MovieDTO) target;
        if (movieRepository.findByName(movieDTO.getName()) != null) {
            errors.rejectValue("name", "", "Такое кино уже добавлено");
        }
    }
}

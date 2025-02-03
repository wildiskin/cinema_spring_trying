package com.wildiskin.cinema.util;

import com.wildiskin.cinema.DTO.DirectorDTO;
import com.wildiskin.cinema.models.Director;
import com.wildiskin.cinema.models.Movie;
import com.wildiskin.cinema.services.DirectorService;
import com.wildiskin.cinema.services.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.ArrayList;
import java.util.List;

@Component
public class DirectorUpdateValidator implements Validator {

    private final MovieService movieService;
    private final DirectorService directorService;

    @Autowired
    public DirectorUpdateValidator(MovieService movieService, DirectorService directorService) {
        this.movieService = movieService;
        this.directorService = directorService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return DirectorDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        DirectorDTO dir = (DirectorDTO) target;
        Director director = directorService.findById(dir.getId());
        if (director == null) {errors.rejectValue("id", "", "This director doesn't exist");}
        List<MovieNameId> moviess = dir.getMovies();
        for (MovieNameId mni : moviess) {
            if (mni.getName().isBlank()) {continue;}
            if (movieService.findByName(mni.getName()) == null) {errors.rejectValue("movie", "", "Entered movie not found");}
        }
    }
}

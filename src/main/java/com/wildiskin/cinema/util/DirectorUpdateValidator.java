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
        List<MovieNameId> movies = dir.getMovies();
        MovieNameId newMovie = dir.getNewMovie();
        for (MovieNameId mni : movies) {
            if (mni.getName().isBlank()) {continue;}
            if (movieService.findByName(mni.getName()) == null) {errors.rejectValue("movies", "", "Entered movie(s) not found");}
        }

        if (!newMovie.getName().isBlank()) {
            if (movieService.findByName(newMovie.getName()) == null) {
                errors.rejectValue("movies", "", "Entered movie(s) not found");
            }
        }
    }

    public void validateByEntity(DirectorDTO target, Director entity, Errors errors) {

        if (target.getId() != entity.getId()) {throw new RuntimeException("target with id: " + target.getId()
                + " aren't comparable with entity having id: " + entity.getId() + " in this method");}

        if (directorService.findByName(target.getName()) != null && !target.getName().equalsIgnoreCase(entity.getName())) {
            errors.rejectValue("name", "", "there is already such a director");
        }
    }
}

package com.wildiskin.cinema.util;

import com.wildiskin.cinema.DTO.MovieDTO;
import com.wildiskin.cinema.models.Director;
import com.wildiskin.cinema.services.BookService;
import com.wildiskin.cinema.services.DirectorService;
import com.wildiskin.cinema.services.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class MovieUpdateValidator implements Validator {

    private final DirectorService directorService;
    private final BookService bookService;
    private final MovieService movieService;

    @Autowired
    public MovieUpdateValidator(DirectorService directorService, BookService bookService, MovieService movieService) {
        this.directorService = directorService;
        this.bookService = bookService;
        this.movieService = movieService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return MovieDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        MovieDTO mdto = (MovieDTO) target;

        if (movieService.findById(mdto.getId()) == null) {errors.rejectValue("id", "", "Invalid movie id");}

        String directorName = mdto.getDirector() == null ? null : mdto.getDirector().getName();
        if (directorName != null && !directorName.isBlank()) {
            if (directorService.findByName(directorName) == null)
                errors.rejectValue("director", "", "This director doesn't exist");
        }

        String bookName = mdto.getSourceBook() == null ? null : mdto.getSourceBook().getName();
        if (bookName != null && !bookName.isBlank()) {
            if (bookService.findByName(bookName) == null)
                errors.rejectValue("sourceBook", "", "This book doesn't exist");
        }
    }
}

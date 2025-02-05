package com.wildiskin.cinema.util;

import com.wildiskin.cinema.DTO.MovieDTO;
import com.wildiskin.cinema.models.Director;
import com.wildiskin.cinema.models.Movie;
import com.wildiskin.cinema.services.BookService;
import com.wildiskin.cinema.services.DirectorService;
import com.wildiskin.cinema.services.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Calendar;
import java.util.Date;

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

        String name = mdto.getName();
        if (name.isBlank()) {errors.rejectValue("name", "", "Name should be not empty");}
        else {
            Movie movie = movieService.findByName(name);
            Movie originalMovie = movieService.findById(mdto.getId());
            if (movie != null && !movie.equals(originalMovie)) {errors.rejectValue("name", "", "There is such a movie");}
        }

        Integer year = mdto.getYear().isBlank() ? null : Integer.parseInt(mdto.getYear());

        if (movieService.findById(mdto.getId()) == null) {errors.rejectValue("id", "", "Invalid movie id");}

        if (year != null) {
            if (!(year > 1887 && year < getYear())) {
                errors.rejectValue("year", "", "Movie should have a real realise date");
            }
        }

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

    private int getYear() {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.YEAR);
    }
}

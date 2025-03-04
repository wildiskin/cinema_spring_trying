package com.wildiskin.cinema.util;

import com.wildiskin.cinema.DTO.BookDTO;
import com.wildiskin.cinema.models.Movie;
import com.wildiskin.cinema.repositories.BookRepository;
import com.wildiskin.cinema.services.BookService;
import com.wildiskin.cinema.services.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class BookValidator implements Validator {

    private final BookService bookService;
    private final MovieService movieService;

    @Autowired
    public BookValidator(BookService bookService, MovieService movieService) {
        this.bookService = bookService;
        this.movieService = movieService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return BookDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        BookDTO book = (BookDTO) target;
        String movieString = book.getMovieChildName();
        if (bookService.findByName(book.getName()) != null) {
            errors.rejectValue("name", "", "There is already such a book");
        }
        if (movieString != null) {
            if (!movieString.isBlank()) {
                Movie movie = movieService.findByName(movieString);
                if (movie == null) {
                    errors.rejectValue("movieChildName", "", "There is no movie by the \"" + movieString + "\" in collection");
                }
            }
        }
    }
}

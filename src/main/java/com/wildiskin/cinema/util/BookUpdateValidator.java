package com.wildiskin.cinema.util;

import com.wildiskin.cinema.DTO.BookDTO;
import com.wildiskin.cinema.models.Book;
import com.wildiskin.cinema.models.Movie;
import com.wildiskin.cinema.services.BookService;
import com.wildiskin.cinema.services.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class BookUpdateValidator implements Validator {

    private final BookService bookService;
    private final MovieService movieService;

    @Autowired
    public BookUpdateValidator(BookService bookService, MovieService movieService) {
        this.bookService = bookService;
        this.movieService = movieService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.equals(Book.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        BookDTO book = (BookDTO) target;
        String movieName = book.getMovieChildName();

        if (bookService.findById(book.getId()) == null) {
            errors.rejectValue("id", "", "Book with this id: "
                    + book.getId() + " doesn't exists");
        }
        if (!movieName.isBlank()) {
            Movie movie = movieService.findByName(movieName);
            if (movie == null) {errors.rejectValue("movieChildName", "",
                    "There is no movie with name: \"" + book.getMovieChildName() + "\" in collection");}
        }
    }
}

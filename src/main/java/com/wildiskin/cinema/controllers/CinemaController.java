package com.wildiskin.cinema.controllers;


import com.wildiskin.cinema.DTO.MovieDTO;
import com.wildiskin.cinema.models.Book;
import com.wildiskin.cinema.models.Director;
import com.wildiskin.cinema.models.Movie;
import com.wildiskin.cinema.services.BookService;
import com.wildiskin.cinema.services.DirectorService;
import com.wildiskin.cinema.services.MovieService;
import com.wildiskin.cinema.util.NotFoundException;
import com.wildiskin.cinema.util.UniversalErrorObject;
import com.wildiskin.cinema.util.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.support.BindingAwareModelMap;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class CinemaController {

    private final MovieService movieService;
    private final BookService bookService;
    private final DirectorService directorService;

    @Autowired
    public CinemaController(MovieService movieService, BookService bookService, DirectorService directorService) {
        this.movieService = movieService;
        this.bookService = bookService;
        this.directorService = directorService;
    }

    @GetMapping()
    public String mainPage() {
        return "index";
    }

    @GetMapping("movie/{id}")
    public String movieCard(@PathVariable("id") String id, Model model) {
        Movie movie = movieService.findById(Long.parseLong(id));
        if (movie == null) {throw new NotFoundException("Movie with this id: " + id + " doesn't exists");}
        MovieDTO m = new MovieDTO(movie.getId(), movie.getName(), movie.getYear(), movie.getDescription());
        String directorName = movie.getDirector() == null ? "unknown director" : movie.getDirector().getName();
        String bookName = movie.getSourceBook() == null ? "" : movie.getSourceBook().getName();
        m.setDirector(directorName);
        m.setSourceBook(bookName);
        model.addAttribute("movie", m);
        return "cards/movie";
    }

    @GetMapping("director/{id}")
    public String directorCard(@PathVariable("id") String id, Model model) {
        Director director = directorService.findById(Long.parseLong(id));
        if (director == null) {throw new NotFoundException("Director with this id: " + id + " doesn't exists");}
        model.addAttribute(director);
        return "cards/director";
    }

    @GetMapping("book/{id}")
    public String bookCard(@PathVariable("id") String id, Model model) {
        Book book = bookService.findById(Long.parseLong(id));
        if (book == null) {throw new NotFoundException("Book with this id: " + id + " doesn't exists");}
        model.addAttribute(book);
        return "cards/book";
    }

    @ExceptionHandler
    public ResponseEntity<String> handlerInvalidIdInput(NotFoundException er) {
        return new ResponseEntity<>(er.getMessage() + " <a href='/'>back</a>", HttpStatus.NOT_FOUND);
    }
}
package com.wildiskin.cinema.controllers;


import com.wildiskin.cinema.DTO.BookDTO;
import com.wildiskin.cinema.DTO.MovieDTO;
import com.wildiskin.cinema.models.Book;
import com.wildiskin.cinema.models.Director;
import com.wildiskin.cinema.models.Movie;
import com.wildiskin.cinema.services.BookService;
import com.wildiskin.cinema.services.DirectorService;
import com.wildiskin.cinema.services.MovieService;
import com.wildiskin.cinema.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.validation.support.BindingAwareModelMap;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/")
public class CinemaController {

    private final MovieService movieService;
    private final BookService bookService;
    private final DirectorService directorService;
    private final BookValidator bookValidator;
    private final DirectorValidator directorValidator;
    private final MovieValidator movieValidator;

    @Autowired
    public CinemaController(MovieService movieService, BookService bookService, DirectorService directorService, BookValidator bookValidator, DirectorValidator directorValidator, MovieValidator movieValidator) {
        this.movieService = movieService;
        this.bookService = bookService;
        this.directorService = directorService;
        this.bookValidator = bookValidator;
        this.directorValidator = directorValidator;
        this.movieValidator = movieValidator;
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
        BookDTO b = new BookDTO(book.getId(), book.getName(), book.getGenre(), book.getAuthor());
        String movieChildName = book.getMovieChild() == null ? null : book.getMovieChild().getName();
        b.setMovieChildName(movieChildName);
        model.addAttribute("book", b);
        return "cards/book";
    }

    @GetMapping("delete/{type}/{id}")
    public String deleteEntity(@PathVariable("type") String type, @PathVariable("id") String id) {
        if (type.equalsIgnoreCase(EntityType.BOOK.name())) {
            Book book = bookService.findById(Long.parseLong(id));
            if (book.getMovieChild() != null) {book.getMovieChild().setSourceBook(null);}
            bookService.delete(Long.parseLong(id));
        }
        else if (type.equalsIgnoreCase(EntityType.MOVIE.name())) {
            Movie movie = movieService.findById(Long.parseLong(id));
            if (movie.getSourceBook() != null) {movie.getSourceBook().setMovieChildId(null);}
            if (movie.getDirector() != null) {movie.getDirector().getMovies().remove(movie);}
            movieService.delete(Long.parseLong(id));
        }
        else if (type.equalsIgnoreCase(EntityType.DIRECTOR.name())) {
            Director director = directorService.findById(Long.parseLong(id));
            for (Movie m : director.getMovies()) {m.setDirector(null);}
            directorService.delete(Long.parseLong(id));
        }
        else {
            throw new RuntimeException("there is no type for deleting");
        }
        return "redirect:/all/" + type.toLowerCase() + "s";
    }

    @PostMapping("update/book")
    public String updateBook(@ModelAttribute("book") @Validated BookDTO bookDTO, BindingResult bindingResult) {
        Movie movie = movieService.findByName(bookDTO.getMovieChildName());
        if (movie == null) {bindingResult.rejectValue("movieChildName", "", "You can add films only from collection");}
        if (((Long) bookDTO.getId()) == null) {bindingResult.rejectValue("name", "", "There is no id");}
        System.out.println(bookDTO.getId());
        if (bindingResult.hasErrors()) {
            return "book/" + 4;
        }
        Book book = bookService.findById(bookDTO.getId());
        book.setName(bookDTO.getName());
        book.setAuthor(bookDTO.getAuthor());
        book.setGenre(bookDTO.getGenre());
        book.setMovieChildId(movie);
        bookService.save(book);
        return "redirect:/all/books";
    }

    @ExceptionHandler
    public ResponseEntity<String> handlerInvalidIdInput(NotFoundException er) {
        return new ResponseEntity<>(er.getMessage() + " <a href='/'>back</a>", HttpStatus.NOT_FOUND);
    }
}
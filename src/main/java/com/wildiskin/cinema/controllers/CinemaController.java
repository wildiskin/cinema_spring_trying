package com.wildiskin.cinema.controllers;


import com.wildiskin.cinema.DTO.BookDTO;
import com.wildiskin.cinema.DTO.DirectorDTO;
import com.wildiskin.cinema.DTO.MovieDTO;
import com.wildiskin.cinema.models.Book;
import com.wildiskin.cinema.models.Director;
import com.wildiskin.cinema.models.Movie;
import com.wildiskin.cinema.services.BookService;
import com.wildiskin.cinema.services.DirectorService;
import com.wildiskin.cinema.services.MovieService;
import com.wildiskin.cinema.util.*;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;

@Controller
@RequestMapping("/")
public class CinemaController {

    private final MovieService movieService;
    private final BookService bookService;
    private final DirectorService directorService;
    private final BookUpdateValidator bookUpdateValidator;
    private final DirectorUpdateValidator directorUpdateValidator;
    private final MovieValidator movieValidator;

    @Autowired
    public CinemaController(MovieService movieService, BookService bookService, DirectorService directorService, BookUpdateValidator bookUpdateValidator, DirectorUpdateValidator directorUpdateValidator, MovieValidator movieValidator) {
        this.movieService = movieService;
        this.bookService = bookService;
        this.directorService = directorService;
        this.bookUpdateValidator = bookUpdateValidator;
        this.directorUpdateValidator = directorUpdateValidator;
        this.movieValidator = movieValidator;
    }

    @GetMapping
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
        DirectorDTO dir = new DirectorDTO(director.getId(), director.getName());
        dir.setMoviesFromMovies(director.getMovies());
        model.addAttribute("director", dir);
//        model.addAttribute("movie", new MovieNameId(-1, "plug"));
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
            if (book == null) {throw new NotFoundException("Book with this id: " + id + " doesn't exists");}
            if (book.getMovieChild() != null) {book.getMovieChild().setSourceBook(null);}
            bookService.delete(Long.parseLong(id));
        }
        else if (type.equalsIgnoreCase(EntityType.MOVIE.name())) {
            Movie movie = movieService.findById(Long.parseLong(id));
            if (movie == null) {throw new NotFoundException("Movie with this id: " + id + " doesn't exists");}
            if (movie.getSourceBook() != null) {movie.getSourceBook().setMovieChildId(null);}
            if (movie.getDirector() != null) {movie.getDirector().getMovies().remove(movie);}
            movieService.delete(Long.parseLong(id));
        }
        else if (type.equalsIgnoreCase(EntityType.DIRECTOR.name())) {
            Director director = directorService.findById(Long.parseLong(id));
            if (director == null) {throw new NotFoundException("Director with this id: " + id + " doesn't exists");}
            for (Movie m : director.getMovies()) {m.setDirector(null);}
            directorService.delete(Long.parseLong(id));
        }
        else {
            throw new InputMismatchException("There is no type to handle");
        }
        return "redirect:/all/" + type.toLowerCase() + "s";
    }

    @PostMapping("update/book")
    public String updateBook(@ModelAttribute("book") @Validated BookDTO bookDTO, BindingResult bindingResult) {
        bookUpdateValidator.validate(bookDTO, bindingResult);

        if (bindingResult.hasErrors()) {
            return "cards/book";
        }

        Book book = bookService.findById(bookDTO.getId());
        book.setName(bookDTO.getName());
        book.setAuthor(bookDTO.getAuthor());
        book.setGenre(bookDTO.getGenre());
        Movie movie = movieService.findByName(bookDTO.getMovieChildName());

        Movie existingMovie = book.getMovieChild();

        if (movie != null) {
            existingMovie.setSourceBook(null);
            book.setMovieChildId(movie);
            movie.setSourceBook(book);
            movieService.save(movie);
        }
        else {
            book.setMovieChildId(null);
            existingMovie.setSourceBook(null);
        }

        bookService.save(book);
        return "redirect:/all/books";
    }

    @PostMapping("update/director")
    public String updateDirector(@ModelAttribute("director") @Validated DirectorDTO directorDTO, BindingResult bindingResult) {
        directorUpdateValidator.validate(directorDTO, bindingResult);

        if (bindingResult.hasErrors()) {
            return "cards/director";
        }

        Director director = directorService.findById(directorDTO.getId());
        List<MovieNameId> movieList = directorDTO.getMovies();
        List<Movie> list = new ArrayList<>(movieList.size());
        for (MovieNameId mni : movieList) {

            Movie movie = movieService.findByName(mni.getName());
            list.add(movie);
            movie.setDirector(director);
            movieService.save(movie);
        }
        director.setName(directorDTO.getName());
        director.setMovies(list);
        directorService.save(director);

        return "redirect:/all/directors";
    }

    @ExceptionHandler
    public ResponseEntity<String> handlerInvalidIdInput(NotFoundException er) {
        return new ResponseEntity<>(er.getMessage() + " <a href='/'>back</a>", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<String> handlerHibernateEx(HibernateException he) {
        return new ResponseEntity<>(" <a href='/'>1 book - 1 movie, back to main page</a>", HttpStatus.CONFLICT);
    }
}
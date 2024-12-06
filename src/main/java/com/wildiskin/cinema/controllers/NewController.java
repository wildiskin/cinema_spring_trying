package com.wildiskin.cinema.controllers;

import com.wildiskin.cinema.models.Book;
import com.wildiskin.cinema.models.Director;
import com.wildiskin.cinema.models.Movie;
import com.wildiskin.cinema.services.BookService;
import com.wildiskin.cinema.services.DirectorService;
import com.wildiskin.cinema.services.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/new")
public class NewController {

    private final MovieService movieService;
    private final DirectorService directorService;
    private final BookService bookService;

    @Autowired
    public NewController(MovieService movieService, DirectorService directorService, BookService bookService) {
        this.movieService = movieService;
        this.directorService = directorService;
        this.bookService = bookService;
    }

    @GetMapping("/movie")
    public String Movie(@ModelAttribute("movie") Movie movie) {
        return "new/movie";
    }

    @GetMapping("/book")
    public String Book(@ModelAttribute("book") Book book) {
        return "new/book";
    }

    @GetMapping("/director")
    public String Director(@ModelAttribute("director") Director director) {
        return "new/director";
    }

    @PostMapping("/movie")
    public String createMovie(@ModelAttribute("movie") Movie movie, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "/new/movie";
        }
        movieService.save(movie);
        return "redirect:/";
    }

    @PostMapping("/director")
    public String createDirector(@ModelAttribute("director") Director director, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "/new/director";
        }
        directorService.save(director);
        return "redirect:/";
    }

    @PostMapping("/book")
    public String createBook(@ModelAttribute("book") Book book, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "/new/book";
        }
        bookService.save(book);
        return "redirect:/";
    }
}
package com.wildiskin.cinema.controllers;

import com.wildiskin.cinema.DTO.BookDTO;
import com.wildiskin.cinema.DTO.DirectorDTO;
import com.wildiskin.cinema.DTO.MovieDTO;
import com.wildiskin.cinema.models.Book;
import com.wildiskin.cinema.models.Director;
import com.wildiskin.cinema.services.BookService;
import com.wildiskin.cinema.services.DirectorService;

import com.wildiskin.cinema.services.MovieService;
import com.wildiskin.cinema.util.BookValidator;
import com.wildiskin.cinema.util.DirectorValidator;
import com.wildiskin.cinema.util.MovieValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
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
    private final MovieValidator movieValidator;
    private final BookValidator bookValidator;
    private final DirectorValidator directorValidator;

    @Autowired
    public NewController(MovieService movieService, DirectorService directorService, BookService bookService, MovieValidator movieValidator, BookValidator bookValidator, DirectorValidator directorValidator) {
        this.movieService = movieService;
        this.directorService = directorService;
        this.bookService = bookService;
        this.movieValidator = movieValidator;
        this.bookValidator = bookValidator;
        this.directorValidator = directorValidator;
    }

    @GetMapping("/movie")
    public String Movie(@ModelAttribute("movieDTO") MovieDTO movieDTO) {
        return "new/movie";
    }

    @GetMapping("/book")
    public String Book(@ModelAttribute("book") BookDTO bookDTO) {
        return "new/book";
    }

    @GetMapping("/director")
    public String Director(@ModelAttribute("director") DirectorDTO directorDTO) {
        return "new/director";
    }

    @PostMapping("/movie")
    public String createMovie(@ModelAttribute("movieDTO") @Validated MovieDTO movieDTO, BindingResult bindingResult) {
        movieValidator.validate(movieDTO, bindingResult);

        if (bindingResult.hasErrors()) {
            return "/new/movie";
        }

        movieService.save(movieDTO);

        return "redirect:/";
    }

    @PostMapping("/director")
    public String createDirector(@ModelAttribute("director") DirectorDTO directorDTO, BindingResult bindingResult) {
        directorValidator.validate(directorDTO, bindingResult);

        if (bindingResult.hasErrors()) {
            return "/new/director";
        }

        directorService.save(directorDTO);
        return "redirect:/";
    }

    @PostMapping("/book")
    public String createBook(@ModelAttribute("book") BookDTO bookDTO, BindingResult bindingResult) {
        bookValidator.validate(bookDTO, bindingResult);

        if (bindingResult.hasErrors()) {
            return "/new/book";
        }

        bookService.save(bookDTO);
        return "redirect:/";
    }
}
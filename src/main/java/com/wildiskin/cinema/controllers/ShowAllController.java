package com.wildiskin.cinema.controllers;

import com.wildiskin.cinema.DTO.BookDTO;
import com.wildiskin.cinema.DTO.DirectorDTO;
import com.wildiskin.cinema.DTO.MovieDTO;
import com.wildiskin.cinema.models.Director;
import com.wildiskin.cinema.models.Movie;
import com.wildiskin.cinema.services.BookService;
import com.wildiskin.cinema.services.DirectorService;
import com.wildiskin.cinema.services.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
//@RequestMapping("/all")
public class ShowAllController {

    private final MovieService movieService;
    private final DirectorService directorService;
    private final BookService bookService;

    @Autowired
    public ShowAllController(MovieService movieService, DirectorService directorService, BookService bookService) {
        this.movieService = movieService;
        this.directorService = directorService;
        this.bookService = bookService;
    }

    @GetMapping(value = {"/all/movies"})
    public String showMovies(Model model) {
        model.addAttribute("movies", movieService.findAllDto());

        return "all/movies";
    }

    @GetMapping("/all/directors")
    public String showDirectors(Model model) {
        model.addAttribute("directors", directorService.findAllDto());

        return "all/directors";
    }

    @GetMapping(value = {"/all/books", "/book/null"})
    public String showBooks(Model model) {
        model.addAttribute("books", bookService.findAllDto());

        return "all/books";
    }
}

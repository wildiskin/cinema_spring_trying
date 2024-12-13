package com.wildiskin.cinema.controllers;

import com.wildiskin.cinema.DTO.BookDTO;
import com.wildiskin.cinema.DTO.DirectorDTO;
import com.wildiskin.cinema.DTO.MovieDTO;
import com.wildiskin.cinema.models.Director;
import com.wildiskin.cinema.models.Movie;
import com.wildiskin.cinema.services.MainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/all")
public class ShowAllController {

    private final MainService mainService;

    @Autowired
    public ShowAllController(MainService mainService) {
        this.mainService = mainService;
    }


    @GetMapping("/movies")
    public String showMovies(Model model) {
        model.addAttribute("movies", mainService.findAllMovies());
        return "all/movies";
    }

    @GetMapping("/directors")
    public String showDirectors(Model model) {
        model.addAttribute("directors", mainService.findAllDirectors());

        return "all/directors";
    }

    @GetMapping("/books")
    public String showBooks(Model model) {
        model.addAttribute("books", mainService.findAllBooks());

        return "all/books";
    }
}

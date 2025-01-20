package com.wildiskin.cinema.controllers;


import com.wildiskin.cinema.models.Book;
import com.wildiskin.cinema.models.Director;
import com.wildiskin.cinema.services.BookService;
import com.wildiskin.cinema.services.DirectorService;
import com.wildiskin.cinema.services.MovieService;
import com.wildiskin.cinema.util.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
    public String movieCard() {return "cards/movie";}

    @GetMapping("director/{id}")
    public String directorCard(@PathVariable("id") String id, Model model, BindingResult bindingResult) {
        Director director = directorService.findById(Long.parseLong(id));
        model.addAttribute(director);
        return "cards/director";
    }

    @GetMapping("book/{id}")
    public String bookCard(@PathVariable("id") String id, Model model, BindingResult bindingResult) {
        Book book = bookService.findById(Long.parseLong(id));
        model.addAttribute(book);
        return "cards/book";
    }
}

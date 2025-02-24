package com.wildiskin.cinema.controllers;


import com.wildiskin.cinema.DTO.BookDTO;
import com.wildiskin.cinema.DTO.DirectorDTO;
import com.wildiskin.cinema.DTO.MovieDTO;
import com.wildiskin.cinema.DTO.UserDTO;
import com.wildiskin.cinema.models.Book;
import com.wildiskin.cinema.models.Director;
import com.wildiskin.cinema.models.Movie;
import com.wildiskin.cinema.models.User;
import com.wildiskin.cinema.services.*;
import com.wildiskin.cinema.util.*;
import jakarta.mail.MessagingException;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/")
@SessionAttributes(value = {"movie"})
public class CinemaController {

    private final MovieService movieService;
    private final BookService bookService;
    private final DirectorService directorService;
    private final BookUpdateValidator bookUpdateValidator;
    private final DirectorUpdateValidator directorUpdateValidator;
    private final MovieUpdateValidator movieUpdateValidator;
    private final UserService userService;
    private final RegisterService registerService;

    public CinemaController(MovieService movieService, BookService bookService, DirectorService directorService, BookUpdateValidator bookUpdateValidator, DirectorUpdateValidator directorUpdateValidator, MovieUpdateValidator movieUpdateValidator, UserService userService, RegisterService registerService) {
        this.movieService = movieService;
        this.bookService = bookService;
        this.directorService = directorService;
        this.bookUpdateValidator = bookUpdateValidator;
        this.directorUpdateValidator = directorUpdateValidator;
        this.movieUpdateValidator = movieUpdateValidator;
        this.userService = userService;
        this.registerService = registerService;
    }

    @ModelAttribute("movie")
    public Movie createMovie() {
        return new Movie();
    }

    @GetMapping
    public String mainPage() {
        return "index";
    }

    @GetMapping("movie/{id}")
    public String movieCard(@PathVariable("id") String id, Model model) {
        Movie movie = movieService.findById(Long.parseLong(id));
        String year = movie.getYear() == null ? "" : movie.getYear().toString();
        if (movie == null) {throw new NotFoundException("Movie with this id: " + id + " doesn't exists");}
        MovieDTO m = new MovieDTO(movie.getId(), movie.getName(), year, movie.getDescription());

        Director director = movie.getDirector();
        if (director == null) {m.setDirector(null);}
        else {
            DirectorNameId dni = new DirectorNameId(director.getId(), director.getName());
            m.setDirector(dni);
        }

        Book book = movie.getSourceBook();
        if (book == null) {m.setSourceBook(null);}
        else {
            BookNameId bni = new BookNameId(book.getId(), book.getName());
            m.setSourceBook(bni);
        }

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

        Director director = directorService.findById(directorDTO.getId());

        directorUpdateValidator.validateByEntity(directorDTO, director, bindingResult);

        if (bindingResult.hasErrors()) {
            return "cards/director";
        }

        for (String s : directorDTO.getMovies().stream().map((z) -> z.getName()).collect(Collectors.toList())) {
            System.out.println(s);
        }



        List<MovieNameId> list = directorDTO.getMovies();
        list.add(directorDTO.getNewMovie());

        for (Movie m: director.getMovies()) {
            m.setDirector(null);
        }

        director.setMovies(null);

        List<Movie> itog = new ArrayList<>(list.size());
        for (MovieNameId mni : list) {
            if (!mni.getName().isBlank()) {
                Movie movie = movieService.findByName(mni.getName());
                itog.add(movie);
                movie.setDirector(director);
                movieService.save(movie);
            }
        }


        director.setName(directorDTO.getName());
        director.setMovies(itog);
        directorService.save(director);

        return "redirect:/all/directors";
    }

    @PostMapping("update/movie")
    public String updateMovie(@ModelAttribute("movie") @Validated MovieDTO movieDTO, BindingResult bindingResult) {

        movieUpdateValidator.validate(movieDTO, bindingResult);

        if (bindingResult.hasErrors()) {
            return "cards/movie";
        }

        Movie movie = movieService.findById(movieDTO.getId());

        movie.setName(movieDTO.getName());

        if ( movieDTO.getYear().isBlank() ) { movie.setYear(null); }
        else { movie.setYear( Integer.parseInt( movieDTO.getYear() ) ); }

        movie.setDescription(movieDTO.getDescription());

        DirectorNameId dni = movieDTO.getDirector();
        if (dni != null) {
            Director newDirector = directorService.findByName(dni.getName());
            Director pastDirector = directorService.findById(dni.getId());

            if (pastDirector != null) {
                pastDirector.getMovies().remove(movie);
                directorService.save(pastDirector);
            }
            if (dni.getName().isBlank()) {
                movie.setDirector(null);
            }
            else {
                newDirector.getMovies().add(movie);
                movie.setDirector(newDirector);
                directorService.save(newDirector);
            }
        }

        BookNameId bni = movieDTO.getSourceBook();
        if (bni != null) {
            if (bni.getName().isBlank()) {movie.setSourceBook(null);}
            else {
                Book pastBook = bookService.findById(bni.getId());
                if (pastBook != null) {
                    pastBook.setMovieChildId(null);
                    bookService.save(pastBook);
                }

                Book newBook = bookService.findByName(bni.getName());

                newBook.setMovieChildId(movie);
                movie.setSourceBook(newBook);
                bookService.save(newBook);
            }
        }

        movieService.save(movie);

        return "redirect:/all/movies";
    }

    @GetMapping("basket")
    public String getBasket(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        UserDTO userDTO = userService.findByEmail(userDetails.getUsername());
        model.addAttribute("basket", userService.getBasketById(userDTO.getId()));
        return "basket";
    }

    @GetMapping("addToBasket")
    public String addMovie(@ModelAttribute("movie") MovieDTO movieDTO, @AuthenticationPrincipal UserDetails userDetails) {
        UserDTO userDTO = userService.findByEmail(userDetails.getUsername());
        User user = userService.findByIdUser(userDTO.getId());

        Movie movie = movieService.findById(movieDTO.getId());
        movie.getOwners().add(user);

        System.out.println(movieDTO.getName());
        System.out.println(userDTO.getName());
        Set<Movie> basket = userService.getBasketById(user.getId());
        basket.add(movie);

        registerService.save(user);
        movieService.save(movie);

        return "redirect:/all/movies";
    }

    @ExceptionHandler
    public ResponseEntity<String> handlerInvalidIdInput(NotFoundException er) {
        return new ResponseEntity<>(er.getMessage() + " <a href='/'>back</a>", HttpStatus.NOT_FOUND);
    }

//    @ExceptionHandler
//    public ResponseEntity<String> handlerHibernateEx(HibernateException he) {
//        return new ResponseEntity<>(" <a href='/'>1 book - 1 movie, back to main page</a>", HttpStatus.CONFLICT);
//    }
}
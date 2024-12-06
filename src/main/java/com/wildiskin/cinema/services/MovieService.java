package com.wildiskin.cinema.services;

import com.wildiskin.cinema.models.Book;
import com.wildiskin.cinema.models.Movie;
import com.wildiskin.cinema.repositories.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class MovieService {

    private final MovieRepository movieRepository;
    private final DirectorService directorService;
    private final BookService bookService;

    @Autowired
    public MovieService(MovieRepository movieRepository, DirectorService directorService, BookService bookService) {
        this.movieRepository = movieRepository;
        this.directorService = directorService;
        this.bookService = bookService;
    }

    public List<Movie> findAll() {
        return movieRepository.findAll();
    }

    public Movie findById(int id) {
        return movieRepository.findById(id).orElse(null);
    }

    @Transactional
    public void save(Movie movie) {
        movieRepository.save(movie);
//        if (Optional.of(movie.getSourceBook()).isPresent()) {
//            Book book = new Book();
//            book.setName(movie.getSourceBook());
//            bookService.save();
//        }
//        if (Optional.of(movie.getDirector()).isPresent()) {
//            directorService.save(movie.getDirector());
//        }
    }

    @Transactional
    public void update(int id, Movie editedMovie) {
        editedMovie.setId(id);
        movieRepository.save(editedMovie);
    }

    @Transactional
    public void delete(int id) {
        movieRepository.deleteById(id);
    }
}

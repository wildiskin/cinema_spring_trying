//package com.wildiskin.cinema.services;
//
//import com.wildiskin.cinema.DTO.MovieDTO;
//import com.wildiskin.cinema.models.Book;
//import com.wildiskin.cinema.models.Director;
//import com.wildiskin.cinema.models.Movie;
//import com.wildiskin.cinema.repositories.MovieRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//@Service
//@Transactional(readOnly = true)
//public class MovieService {
//
//    private final MovieRepository movieRepository;
//
//    @Autowired
//    public MovieService(MovieRepository movieRepository, DirectorService directorService, BookService bookService) {
//        this.movieRepository = movieRepository;
//    }
//
//    public List<Movie> findAll() {
//        return movieRepository.findAll();
//    }
//
//    public Movie findById(int id) {
//        return movieRepository.findById(id).orElse(null);
//    }
//
//    @Transactional
//    public void save(MovieDTO movieDTO) {
//        Movie movie = new Movie(movieDTO.getName(), movieDTO.getYear(), movieDTO.getDescription());
//        movieRepository.save(movie);
//    }
//
//    @Transactional
//    public void update(int id, Movie editedMovie) {
//        editedMovie.setId(id);
//        movieRepository.save(editedMovie);
//    }
//
//    @Transactional
//    public void delete(int id) {
//        movieRepository.deleteById(id);
//    }
//
//}

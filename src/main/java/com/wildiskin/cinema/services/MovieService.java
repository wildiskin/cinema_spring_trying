package com.wildiskin.cinema.services;

import com.wildiskin.cinema.DTO.MovieDTO;
import com.wildiskin.cinema.models.Book;
import com.wildiskin.cinema.models.Director;
import com.wildiskin.cinema.models.Movie;
import com.wildiskin.cinema.repositories.BookRepository;
import com.wildiskin.cinema.repositories.DirectorRepository;
import com.wildiskin.cinema.repositories.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class MovieService {

    private final MovieRepository movieRepository;
    private final DirectorService directorService;
    private final BookService bookService;

    public MovieService(MovieRepository movieRepository, DirectorService directorService, BookService bookService) {
        this.movieRepository = movieRepository;
        this.directorService = directorService;
        this.bookService = bookService;
    }

    public List<Movie> findAll() {
        return movieRepository.findAll();
    }

    public List<MovieDTO> findAllDto() {
        List<Movie> list = movieRepository.findAll();
        List<MovieDTO> listDto = new ArrayList<>(list.size());
        for (Movie m : list) {
            MovieDTO movieDTO = new MovieDTO(m.getId(), m.getName(), m.getYear(), m.getDescription());
            listDto.add(movieDTO);
        }

        return listDto;

    }

    public Movie findById(long id) {
        return movieRepository.findById(id);
    }

    @Transactional
    public void save(MovieDTO movieDTO) {
        Movie movie = new Movie(movieDTO.getName(), movieDTO.getYear(), movieDTO.getDescription());

        if (!movieDTO.getDirector().equals(null)) {  //director handling

            Director director = getExistOrNewDirectorByName(movieDTO.getDirector());
            director.getMovies().add(movie);
            movie.setDirector(director);
            directorService.update(director);
        }

        if (!movieDTO.getSourceBook().equals(null)) {

            Book book = getExistOrNewBookByName(movieDTO.getSourceBook());
            book.setMovieChildId(movie);
            movie.setSourceBook(book);

        }

        movieRepository.save(movie);
    }

    @Transactional
    public void update(long id, MovieDTO editedMovie) {
        Movie movie = movieRepository.findById(id);
        movie.setName(editedMovie.getName());
        movie.setYear(editedMovie.getYear());
        movie.setDescription(editedMovie.getDescription());

    }

    @Transactional
    public void delete(long id) {
        movieRepository.deleteById(id);
    }

    private Director getExistOrNewDirectorByName(String name) {
        Director director = directorService.findByName(name);
        if (director == null) {
            director = new Director(name);
//            directorService.save(director);
        }
        return director;
    }

    private Book getExistOrNewBookByName(String name) {
        Book book = bookService.findByName(name);
        if (book == null) {
            book = new Book(name);
//            bookService.save(book);
        }
        return book;
    }


    public List<MovieDTO> findAllMoviesByDirectorName(String directorName) {
        List<Movie> list = movieRepository.findAllMoviesByDirectorName(directorName);
        List<MovieDTO> listDto = new ArrayList<>(list.size());
        for (Movie m : list) {
            MovieDTO movie = new MovieDTO(m.getId(), m.getName(), m.getYear(), m.getDescription());
            movie.setDirector(m.getDirector().getName());
            Book book = m.getSourceBook();
            if (book != null) {movie.setSourceBook(book.getName());}
            listDto.add(movie);
        }
        return listDto;
    }
}

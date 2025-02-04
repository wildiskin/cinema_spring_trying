package com.wildiskin.cinema.services;

import com.wildiskin.cinema.DTO.MovieDTO;
import com.wildiskin.cinema.models.Book;
import com.wildiskin.cinema.models.Director;
import com.wildiskin.cinema.models.Movie;
import com.wildiskin.cinema.repositories.BookRepository;
import com.wildiskin.cinema.repositories.DirectorRepository;
import com.wildiskin.cinema.repositories.MovieRepository;
import com.wildiskin.cinema.util.BookNameId;
import com.wildiskin.cinema.util.DirectorNameId;
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
            MovieDTO movieDTO = new MovieDTO(m.getId(), m.getName());

//            String directorName = m.getDirector().getName() == null ? "unknown director" : m.getDirector().getName();
//
//            String bookName = m.getSourceBook().getName() == null ? "" : m.getSourceBook().getName();
//            movieDTO.setDirector(new DirectorNameId(directorName));
//            movieDTO.setSourceBook(new BookNameId(bookName));
            listDto.add(movieDTO);
        }

        return listDto;

    }

    public Movie findByName(String name) {
        return movieRepository.findByName(name);
    }

    public Movie findById(long id) {
        return movieRepository.findById(id);
    }

    @Transactional
    public void save(MovieDTO movieDTO) {
        Movie movie = new Movie(movieDTO.getName(), movieDTO.getYear(), movieDTO.getDescription());

        if (movieDTO.getDirector().getName() != null && !movieDTO.getDirector().getName().isBlank()) {  //director handling

            boolean alsoExist;
            Director director;
            String directorName = movieDTO.getDirector().getName();
            if (directorService.findByName(directorName) == null) {
                director = new Director(directorName);
                alsoExist = false;
            }
            else {
                director = directorService.findByName(directorName);
                alsoExist = true;
            }

            director.getMovies().add(movie);
            movie.setDirector(director);
            if (!alsoExist)
                    directorService.save(director);
        }

        if (movieDTO.getSourceBook().getName() != null && !movieDTO.getSourceBook().getName().isBlank()) {  //book handle
            boolean alsoExist;
            Book book;
            String bookName = movieDTO.getSourceBook().getName();
            if (bookService.findByName(bookName) == null) {
                alsoExist = false;
                book = new Book(bookName);
            }
            else {
                book = bookService.findByName(bookName);
                alsoExist = true;
            }
            book.setMovieChildId(movie);
            movie.setSourceBook(book);
//            if (!alsoExist) {
                bookService.save(book);
//            }
        }
        movieRepository.save(movie);
    }

    @Transactional
    public Movie save(Movie movie) {
        return movieRepository.save(movie);
    }



    @Transactional
    public void delete(long id) {
        movieRepository.deleteById(id);
    }

    public List<MovieDTO> findAllMoviesByDirectorName(String directorName) {
        List<Movie> list = movieRepository.findAllMoviesByDirectorName(directorName);
        List<MovieDTO> listDto = new ArrayList<>(list.size());
        for (Movie movie : list) {
            MovieDTO mdto = new MovieDTO(movie.getId(), movie.getName(), movie.getYear(), movie.getDescription());

            Director director = movie.getDirector();
            DirectorNameId dni = new DirectorNameId(director.getId(), director.getName());
            mdto.setDirector(dni);

            Book book = movie.getSourceBook();
            if (book != null) {
                BookNameId bni = new BookNameId(book.getId(), book.getName());
                mdto.setSourceBook(bni);
            }

            listDto.add(mdto);
        }
        return listDto;
    }
}

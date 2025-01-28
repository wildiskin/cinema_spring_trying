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
            String directorName = m.getDirector() == null ? "unknown director" : m.getDirector().getName();
            String bookName = m.getSourceBook() == null ? "" : m.getSourceBook().getName();
            movieDTO.setDirector(directorName);
            movieDTO.setSourceBook(bookName);
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

        if (!(movieDTO.getDirector() == null)) {  //director handling

            boolean alsoExist;
            Director director;
            String directorName = movieDTO.getDirector();
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

        if (movieDTO.getSourceBook() != null) {  //book handle
            boolean alsoExist;
            Book book;
            String bookName = movieDTO.getSourceBook();
            if (bookService.findByName(bookName) == null) {
                System.out.println("There is no book with name: " + bookName);
                alsoExist = false;
                book = new Book(bookName);
            }
            else {
                for (int i = 0; i < 5; i++) {System.out.println(bookName + "_________________________________________________________________");}
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

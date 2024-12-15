package com.wildiskin.cinema.services;

import com.wildiskin.cinema.DTO.BookDTO;
import com.wildiskin.cinema.DTO.DirectorDTO;
import com.wildiskin.cinema.DTO.DtoI;
import com.wildiskin.cinema.DTO.MovieDTO;
import com.wildiskin.cinema.models.Book;
import com.wildiskin.cinema.models.Director;
import com.wildiskin.cinema.models.Movie;
import com.wildiskin.cinema.repositories.BookRepository;
import com.wildiskin.cinema.repositories.DirectorRepository;
import com.wildiskin.cinema.repositories.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
//@Transactional
public class MainService {
    public BookRepository bookRepository;
    public MovieRepository movieRepository;
    public DirectorRepository directorRepository;

    @Autowired
    public MainService(BookRepository bookRepository, MovieRepository movieRepository, DirectorRepository directorRepository) {
        this.bookRepository = bookRepository;
        this.movieRepository = movieRepository;
        this.directorRepository = directorRepository;
    }

    public void save(MovieDTO movieDTO) {
        Movie superPosMovie = movieRepository.findByName(movieDTO.getName());
        if (superPosMovie == null) {
            superPosMovie = new Movie(movieDTO.getName(), movieDTO.getYear(), movieDTO.getDescription());


            if (movieDTO.getDirector() != null && !movieDTO.getDirector().isEmpty()) {  //director block
                Director director = directorRepository.findByName(movieDTO.getDirector());
                if (director != null) {
                    superPosMovie.setDirector(director);
                }
                else {
                    director = new Director(movieDTO.getDirector());
                    superPosMovie.setDirector(director);
                }
                directorRepository.save(director);
            }

            if (movieDTO.getSourceBook() != null && !movieDTO.getSourceBook().isEmpty()) { //book block
                Book book = bookRepository.findByName(movieDTO.getSourceBook());
                if (book != null) {
                    superPosMovie.setSourceBook(book);
                }
                else {
                    book = new Book(movieDTO.getSourceBook());
                    superPosMovie.setSourceBook(book);
                }
                bookRepository.save(book);
            }
            movieRepository.save(superPosMovie);
        }
    }

    public void save(DirectorDTO directorDTO) {
        Director superPosDirector = directorRepository.findByName(directorDTO.getName());
        if (superPosDirector == null) {
            superPosDirector = new Director(directorDTO.getName());
            if (directorDTO.getOwnMovies() != null) {if (!directorDTO.getOwnMovies().isEmpty()) {
                List<String> moviesNames = directorDTO.getOwnMovies();
                List<Movie> movies = new ArrayList<>(moviesNames.size());
                for (String m : moviesNames) {
                    Movie movie = movieRepository.findByName(m);
                    if (movie != null) {movies.add(movie);}
                    else {
                        movie = new Movie();
                        movie.setName(m);
                        movies.add(movie);
                    }
                }
            }}
            directorRepository.save(superPosDirector);
        }
    }

    public void save(BookDTO bookDTO) {
        Book superPosBook = bookRepository.findByName(bookDTO.getName());
        if (superPosBook == null) {
            superPosBook = new Book(bookDTO.getName());
            if (bookDTO.getAuthor() != null) {superPosBook.setAuthor(bookDTO.getAuthor());}
            if (bookDTO.getGenre() != null) {superPosBook.setGenre(bookDTO.getGenre());}
        bookRepository.save(superPosBook);
        }
    }

    public void delete(MovieDTO movie) {
        movieRepository.deleteByName(movie.getName());
    }

    public void delete(BookDTO book) {
        bookRepository.deleteByName(book.getName());
    }

    public void delete(DirectorDTO director) {
        directorRepository.deleteByName(director.getName());
    }

//    @Transactional(readOnly = true)
    public List<MovieDTO> findAllMovies() {
        List<Movie> list = movieRepository.findAll();
        List<MovieDTO> listDto = new ArrayList<>(list.size());
        for (Movie m : list) {
            MovieDTO movieDTO = new MovieDTO(m.getName(), m.getYear(), m.getDescription());

            Director director = m.getDirector();
            Book book = m.getSourceBook();
            if (director != null) {movieDTO.setDirector(director.getName());}
            if (book != null) {movieDTO.setSourceBook(book.getName());}

            listDto.add(movieDTO);
        }
        return listDto;
    }

//    @Transactional(readOnly = true)
    public List<BookDTO> findAllBooks() {
        List<Book> list = bookRepository.findAll();
        List<BookDTO> listDto = new ArrayList<>(list.size());
        for (Book b : list) {
            BookDTO book = new BookDTO(b.getName(), b.getGenre(), b.getAuthor());
            listDto.add(book);
        }
        return listDto;
    }

//    @Transactional(readOnly = true)
    public List<DirectorDTO> findAllDirectors() {
        List<Director> list = directorRepository.findAll();
        List<DirectorDTO> listDto = new ArrayList<>(list.size());
        for (Director d : list) {
            List<String> movies = d.getMovies().stream().map(x -> x.getName()).collect(Collectors.toList());
            DirectorDTO director = new DirectorDTO(d.getName(), movies);
            listDto.add(director);
        }
        return listDto;
    }

}

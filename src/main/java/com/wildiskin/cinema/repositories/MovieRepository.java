package com.wildiskin.cinema.repositories;

import com.wildiskin.cinema.DTO.MovieDTO;
import com.wildiskin.cinema.models.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository  extends JpaRepository<Movie, Integer> {
    public List<Movie> findAll();

    @Query(value = "SELECT m FROM Movie m WHERE LOWER(m.name) = LOWER(?1)")
    public Movie findByName(String name);

    public Movie findById(long id);

    public void deleteByName(String name);

    public void deleteById(long id);

    @Query(value = "SELECT m FROM Movie m WHERE m.director.name = ?1")
    public List<Movie> findAllMoviesByDirectorName(String directorName);
}

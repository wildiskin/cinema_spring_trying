package com.wildiskin.cinema.repositories;

import com.wildiskin.cinema.models.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository  extends JpaRepository<Movie, Integer> {
    public List<Movie> findAll();

    public Movie findByName(String name);

    public Movie findById(long id);

    public void deleteByName(String name);

    public void deleteById(long id);
}

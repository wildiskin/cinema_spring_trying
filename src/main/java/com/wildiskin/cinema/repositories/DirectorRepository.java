package com.wildiskin.cinema.repositories;

import com.wildiskin.cinema.models.Director;
import com.wildiskin.cinema.models.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DirectorRepository extends JpaRepository<Director, Integer> {
    @Query(value = "SELECT d FROM Director d WHERE LOWER(d.name) = LOWER(?1)")
    public Director findByName(String name);

    public void deleteByName(String name);
    public Director findById(long id);
}

package com.wildiskin.cinema.repositories;

import com.wildiskin.cinema.models.Director;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DirectorRepository extends JpaRepository<Director, Integer> {
    public Director findByName(String Name);
    public void deleteByName(String name);
}

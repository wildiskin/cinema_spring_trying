package com.wildiskin.cinema.repositories;

import com.wildiskin.cinema.models.Movie;
import com.wildiskin.cinema.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Integer> {
    @Query(value = "SELECT u FROM User u WHERE LOWER(u.name) = LOWER(?1)")
    public User findByName(String name);
}

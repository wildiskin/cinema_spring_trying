package com.wildiskin.cinema.repositories;

import com.wildiskin.cinema.models.Movie;
import com.wildiskin.cinema.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface UserRepository extends JpaRepository<User, Integer> {
    @Query(value = "SELECT u FROM User u WHERE LOWER(u.name) = LOWER(?1)")
    public User findByName(String name);

    @Query(value = "SELECT u FROM User u WHERE LOWER(u.email) = LOWER(?1)")
    public User findByEmail(String email);

    @Query(value = "SELECT u.basket FROM User u WHERE u.id = ?1")
    public Set<Movie> getBasket(long id);
}

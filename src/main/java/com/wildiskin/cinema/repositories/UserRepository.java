package com.wildiskin.cinema.repositories;

import com.wildiskin.cinema.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByName(String username);
}

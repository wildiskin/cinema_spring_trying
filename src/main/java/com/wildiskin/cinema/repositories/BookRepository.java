package com.wildiskin.cinema.repositories;

import com.wildiskin.cinema.models.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {
    public Book findByName(String name);
    public void deleteByName(String name);

    Book findById(long id);
}


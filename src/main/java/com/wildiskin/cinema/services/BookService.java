package com.wildiskin.cinema.services;

import com.wildiskin.cinema.models.Book;
import com.wildiskin.cinema.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class BookService {

    private final BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public Book findById(int id) {
        return bookRepository.findById(id).orElse(null);
    }

    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @Transactional
    public void save(Book book) {
        bookRepository.save(book);
    }

    @Transactional
    public void update(int id, Book editedBook) {
        editedBook.setId(id);
        bookRepository.save(editedBook);
    }

    @Transactional
    public void delete(int id) {
        bookRepository.deleteById(id);
    }
}

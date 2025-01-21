package com.wildiskin.cinema.services;

import com.wildiskin.cinema.DTO.BookDTO;
import com.wildiskin.cinema.models.Book;
import com.wildiskin.cinema.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookService {
    private final BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public void save(Book book) {
        bookRepository.save(book);
    }

    public void save(BookDTO bookDTO) {
        Book book = new Book(bookDTO.getName());
        bookRepository.save(book);
    }

    public Book findByName(String name) {
        return bookRepository.findByName(name);
    }

    public Book findById(long id) {
        return bookRepository.findById(id);
    }

    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    public List<BookDTO> findAllDto() {
        List<Book> bookList = bookRepository.findAll();
        List<BookDTO> list = new ArrayList<>(bookList.size());
        for (Book b : bookList) {
            BookDTO bookDTO = new BookDTO(b.getId(), b.getName(), b.getGenre(), b.getAuthor());
            list.add(bookDTO);
        }
        return list;
    }
}

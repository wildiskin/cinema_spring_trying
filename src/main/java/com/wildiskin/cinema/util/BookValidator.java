package com.wildiskin.cinema.util;

import com.wildiskin.cinema.DTO.BookDTO;
import com.wildiskin.cinema.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class BookValidator implements Validator {
    private final BookRepository bookRepository;

    @Autowired
    public BookValidator(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }


    @Override
    public boolean supports(Class<?> clazz) {
        return BookDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        BookDTO book = (BookDTO) target;
        if (bookRepository.findByName(book.getName()) != null) {
            errors.rejectValue("name", "", "Такая книга уже есть");
        }
    }
}

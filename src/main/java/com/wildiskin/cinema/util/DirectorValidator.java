package com.wildiskin.cinema.util;

import com.wildiskin.cinema.DTO.DirectorDTO;
import com.wildiskin.cinema.models.Director;
import com.wildiskin.cinema.repositories.DirectorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class DirectorValidator implements Validator {
    private final DirectorRepository directorRepository;

    @Autowired
    public DirectorValidator(DirectorRepository directorRepository) {
        this.directorRepository = directorRepository;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return DirectorDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        DirectorDTO director = (DirectorDTO) target;
        if (directorRepository.findByName(director.getName()) != null) {
            errors.rejectValue("name", "", "there is already such a director");
        }
    }

}

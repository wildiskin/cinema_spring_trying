package com.wildiskin.cinema.util;

import com.wildiskin.cinema.DTO.UserDTO;
import com.wildiskin.cinema.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class UserValidatorLog implements Validator {

    private final UserRepository userRepository;

    @Autowired
    public UserValidatorLog(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return UserDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserDTO userDTO = (UserDTO) target;
        if (userRepository.findByName(userDTO.getName()) == null) {
            errors.rejectValue("name", "", "There are not account with this username");
        }
    }
}
package com.wildiskin.cinema.util;

import com.wildiskin.cinema.DTO.UserDTO;
import com.wildiskin.cinema.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class UserValidatorReg implements Validator {

    private final UserRepository userRepository;

    @Autowired
    public UserValidatorReg(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return UserDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserDTO userDTO = (UserDTO) target;
        if (userRepository.findByEmail(userDTO.getEmail()) != null) {
            errors.rejectValue("email", "", "account with this email also exist");
        }
    }
}

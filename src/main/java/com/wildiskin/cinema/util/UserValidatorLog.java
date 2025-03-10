package com.wildiskin.cinema.util;

import com.wildiskin.cinema.DTO.UserDto;
import com.wildiskin.cinema.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class UserValidatorLog implements Validator {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserValidatorLog(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return UserDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserDto userDTO = (UserDto) target;
        if (userService.findByEmail(userDTO.getEmail()) == null) {
            errors.rejectValue("username", "", "There is no account with this email");
        }
        UserDto user = userService.findByEmail(userDTO.getEmail());
        String password = passwordEncoder.encode(userDTO.getPassword());
        if (!user.getPassword().equals(password)) {
            errors.rejectValue("password", "", "Wrong password, try again");
        }
    }
}
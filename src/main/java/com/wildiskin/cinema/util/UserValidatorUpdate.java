package com.wildiskin.cinema.util;

import com.wildiskin.cinema.DTO.UserDTO;
import com.wildiskin.cinema.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class UserValidatorUpdate implements Validator {

    private final UserService userService;

    @Autowired
    public UserValidatorUpdate(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return UserDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserDTO userDTO = (UserDTO) target;

        String newUsername = userDTO.getUsername();
        String oldUsername = userService.findById( (int) userDTO.getId()).getUsername();
        boolean isUsernameTaken;

        try {
            UserDTO userDTO1 = userService.findByEmail(newUsername);
            isUsernameTaken = true;
        }
        catch (UserNotFoundException e) {
            isUsernameTaken = false;
        }

        if (isUsernameTaken && !newUsername.equals(oldUsername)) {
            errors.rejectValue("username", "", "Email: " + newUsername + " is already taken");
        }

        String newPhone = userDTO.getPhoneNumber();
        String oldPhone = userService.findById( (int) userDTO.getId()).getPhoneNumber();
        boolean isPhoneTaken;

        try {
            UserDTO userDTO1 = userService.findByPhoneNumber(newPhone);
            isPhoneTaken = true;
        }
        catch (UserNotFoundException e) {
            isPhoneTaken = false;
        }

        if (isPhoneTaken && !newPhone.equals(oldPhone)) {
            errors.rejectValue("phoneNumber", "Phone number: " + newPhone + " is already taken");
        }
    }
}

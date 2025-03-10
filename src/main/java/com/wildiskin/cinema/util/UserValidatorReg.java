package com.wildiskin.cinema.util;

import com.wildiskin.cinema.DTO.UserDto;
import com.wildiskin.cinema.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class UserValidatorReg implements Validator {

    private final UserRepository userRepository;

    @Autowired
    public UserValidatorReg(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return UserDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserDto userDto = (UserDto) target;
        if (userRepository.findByEmail(userDto.getEmail()) != null) {
            errors.rejectValue("email", "", "account with this email also exist");
        }
        String phone = userDto.getPhoneNumber();
        Pattern phonePattern = Pattern.compile("\\+?[0-9]{11}");
        if (!phone.isBlank()) {
            Matcher matcher = phonePattern.matcher(phone);
            if (!matcher.matches()) {
                errors.rejectValue("phoneNumber", "", "Invalid phone number format");
            }
        }
    }
}

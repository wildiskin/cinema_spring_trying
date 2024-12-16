package com.wildiskin.cinema.controllers;

import com.wildiskin.cinema.DTO.UserDTO;
import com.wildiskin.cinema.services.RegisterService;
import com.wildiskin.cinema.util.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
//@RequestMapping("/reg")
public class RegisterController {

    private final RegisterService registerService;
    private final UserValidator userValidator;

    @Autowired
    public RegisterController(RegisterService registerService, UserValidator userValidator) {
        this.registerService = registerService;
        this.userValidator = userValidator;
    }

    @GetMapping("/registration")
    public String registration(@ModelAttribute("user") UserDTO userDTO) {
        return "auth/registration";
    }

    @PostMapping("/registration")
    public String registration(@ModelAttribute("user") UserDTO userDTO, BindingResult bindingResult) {
        userValidator.validate(userDTO, bindingResult);

        if (bindingResult.hasErrors()) {
            return "auth/registration";
        }
        registerService.save(userDTO);

        return "redirect:/";
    }
}

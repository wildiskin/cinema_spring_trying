package com.wildiskin.cinema.controllers;

import com.wildiskin.cinema.DTO.UserDTO;
import com.wildiskin.cinema.services.RegisterService;
import com.wildiskin.cinema.util.UserValidatorLog;
import com.wildiskin.cinema.util.UserValidatorReg;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final RegisterService registerService;
    private final UserValidatorReg userValidatorReg;
    private final UserValidatorLog userValidatorLog;

    @Autowired
    public AuthController(RegisterService registerService, UserValidatorReg userValidatorReg, UserValidatorLog userValidatorLog) {
        this.registerService = registerService;
        this.userValidatorReg = userValidatorReg;
        this.userValidatorLog = userValidatorLog;
    }

    @GetMapping("/registration")
    public String registration(@ModelAttribute("user") UserDTO userDTO) {
        return "auth/registration";
    }

    @PostMapping("/registration")
    public String registration(@ModelAttribute("user") @Valid UserDTO userDTO, BindingResult bindingResult) {
        userValidatorReg.validate(userDTO, bindingResult);

        if (bindingResult.hasErrors()) {
            return "auth/registration";
        }
        registerService.save(userDTO);

        return "redirect:/";
    }

    @GetMapping("/login")
    public String login(@ModelAttribute("user") UserDTO userDTO) {
        return "auth/login";
    }

//    @PostMapping("/login")
//    public String login(@ModelAttribute("user") UserDTO userDTO, BindingResult bindingResult) {
//        userValidatorLog.validate(userDTO, bindingResult);
//
//        if (bindingResult.hasErrors()) {
//            return "auth/login";
//        }
//
//        return "redirect:/";
//    }
}

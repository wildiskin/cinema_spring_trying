package com.wildiskin.cinema.controllers;

import com.wildiskin.cinema.DTO.UserDto;
import com.wildiskin.cinema.services.MailService;
import com.wildiskin.cinema.services.RegisterService;
import com.wildiskin.cinema.util.CodeGenerator;
import com.wildiskin.cinema.util.StringWrapper;
import com.wildiskin.cinema.util.UserValidatorReg;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import java.io.IOException;

@Controller
@RequestMapping("/auth")
@SessionAttributes({"user", "secretCode"})
public class AuthController {

    private final RegisterService registerService;
    private final UserValidatorReg userValidatorReg;
    private final MailService mailService;


    @Autowired
    public AuthController(RegisterService registerService, UserValidatorReg userValidatorReg, MailService mailService) {
        this.registerService = registerService;
        this.userValidatorReg = userValidatorReg;
        this.mailService = mailService;
    }

    @ModelAttribute("user")
    public UserDto createUser() {
        return new UserDto();
    }

    @GetMapping("/registration")
    public String registration(@ModelAttribute("user") UserDto userDTO) {
        return "auth/registration";
    }

    @PostMapping("/registration")
    public String verification(@ModelAttribute("user") @Valid UserDto userDTO, BindingResult bindingResult, Model model) throws IOException, InterruptedException {

        userValidatorReg.validate(userDTO, bindingResult);

        if (bindingResult.hasErrors()) {
            return "/auth/registration";
        }

        String secretCode = CodeGenerator.generate();
        mailService.sendMessage(userDTO.getEmail(), secretCode);

        model.addAttribute("secretCode", secretCode);
        model.addAttribute("user", userDTO);

        return "redirect:/auth/approve";
    }

    @GetMapping("/approve")
    public String approveGet(@ModelAttribute("inputCode") StringWrapper inputCode) {
        return "auth/approveEmail";
    }

    @PostMapping("/approve")
    public String approve(@ModelAttribute("inputCode") StringWrapper inputCode, BindingResult br,
                          @ModelAttribute("user") UserDto user,
                          @ModelAttribute("secretCode") String secretCode, BindingResult bindingResult,
                          SessionStatus sessionStatus) {

        if (inputCode.getValue().equals(secretCode)) {
            registerService.save(user);
            sessionStatus.setComplete();
            return "redirect:/auth/login";
        }

        br.rejectValue("value", "", "wrong code, try again");

        return "auth/approveEmail";
    }

    @GetMapping("/login")
    public String login(@ModelAttribute("user") UserDto userDTO) {
        return "auth/login";
    }

//    @PostMapping("/registration")
//    public String registration(@ModelAttribute("user") @Valid UserDTO userDTO, BindingResult bindingResult) {
//        userValidatorReg.validate(userDTO, bindingResult);
//
//        if (bindingResult.hasErrors()) {
//            return "auth/registration";
//        }
//        registerService.save(userDTO);
//
//        return "redirect:/";
//    }


//    @PostMapping("/processLogin")
//    public String loginPost(@ModelAttribute("user") UserDTO userDTO, BindingResult bindingResult) {
//        System.out.println("i workin biiiitchju");
//        System.out.println("i workin biiiitchju");
//        System.out.println("i workin biiiitchju");
//        System.out.println("i workin biiiitchju");
//        userValidatorLog.validate(userDTO, bindingResult);
//
//        if (bindingResult.hasErrors()) {
//            return "auth/login";
//        }
//
//        return "redirect:/";
//    }




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


//public String loginUser(HttpServletRequest request, RedirectAttributes redirectAttributes) {
//    ...
//    redirectAttributes.addFlashAttribute("user", user2);
//    return "redirect:/book/books";
//}
//
//public String books(@ModelAttribute User user, HttpServletRequest request, Model model) {
//    ...
//}

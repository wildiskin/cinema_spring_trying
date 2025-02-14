package com.wildiskin.cinema.controllers;

import com.wildiskin.cinema.DTO.UserDTO;
import com.wildiskin.cinema.services.MailService;
import com.wildiskin.cinema.services.RegisterService;
import com.wildiskin.cinema.services.UserService;
import com.wildiskin.cinema.util.CodeGenerator;
import com.wildiskin.cinema.util.StringWrapper;
import com.wildiskin.cinema.util.UserValidatorLog;
import com.wildiskin.cinema.util.UserValidatorReg;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Map;

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
    public UserDTO createUser() {
        return new UserDTO();
    }

    @GetMapping("/registration")
    public String registration(@ModelAttribute("user") UserDTO userDTO) {
        return "auth/registration";
    }

    @PostMapping("/registration")
    public String verification(@ModelAttribute("user") @Valid UserDTO userDTO, BindingResult bindingResult, Model model) {

        userValidatorReg.validate(userDTO, bindingResult);

        if (bindingResult.hasErrors()) {
            return "/auth/registration";
        }

        String secretCode = CodeGenerator.generate();
        mailService.sendMessage(userDTO.getUsername(), secretCode);

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
                          @ModelAttribute("user") UserDTO user,
                          @ModelAttribute("secretCode") String secretCode, BindingResult bindingResult,
                          SessionStatus sessionStatus) {

        if (inputCode.getValue().equals(secretCode)) {
            registerService.save(user);
            sessionStatus.setComplete();
            return "redirect:/login";
        }

        br.rejectValue("value", "", "wrong code, try again");

        return "auth/approveEmail";
    }

    @GetMapping("/login")
    public String login(@ModelAttribute("user") UserDTO userDTO) {
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

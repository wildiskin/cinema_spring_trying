package com.wildiskin.cinema.controllers;

import com.wildiskin.cinema.DTO.UserDTO;
import com.wildiskin.cinema.services.MailService;
import com.wildiskin.cinema.services.RegisterService;
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
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final RegisterService registerService;
    private final UserValidatorReg userValidatorReg;
    private final MailService mailService;
    private final RedirectAttributes ra = new RedirectAttributesModelMap();

    @Autowired
    public AuthController(RegisterService registerService, UserValidatorReg userValidatorReg, MailService mailService) {
        this.registerService = registerService;
        this.userValidatorReg = userValidatorReg;
        this.mailService = mailService;
    }

    @GetMapping("/registration")
    public String registration(@ModelAttribute("user") UserDTO userDTO) {
        return "auth/registration";
    }

    @PostMapping("/email")
    public String verification(@ModelAttribute("user") @Valid UserDTO userDTO, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        mailService.sendMessage(userDTO.getUsername(), "7777");

        redirectAttributes.addFlashAttribute("inputCode", "1234");
        redirectAttributes.addFlashAttribute("user", userDTO);
        redirectAttributes.addFlashAttribute("code", 7777);

        return "redirect:/auth/approve";
    }

    @GetMapping("/approve")
    public String approveGet(@ModelAttribute("inputCode") String inputCode, RedirectAttributes redirectAttributes, ModelMap modelMap) {
        redirectAttributes.addFlashAttribute("user", modelMap.getAttribute("user"));
        return "auth/approveEmail";
    }

    @PostMapping("/approve")
    public String approve(@RequestParam String inputCode, BindingResult bindingResult, UserDTO userDTO, String code, RedirectAttributes redirectAttributes) {

        if (inputCode == code) {
            redirectAttributes.addFlashAttribute("user", userDTO);
            return "redirect:/auth/registration";
        }

        bindingResult.rejectValue("inputCode", "", "wrong code,try again");

        return "auth/approveEmail";
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

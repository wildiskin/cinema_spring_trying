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
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Map;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final RegisterService registerService;
    private final UserValidatorReg userValidatorReg;
    private final MailService mailService;
    private final RedirectAttributes ra;
    private final UserService userService;

    @Autowired
    public AuthController(RegisterService registerService, UserValidatorReg userValidatorReg, MailService mailService, RedirectAttributes ra, UserService userService) {
        this.registerService = registerService;
        this.userValidatorReg = userValidatorReg;
        this.mailService = mailService;
        this.ra = ra;
        this.userService = userService;
    }

    @GetMapping("/registration")
    public String registration(@ModelAttribute("user") UserDTO userDTO) {
        return "auth/registration";
    }

    @PostMapping("/registration")
    public String verification(@ModelAttribute("user") @Valid UserDTO userDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        userValidatorReg.validate(userDTO, bindingResult);

        if (bindingResult.hasErrors()) {
            return "/auth/registration";
        }

        String secretCode = CodeGenerator.generate();
        StringWrapper inputCode = new StringWrapper("string");
        UserDTO user = new UserDTO(userDTO.getId(), userDTO.getUsername(), userDTO.getName(), userDTO.getPassword(), userDTO.getRole());
        mailService.sendMessage(userDTO.getUsername(), secretCode);

        redirectAttributes.addFlashAttribute("inputCode", inputCode);
        redirectAttributes.addFlashAttribute("user", user);
        redirectAttributes.addFlashAttribute("secretCode", secretCode);

        return "redirect:/auth/approve";
    }

    @GetMapping("/approve")
    public String approveGet(@ModelAttribute("inputCode") StringWrapper inputCode, RedirectAttributes redirectAttributes) {

        Map<String, ?> attrs = redirectAttributes.getFlashAttributes();
        UserDTO user = (UserDTO) attrs.get("user");
        String secretCode = (String) attrs.get("secretCode");


        redirectAttributes.addFlashAttribute("user", user);
        redirectAttributes.addFlashAttribute("secretCode", secretCode);
        System.out.println(user.getPassword());
        System.out.println(user.getUsername());
        System.out.println(user.getName());
        System.out.println(inputCode.getString());
        System.out.println(secretCode);
        return "auth/approveEmail";
    }

    @PostMapping("/approve")
    public String approve(@ModelAttribute("inputCode") StringWrapper inputCode, BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        Map<String, ?> attrs = redirectAttributes.getFlashAttributes();
        String secret = (String) attrs.get("secretCode");

        if (secret.equals(inputCode.getString())) {
            UserDTO user = (UserDTO) attrs.get("user");
            registerService.save(user);
            return "redirect:/login";
        }

        bindingResult.rejectValue("inputCode", "", "wrong code, try again");

        return "auth/approveEmail";
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

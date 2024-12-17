package com.wildiskin.cinema.controllers;

import com.wildiskin.cinema.DTO.MovieDTO;
import com.wildiskin.cinema.DTO.UserDTO;
import com.wildiskin.cinema.models.Movie;
import com.wildiskin.cinema.services.MainService;
import com.wildiskin.cinema.services.RegisterService;
import com.wildiskin.cinema.services.UserService;
import com.wildiskin.cinema.util.UserErrorResponse;
import com.wildiskin.cinema.util.UserNotCreatedException;
import com.wildiskin.cinema.util.UserNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api")
public class AdminController {
    private final MainService mainService;
    private final RegisterService registerService;
    private final UserService userService;

    @Autowired
    public AdminController(MainService mainService, RegisterService registerService, UserService userService) {
        this.mainService = mainService;
        this.registerService = registerService;
        this.userService = userService;
    }

    @GetMapping("/showAllMovies")
    public List<MovieDTO> showMovies() {
        return mainService.findAllMovies();
    }

    @GetMapping("/{id}")
    public UserDTO showById(@PathVariable("id") int id) {
        return userService.findById(id);
    }

    @PostMapping("addUser")
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid UserDTO userDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder errorMsg = new StringBuilder();
            List<String> errors = bindingResult.getFieldErrors().stream().map(e -> e.getField() + " - " + e.getDefaultMessage() + ";").collect(Collectors.toList());
            for (String text : errors) {errorMsg.append(text);}

            throw new UserNotCreatedException(errorMsg.toString());
        }

        registerService.save(userDTO);

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @ExceptionHandler
    private ResponseEntity<UserErrorResponse> exceptionHandler(UserNotFoundException e) {
        UserErrorResponse response = new UserErrorResponse(
                "User with this id wasn't found",
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    private ResponseEntity<UserErrorResponse> exceptionHandler(UserNotCreatedException e) {
        UserErrorResponse response = new UserErrorResponse(
                e.getMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
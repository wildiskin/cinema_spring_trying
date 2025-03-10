package com.wildiskin.cinema.controllers;

import com.wildiskin.cinema.DTO.MovieDTO;
import com.wildiskin.cinema.DTO.UserDto;
import com.wildiskin.cinema.services.MovieService;
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
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class AdminController {
    private final MovieService movieService;
    private final RegisterService registerService;
    private final UserService userService;

    @Autowired
    public AdminController(MovieService movieService, RegisterService registerService, UserService userService) {
        this.movieService = movieService;
        this.registerService = registerService;
        this.userService = userService;
    }

    @GetMapping("/showAllMovies")
    public List<MovieDTO> showMovies() {
        return movieService.findAllDto();
    }

    @GetMapping("/showAllUsers")
    public List<UserDto> showUsers() {
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public UserDto showById(@PathVariable("id") int id) {
        return userService.findById(id);
    }

    @PostMapping("/addUser")
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid UserDto userDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder errorMsg = new StringBuilder();
            List<String> errors = bindingResult.getFieldErrors().stream().map(e -> e.getField() + " - " + e.getDefaultMessage() + ";").collect(Collectors.toList());
            for (String text : errors) {errorMsg.append(text);}

            throw new UserNotCreatedException(errorMsg.toString());
        }

        registerService.save(userDTO);

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/updateUser")
    public ResponseEntity<HttpStatus> update(@RequestBody @Valid UserDto userDTO, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            StringBuilder errorMsg = new StringBuilder();
            List<String> errors = bindingResult.getFieldErrors().stream().map(e -> e.getField() + " - " + e.getDefaultMessage() + ";").collect(Collectors.toList());
            for (String text : errors) {errorMsg.append(text);}

            throw new UserNotCreatedException(errorMsg.toString());
        }

        registerService.update(userDTO);

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/deleteUser/{id}")
    public HttpEntity<HttpStatus> delete(@PathVariable("id") long id) {
        UserDto user = userService.findById((int) id);
        if (user == null)
            throw new UserNotFoundException("User with this id wasn't found");
        else {
            userService.deleteById(id);
            return ResponseEntity.ok(HttpStatus.OK);
        }
    }

    @GetMapping("showAllDirectorsFilmsBy/{name}")
    public List<MovieDTO> show(@PathVariable("name") String directorName) {
        return movieService.findAllMoviesByDirectorName(directorName);
    }

    @GetMapping("showAllDirectorsFilmsByDirector")
    public List<MovieDTO> showByParams(String director) {
        return movieService.findAllMoviesByDirectorName(director);
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

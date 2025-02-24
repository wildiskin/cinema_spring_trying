package com.wildiskin.cinema.services;

import com.wildiskin.cinema.DTO.UserDTO;
import com.wildiskin.cinema.models.User;
import com.wildiskin.cinema.repositories.UserRepository;
import com.wildiskin.cinema.util.Roles;
import com.wildiskin.cinema.util.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RegisterService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public RegisterService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void save(UserDTO userDTO) {
        String password = passwordEncoder.encode(userDTO.getPassword());
        User user = new User(userDTO.getName(), password, userDTO.getUsername());

        user.setRole(userDTO.getRole());

        userRepository.save(user);
    }

    public void save(User user) {
        userRepository.save(user);
    }

    public void update(UserDTO userDTO) {
        long id = userDTO.getId();
        Optional<User> user = userRepository.findById((int) id);
        if (!user.isPresent())
            throw new UserNotFoundException("User with this id: " + id + " isn't exist");
        else {
            User u = user.get();
            u.setName(userDTO.getName());
            u.setRole(userDTO.getRole());
            u.setPassword(passwordEncoder.encode(userDTO.getPassword()));
            u.setId(id);
            u.setEmail(userDTO.getUsername());
            userRepository.save(u);
        }
    }
}

package com.wildiskin.cinema.services;

import com.wildiskin.cinema.DTO.UserDTO;
import com.wildiskin.cinema.models.User;
import com.wildiskin.cinema.repositories.UserRepository;
import com.wildiskin.cinema.util.Roles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
        User user = new User(userDTO.getName(), password);

        if (userDTO.getRole() != null) {
            for (Roles role : Roles.values()) {
                if (role.name().equalsIgnoreCase(userDTO.getRole())) {
                    user.setRole(role.name());
                }
            }
        }
        else {
            user.setRole(Roles.ROLE_USER.name());
        }
        userRepository.save(user);
    }
}

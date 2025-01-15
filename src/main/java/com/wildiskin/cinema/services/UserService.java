package com.wildiskin.cinema.services;

import com.wildiskin.cinema.DTO.UserDTO;
import com.wildiskin.cinema.models.User;
import com.wildiskin.cinema.repositories.UserRepository;
//import com.wildiskin.cinema.security.UserDetailsImpl;
import com.wildiskin.cinema.security.UserDetailsImpl;
import com.wildiskin.cinema.util.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByName(username);

        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        return new UserDetailsImpl(user);
    }

    public UserDTO findById(int id) {
        Optional<User> superPosUser = userRepository.findById(id);
        if (!superPosUser.isPresent()) {
            throw new UserNotFoundException("There is not user with this username in app");
        }
        User user = superPosUser.get();
        return new UserDTO(user.getName(), user.getPassword(), user.getRole());
    }

    public List<UserDTO> findAll() {
        List<User> users = userRepository.findAll();
        List<UserDTO> usersDTO = new ArrayList<>(users.size());
        for (User u : users) {
            usersDTO.add(new UserDTO(u.getName(), u.getPassword(), u.getRole()));
        }
        return usersDTO;
    }
}

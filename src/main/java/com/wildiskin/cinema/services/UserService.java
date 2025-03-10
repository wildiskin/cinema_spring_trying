package com.wildiskin.cinema.services;

import com.wildiskin.cinema.DTO.UserDto;
import com.wildiskin.cinema.models.Movie;
import com.wildiskin.cinema.models.User;
import com.wildiskin.cinema.repositories.UserRepository;

import com.wildiskin.cinema.security.UserDetailsImpl;
import com.wildiskin.cinema.util.CustomSet;
import com.wildiskin.cinema.util.UserNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.*;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        return new UserDetailsImpl(user);
    }

    @Transactional
    public UserDto findByEmail(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UserNotFoundException("There is not user with this username in app");
        }
        UserDto result = new UserDto(user.getEmail(), user.getName(), user.getPassword());
        result.setId(user.getId());
        result.setRole(user.getRole());
        result.setBasket(CustomSet.newBySet(user.getBasket()));
        result.setPhoneNumber(user.getPhoneNumber());

        return result;
    }

    public User findUserByEmail(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UserNotFoundException("There is not user with this username in app");
        }
        return user;
    }


    public UserDto findById(int id) {
        Optional<User> superPosUser = userRepository.findById(id);
        if (!superPosUser.isPresent()) {
            throw new UserNotFoundException("There is not user with this username in app");
        }
        User user = superPosUser.get();

        UserDto result = new UserDto(user.getEmail(), user.getName(), user.getPassword());
        result.setId(user.getId());
        result.setRole(user.getRole());
        result.setBasket(CustomSet.newBySet(user.getBasket()));

        return result;
    }

    public User findByIdUser(long id) {
        Optional<User> superPosUser = userRepository.findById((int) id);
        if (!superPosUser.isPresent()) {
            throw new UserNotFoundException("There is not user with this username in app");
        }
        return superPosUser.get();
    }

    public Set<Movie> getBasketById(long id) {
        return userRepository.getBasket(id);
    }

    public void deleteById(long id) {
        userRepository.deleteById((int) id);
    }

    public List<UserDto> findAll() {
        List<User> users = userRepository.findAll();
        List<UserDto> usersDto = new ArrayList<>(users.size());
        for (User u : users) {
            UserDto one = new UserDto(u.getEmail(), u.getName(), u.getPassword());
            one.setId(u.getId());
            one.setRole(u.getRole());
            one.setBasket(CustomSet.newBySet(u.getBasket()));
            usersDto.add(one);
        }
        return usersDto;
    }

    public UserDto findByPhoneNumber(String phoneNumber) {

        int len = phoneNumber.length();
        if

        phoneNumber.subSequence(0, len);

        User user = userRepository.findByPhoneNumber(phoneNumber);

        if (user == null) {
            throw new UserNotFoundException("There is not user with this username in app");
        }

        UserDto result = new UserDto(user.getEmail(), user.getName(), user.getPassword());
        result.setId(user.getId());
        result.setRole(user.getRole());
        result.setBasket(CustomSet.newBySet(user.getBasket()));
        result.setPhoneNumber(user.getPhoneNumber());

        return result;
    }
}

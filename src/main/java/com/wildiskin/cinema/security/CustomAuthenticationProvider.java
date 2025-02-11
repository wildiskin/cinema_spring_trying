package com.wildiskin.cinema.security;

import com.wildiskin.cinema.DTO.UserDTO;
import com.wildiskin.cinema.models.User;
import com.wildiskin.cinema.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public CustomAuthenticationProvider(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication auth) throws AuthenticationException {

        String userName = auth.getName();
        String password =auth.getCredentials().toString();

        UserDTO userDTO = userService.findByEmail(userName);

        if (userDTO == null) {throw new BadCredentialsException("User with username: \"" + userName + "\" not found");}
        if (!passwordEncoder.matches(password, userDTO.getPassword())) {throw new BadCredentialsException("Wrong password");}

        User user = new User(userDTO.getName(), userDTO.getPassword(), userDTO.getUsername());
        user.setRole(userDTO.getRole());
        user.setId(userDTO.getId());

        UserDetails principal = new UserDetailsImpl(user);

        return new UsernamePasswordAuthenticationToken(principal, password, principal.getAuthorities());

    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}

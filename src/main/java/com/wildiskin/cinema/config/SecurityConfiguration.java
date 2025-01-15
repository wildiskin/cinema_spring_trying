package com.wildiskin.cinema.config;

import com.wildiskin.cinema.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration {

    private final UserService userService;

    @Autowired
    public SecurityConfiguration(UserService userService) {
        this.userService = userService;
    }

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(
                        (auth) -> auth
                                .requestMatchers("/api/showAllMovies", "api/addUser", "api/{id}", "/api/showAllUsers").hasAnyAuthority("ROLE_ADMIN", "ROLE_MODERATOR")
                                .requestMatchers("/auth/registration", "/auth/login").permitAll()
                                .anyRequest().authenticated()
                )
                .formLogin(
                        (auth) -> auth
                                .loginPage("/auth/login")
                                .loginProcessingUrl("/process_login")
                                .defaultSuccessUrl("/")
                                .failureUrl("/auth/login?error")

                )
                .logout(
                        (auth) -> auth
                                .logoutUrl("/logout")
                                .logoutSuccessUrl("/auth/login")
                )
                .csrf((auth) -> auth.ignoringRequestMatchers("api/addUser"))
                .httpBasic(Customizer.withDefaults());
        return http.build();
    }

    // Настраиваем аутентификацию
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService)
                .passwordEncoder(getPasswordEncoder());
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

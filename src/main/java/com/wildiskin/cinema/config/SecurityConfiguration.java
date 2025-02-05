package com.wildiskin.cinema.config;

import com.wildiskin.cinema.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Properties;

@Configuration
@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration { //implements WebMvcConfigurer

    private final UserService userService;

//    @Value("${app.storage.path}")
//    private String appPath;
    @Value("${mail.sender.address}")
    private String username;

    @Value("${mail.sender.password}")
    private String password;

    @Value("${smtp.google.domain}")
    private String domain;

    private int port = 25;

    @Autowired
    public SecurityConfiguration(UserService userService) {
        this.userService = userService;
    }

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(
                        (auth) -> auth
                                .requestMatchers("/api/showAllMovies", "api/addUser", "api/{id}", "/api/showAllUsers", "/api/updateUser", "/api/deleteUser/", "/api/showAllDirectorsFilmsBy/", "/api/showAllDirectorsFilmsByDirector").hasAnyAuthority("ROLE_ADMIN", "ROLE_MODERATOR")
                                .requestMatchers("/auth/registration", "/auth/login", "/css/styles.css").permitAll()
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
                .csrf((auth) -> auth.ignoringRequestMatchers("api/addUser", "api/updateUser"))
                .httpBasic(Customizer.withDefaults());
        return http.build();
    }

//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        WebMvcConfigurer.super.addResourceHandlers(registry);
//        registry
//                .addResourceHandler("resources/**")
//                .addResourceLocations("file:" + appPath + "/src/main/resources/static");
//    }

    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService)
                .passwordEncoder(getPasswordEncoder());
    }


    public void configure(WebSecurity web) throws Exception {
        web
                .ignoring()
                .requestMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/images/**","/vendor/**","/fonts/**");
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder(BCryptPasswordEncoder.BCryptVersion.$2A);
    }

    @Bean
    public JavaMailSender getMailSender() {
        JavaMailSenderImpl sender = new JavaMailSenderImpl();
        sender.setPort(port);
        sender.setPassword(password);
        sender.setUsername(username);
        sender.setHost(domain);
        Properties properties = sender.getJavaMailProperties();
        return sender;
    }
}

package com.wildiskin.cinema.DTO;

import com.wildiskin.cinema.util.Roles;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public class UserDTO {

    private long id;

    @NotNull(message = "username is required")
    @Length(min = 2, max = 50)
    private String name;

    @NotNull(message = "password is required")
    @Length(min = 8, max = 50)
    private String password;

    @NotNull
    @Email
    private String username;



    private String role;

    public String getRole() {
        return role;
    }

    public void setRole(@NotNull String role) {
        this.role = role;
    }

    public UserDTO() {
    }

    public UserDTO(long id,  String username, String name, String password, String role) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.role = role;
        this.username = username;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public @NotNull @Email String getUsername() {
        return username;
    }

    public void setUsername(@NotNull @Email String username) {
        this.username = username;
    }
}
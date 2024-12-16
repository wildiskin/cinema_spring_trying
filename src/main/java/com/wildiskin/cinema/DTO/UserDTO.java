package com.wildiskin.cinema.DTO;

import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public class UserDTO {

    @NotNull(message = "username is required")
    @Length(min = 2, max = 50)
    private String name;

    @NotNull(message = "password is required")
    @Length(min = 8, max = 50)
    private String password;

    public UserDTO(String name, String password) {
        this.name = name;
        this.password = password;
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
}

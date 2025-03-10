package com.wildiskin.cinema.DTO;

import com.wildiskin.cinema.models.Movie;
import com.wildiskin.cinema.util.CustomSet;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.HashSet;
import java.util.Set;

public class UserDto {

    private long id;

    @NotNull(message = "username is required")
    @Size(min = 2, max = 50)
    private String name;

    @NotNull(message = "password is required")
    @Size(min = 8, max = 50)
    private String password;

    @NotNull
    @Email
    private String email;

    private String phoneNumber;

    private CustomSet<Movie> basket = new CustomSet<>();

    private CustomSet<Movie> collectionOfMovies = new CustomSet<>();

    private String role;

    private String photoLink;

    public String getRole() {
        return role;
    }

    public void setRole(@NotNull String role) {
        this.role = role;
    }

    public UserDto() {
    }

    public UserDto(String email, String name, String password) {
        this.name = name;
        this.password = password;
        this.email = email;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public @NotNull @Email String getEmail() {
        return email;
    }

    public void setEmail(@NotNull @Email String email) {
        this.email = email;
    }

    public String getPhotoLink() {
        return photoLink;
    }

    public void setPhotoLink(String photoLink) {
        this.photoLink = photoLink;
    }

    public CustomSet<Movie> getBasket() {
        return basket;
    }

    public void setBasket(CustomSet<Movie> basket) {
        this.basket = basket;
    }

    public CustomSet<Movie> getCollectionOfMovies() {
        return collectionOfMovies;
    }

    public void setCollectionOfMovies(CustomSet<Movie> collectionOfMovies) {
        this.collectionOfMovies = collectionOfMovies;
    }
}
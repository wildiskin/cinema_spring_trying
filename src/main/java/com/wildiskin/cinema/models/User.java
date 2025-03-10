package com.wildiskin.cinema.models;

import com.wildiskin.cinema.util.Roles;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "password")
    private String password;

    @Column(name = "email")
    private String email;

    private String photoLink;

    @Column(name = "role")
    private String role;

    @Column(name="phone_number")
    private String phoneNumber;

    @Column(name="is_phone_number_approved")
    private boolean isPhoneNumberApproved;

    @ManyToMany
    @JoinTable(name = "basket",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "movie_id", referencedColumnName = "id"))
    private Set<Movie> basket = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "collection_of_movies",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "movie_id", referencedColumnName = "id"))
    private Set<Movie> collectionOfMovies = new HashSet<>();

    public User() {}

    private User(UserBuilder builder) {
        this.name = builder.name;
        this.password = builder.password;
        this.email = builder.email;
        this.isPhoneNumberApproved = builder.isPhoneNumberApproved;
        this.photoLink = builder.photoLink;
        this.phoneNumber = builder.phoneNumber;
        this.setRole(null);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhotoLink() {
        return photoLink;
    }

    public void setPhotoLink(String photoLink) {
        this.photoLink = photoLink;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        if (role != null) {
            role = role.toUpperCase();
            role = role.startsWith("ROLE_") ? role : "ROLE_" + role;
            for (Roles r : Roles.values()) {
                if (r.name().equalsIgnoreCase(role)) {
                    this.role = r.name();
                    return;
                }
            }
        }
        this.role = "ROLE_USER";
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean isPhoneNumberApproved() {
        return isPhoneNumberApproved;
    }

    public void setPhoneNumberApproved(boolean phoneNumberApproved) {
        isPhoneNumberApproved = phoneNumberApproved;
    }

    public Set<Movie> getBasket() {
        return basket;
    }

    public void setBasket(Set<Movie> basket) {
        this.basket = basket;
    }

    public Set<Movie> getCollectionOfMovies() {
        return collectionOfMovies;
    }

    public void setCollectionOfMovies(Set<Movie> collectionOfMovies) {
        this.collectionOfMovies = collectionOfMovies;
    }

    public static class UserBuilder {

        private String name;

        private String email;

        private String password;

        private boolean isPhoneNumberApproved;

        private String phoneNumber;

        private String photoLink;

        public UserBuilder(String name, String email, String password) {
            this.name = name;
            this.email = email;
            this.password = password;
            this.isPhoneNumberApproved = false;
        }

        public UserBuilder setIsPhoneNumberApproved(boolean bool) {
            this.isPhoneNumberApproved = bool;
            return this;
        }

        public UserBuilder setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
            return this;
        }

        public UserBuilder setPhotoLink(String photoLink) {
            this.photoLink = photoLink;
            return this;
        }

        public User build() {
            return new User(this);
        }

    }
}

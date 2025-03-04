package com.wildiskin.cinema.models;

import com.wildiskin.cinema.util.Basket;
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

    @Column(name = "role")
    private String role;

    @Column(name="phone_number")
    private String phoneNumber;

    @Column(name="is_number_approved")
    private boolean isNumberApproved;

    @ManyToMany
    @JoinTable(name = "basket",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "movie_id", referencedColumnName = "id"))
    private Set<Movie> basket = new HashSet<>();

    public User() {}

    public User(String name, String password, String email) {
        this.name = name;
        this.password = password;
        this.email = email;
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

    public Set<Movie> getBasket() {
        return basket;
    }

    public void setBasket(Set<Movie> basket) {
        this.basket = basket;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean isNumberApproved() {
        return isNumberApproved;
    }

    public void setNumberApproved(boolean numberApproved) {
        isNumberApproved = numberApproved;
    }
}

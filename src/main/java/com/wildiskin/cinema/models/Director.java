package com.wildiskin.cinema.models;


import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name="director")
public class Director {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="name")
    private String name;


    @OneToMany(mappedBy = "director")
    private List<Movie> movies;

    @Override
    public String toString() {
        return "Director{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", movies=" + movies +
                '}';
    }

    public Director() {
    }

    public Director(String name) {this.name = name;}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Movie> getMovies() {
        return movies;
    }

    public void setMovies(List<Movie> movie) {
        this.movies = movie;
    }
}

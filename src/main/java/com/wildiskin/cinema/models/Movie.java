package com.wildiskin.cinema.models;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "movie")
public class Movie {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private long price;

    @Column(name="name")
    private String name;

    @ManyToOne
    @JoinColumn(name="director_id", referencedColumnName = "id")
    private Director director;

    @Column(name="year")
    private Integer year;

    @Column(name = "description")
    private String description;

    @OneToOne
    @JoinColumn(name = "book_id", referencedColumnName = "id")
    private Book sourceBook;

    @ManyToMany
    @JoinTable(name = "basket",
            joinColumns = @JoinColumn(name = "movie_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"))
    private Set<User> potentialBuyers = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "collection_of_movies",
            joinColumns = @JoinColumn(name = "movie_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"))
    private Set<User> owners;

    public Movie() {}

    public Movie(String name, int year, String description) {
        this.name = name;
        this.year = year;
        this.description = description;
    }

    public Set<User> getPotentialBuyers() {
        return potentialBuyers;
    }

    public void setPotentialBuyers(Set<User> owners) {
        this.potentialBuyers = owners;
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

    public Director getDirector() {
        return director;
    }

    public void setDirector(Director director) {
        this.director = director;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Book getSourceBook() {
        return sourceBook;
    }

    public void setSourceBook(Book sourceBook) {
        this.sourceBook = sourceBook;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public Set<User> getOwners() {
        return owners;
    }

    public void setOwners(Set<User> owners) {
        this.owners = owners;
    }
}


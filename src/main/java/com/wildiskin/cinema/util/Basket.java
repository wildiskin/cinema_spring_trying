package com.wildiskin.cinema.util;

import com.wildiskin.cinema.models.Movie;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class Basket<M> extends HashSet<M> {

    public Basket() {
    }

    public Basket(Collection<? extends M> c) {
        super(c);
    }

    public boolean containsById(long id) {
        for (Object object : this.stream().toArray()) {
            Movie movie = (Movie) object;
            if (movie.getId() == id) {return true;}
        }
        return false;
    }
}

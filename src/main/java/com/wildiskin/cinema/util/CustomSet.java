package com.wildiskin.cinema.util;

import com.wildiskin.cinema.models.Movie;

import java.util.HashSet;
import java.util.Set;

public class CustomSet<M> extends HashSet<M> {

    public CustomSet() {
        super();
    }

    public static CustomSet newBySet(Set set) {
        CustomSet result = new CustomSet();
        result.addAll(set);
        return result;
    }

    public boolean containsById(long id) {
        for (Object m : this.stream().toArray()) {
            if (((Movie) m).getId() == id) {return true;}
        }
        return false;
    }
}

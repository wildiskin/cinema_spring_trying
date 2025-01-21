package com.wildiskin.cinema.services;

import com.wildiskin.cinema.DTO.DirectorDTO;
import com.wildiskin.cinema.models.Director;
import com.wildiskin.cinema.models.Movie;
import com.wildiskin.cinema.repositories.DirectorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DirectorService {

    private final DirectorRepository directorRepository;

    @Autowired
    public DirectorService(DirectorRepository directorRepository) {
        this.directorRepository = directorRepository;
    }

    public void save(Director director) {
        directorRepository.save(director);
    }

    public void save(DirectorDTO directorDTO) {
        Director director = new Director(directorDTO.getName());
        directorRepository.save(director);
    }

    public void update(long id, DirectorDTO directorDTO) {
        Director director = directorRepository.findById(id);
        director.setName(directorDTO.getName());
    }

    public void update(Director director) {
        Director existDirector = directorRepository.findById(director.getId());
        existDirector.setName(director.getName());
        List<Movie> alsoMovies = existDirector.getMovies();
        List<Movie> newMovies = director.getMovies();
        for (Movie m : newMovies) {
            if (!alsoMovies.contains(m)) {alsoMovies.add(m);}
        }
    }

    public Director findByName(String name) {
        return directorRepository.findByName(name);
    }

    public List<Director> findAll() {
        return directorRepository.findAll();
    }

    public List<DirectorDTO> findAllDto() {
        List<Director> list = directorRepository.findAll();
        List<DirectorDTO> finalList = new ArrayList<>(list.size());
        for (Director d : list) {
            DirectorDTO directorDTO = new DirectorDTO(d.getId(), d.getName());
            List<Movie> movies = d.getMovies();
            List<String> directorMovies = movies.equals(null) ? new ArrayList<>() : movies.stream().map((x) -> x.getName()).toList();
            directorDTO.setMovies(directorMovies);
            finalList.add(directorDTO);
        }
        return finalList;
    }

    public Director findById(long l) {
        return directorRepository.findById(l);
    }
}

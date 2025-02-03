package com.wildiskin.cinema.services;

import com.wildiskin.cinema.DTO.DirectorDTO;
import com.wildiskin.cinema.models.Director;
import com.wildiskin.cinema.models.Movie;
import com.wildiskin.cinema.repositories.DirectorRepository;
import com.wildiskin.cinema.util.MovieNameId;
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

//    public void save(DirectorDTO directorDTO) {
//        Director director = new Director(directorDTO.getName());
//        directorRepository.save(director);
//    }

    public void update(long id, DirectorDTO directorDTO) {
        Director director = directorRepository.findById(id);
        director.setName(directorDTO.getName());
    }

    public Director findByName(String name) {
        return directorRepository.findByName(name);
    }

    public List<Director> findAll() {
        return directorRepository.findAll();
    }

    public List<DirectorDTO> findAllDto() {
        List<Director> list = directorRepository.findAll();
        List<DirectorDTO> finaList = new ArrayList<>(list.size());
        for (Director d : list) {
            DirectorDTO directorDTO = new DirectorDTO(d.getId(), d.getName());
            List<Movie> movies = d.getMovies();
            List<MovieNameId> directorMovies = movies.equals(null) ? new ArrayList<>() : movies.stream().map((x) -> new MovieNameId(x.getId(), x.getName())).toList();
            directorDTO.setMovies(directorMovies);
            finaList.add(directorDTO);
        }
        return finaList;
    }

    public Director findById(long l) {
        return directorRepository.findById(l);
    }

    public void delete(long id) {
        directorRepository.deleteById((int) id);
    }
}

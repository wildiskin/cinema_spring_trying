package com.wildiskin.cinema.services;

import com.wildiskin.cinema.models.Director;
import com.wildiskin.cinema.repositories.DirectorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class DirectorService {

    private final DirectorRepository directorRepository;

    @Autowired
    public DirectorService(DirectorRepository directorRepository) {
        this.directorRepository = directorRepository;
    }

    public Director findById(int id) {
        return directorRepository.findById(id).orElse(null);
    }

    public List<Director> findAll() {
        return directorRepository.findAll();
    }

    @Transactional
    public void save(Director director) {
        directorRepository.save(director);
    }

    @Transactional
    public void update(int id, Director editedDirector) {
        editedDirector.setId(id);
        directorRepository.save(editedDirector);
    }

    @Transactional
    public void delete(int id) {
        directorRepository.deleteById(id);
    }
}

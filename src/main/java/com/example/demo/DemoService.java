package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DemoService {
    private final DemoRepository demoRepository;

    @Autowired
    public DemoService(DemoRepository demoRepository) {
        this.demoRepository = demoRepository;
    }

    public List<Song> getAllSongs() {
        return demoRepository.getAllSongs();
    }

    public void addSong(Song newSong) {
        demoRepository.addSong(newSong);
    }

    public Song getSongById(Long id) {
        
        return demoRepository.getSongById(id);
    }

    public void deleteSongById(Long id) {
  
        demoRepository.deleteSongById(id);
    }
}

package com.lyriqu.music.service;

import com.lyriqu.music.entity.Music;
import com.lyriqu.music.repository.MusicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class MusicService {

    private final MusicRepository musicRepository;

    @Autowired
    public MusicService(MusicRepository repository) {
        this.musicRepository = repository;
    }

    public Optional<Music> findById(String id) {
        return this.musicRepository.findById(UUID.fromString(id));
    }

    public List<Music> findAllByUserId(String id) {
        return this.musicRepository.findAllByUserId(UUID.fromString(id));
    }

    public Music createOrUpdate(Music music) {
        return this.musicRepository.save(music);
    }
}

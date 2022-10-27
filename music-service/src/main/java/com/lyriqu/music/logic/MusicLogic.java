package com.lyriqu.music.logic;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lyriqu.music.entity.Category;
import com.lyriqu.music.entity.Music;
import com.lyriqu.music.enums.Status;
import com.lyriqu.music.models.AwsSongReturnDTO;
import com.lyriqu.music.models.SongDTO;
import com.lyriqu.music.rabbitmq.logic.SendLogic;
import com.lyriqu.music.rabbitmq.models.ReturnMessage;
import com.lyriqu.music.service.CategoryService;
import com.lyriqu.music.service.MusicService;
import com.lyriqu.music.service.StorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Slf4j
@Component
public class MusicLogic {

    private final MusicService musicService;
    private final CategoryService categoryService;
    private final SendLogic sendLogic;

    @Autowired
    public MusicLogic(MusicService musicService, SendLogic sendLogic, CategoryService categoryService) {
        this.musicService = musicService;
        this.sendLogic = sendLogic;
        this.categoryService = categoryService;
    }

    public Music getSong(String id) throws Exception {
        Optional<Music> music = this.musicService.findById(id);

        if(music.isPresent()) {
            return music.get();
        }

        throw new Exception("Song couldn't be retrieved");
    }

    public List<Music> getSongsByUser(String userId) {
        return this.musicService.findAllByUserId(userId);
    }

    public Music publishSong(SongDTO songDTO, String userId) throws Exception {
        ReturnMessage returnMessage = this.sendLogic.publishSongQueue(songDTO.getFile(), songDTO.getImage());

        if(returnMessage.getStatus()) {

            try {
                UUID musicId = songDTO.getMusicId();

                ReturnMessage monitorReturn = this.sendLogic.publishToMonitoring(musicId.toString(), userId);

                if(monitorReturn.getStatus()) {
                    Music music = new Music();
                    music.setId(musicId);
                    music.setName(songDTO.getName());

                    for (String cat : songDTO.getCategories()) {
                        Optional<Category> category = categoryService.getCategory(cat);
                        category.ifPresent(music::addCategory);
                    }

                    music.setActive(false);
                    music.setStatus(Status.WAITING);
                    music.setUserId(UUID.fromString(userId));

                    ObjectMapper objectMapper = new ObjectMapper();

                    AwsSongReturnDTO returnDto = objectMapper.readValue(returnMessage.getMessage(), AwsSongReturnDTO.class);

                    music.setThumbnail(returnDto.getImagePath());
                    music.setPath(returnDto.getSongPath());

                    return this.musicService.createOrUpdate(music);
                }

                throw new Exception("Could not publish song, please try again.");
            } catch (Exception e) {
                throw new Exception("Could not publish song, please try again.");
            }
        }

        throw new Exception("Could not publish song, please try again.");
    }

    public void saveMusic(Music music) {
        this.musicService.createOrUpdate(music);
    }
}

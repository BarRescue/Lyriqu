package com.lyriqu.music;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lyriqu.music.entity.Category;
import com.lyriqu.music.entity.Music;
import com.lyriqu.music.enums.Status;
import com.lyriqu.music.logic.MusicLogic;
import com.lyriqu.music.models.AwsSongReturnDTO;
import com.lyriqu.music.models.SongDTO;
import com.lyriqu.music.rabbitmq.logic.SendLogic;
import com.lyriqu.music.rabbitmq.models.ReturnMessage;
import com.lyriqu.music.service.CategoryService;
import com.lyriqu.music.service.MusicService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class MusicTests {

    @InjectMocks
    private MusicLogic musicLogic;

    @Mock
    private MusicService musicService;

    @Mock
    private SendLogic sendLogic;

    @Mock
    private CategoryService categoryService;

    ObjectMapper objectMapper;

    UUID musicId = UUID.fromString("123e4567-e89b-42d3-a456-556642440010");
    UUID userId = UUID.fromString("123e4567-e89b-42d3-a456-556642440030");
    UUID catId = UUID.fromString("123e4567-e89b-42d3-a456-556642440080");

    @BeforeEach
    void setUp() {
        this.objectMapper = new ObjectMapper();
    }

    @Test
    void getSongValidId() throws Exception {
        lenient().when(musicService.findById(musicId.toString())).thenReturn(Optional.of(this.getMusic()));

        Music music = musicLogic.getSong(musicId.toString());

        assertEquals("Army", music.getName());
    }

    @Test
    void getSongInvalidId() {
        lenient().when(musicService.findById(musicId.toString())).thenReturn(Optional.empty());

        assertThrows(Exception.class, () -> {
            musicLogic.getSong(musicId.toString());
        });
    }

    @Test
    void getSongsInvalidUserId() {
        lenient().when(musicService.findAllByUserId(userId.toString())).thenReturn(Collections.emptyList());
        assertEquals(0, musicLogic.getSongsByUser(userId.toString()).size());
    }

    @Test
    void getSongsValidUserId() {
        List<Music> songs = new ArrayList<>();
        songs.add(this.getMusic());

        lenient().when(musicService.findAllByUserId(userId.toString())).thenReturn(songs);

        assertEquals(1, musicLogic.getSongsByUser(userId.toString()).size());
    }

    @Test
    void publishSongValidData() throws Exception {
        SongDTO dto = this.getSongDTO();

        Music musicToReturn = this.getMusic();
        musicToReturn.setStatus(Status.WAITING);

        lenient().when(sendLogic.publishSongQueue(dto.getFile(), dto.getImage())).thenReturn(
                new ReturnMessage(true, objectMapper.writeValueAsString(this.getAwsReturn())));

        lenient().when(sendLogic.publishToMonitoring(musicId.toString(), userId.toString())).thenReturn(
                new ReturnMessage(true, "OK"));

        lenient().when(categoryService.getCategory(catId.toString())).thenReturn(Optional.of(new Category()));

        lenient().when(musicService.createOrUpdate(any(Music.class))).thenReturn(musicToReturn);

        assertEquals("WAITING", this.musicLogic.publishSong(dto, userId.toString()).getStatus().name());
    }

    @Test
    void publishSongInvalidData() throws Exception {
        SongDTO dto = this.getSongDTO();

        lenient().when(sendLogic.publishSongQueue(dto.getFile(), dto.getImage())).thenReturn(
                new ReturnMessage(false, "Invalid Data"));

        assertThrows(Exception.class, () -> {
            this.musicLogic.publishSong(dto, userId.toString());
        });
    }

    private Music getMusic() {
        Music song = new Music();
        song.setId(musicId);
        song.setName("Army");
        song.setActive(false);
        song.setStatus(Status.WAITING);
        song.setUserId(userId);
        song.setPath("test");
        song.setThumbnail("test");

        return song;
    }

    private AwsSongReturnDTO getAwsReturn() {
        AwsSongReturnDTO aws = new AwsSongReturnDTO();
        aws.setImagePath("test");
        aws.setSongPath("test");

        return aws;
    }

    private SongDTO getSongDTO() {
        SongDTO songDTO = new SongDTO();
        songDTO.setName("Army");
        songDTO.setMusicId(musicId);
        songDTO.setCategories(new String[]{catId.toString()});
        songDTO.setImage(new MockMultipartFile("image", "army.jpeg", "image/jpeg", "some other type".getBytes()));
        songDTO.setFile(new MockMultipartFile("Army", "army.mp3", "audio/mp3", "some other type".getBytes()));

        return songDTO;
    }
}
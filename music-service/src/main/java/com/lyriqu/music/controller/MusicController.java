package com.lyriqu.music.controller;

import com.amazonaws.services.mediastoredata.model.GetObjectResult;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.util.IOUtils;
import com.lyriqu.music.entity.Music;
import com.lyriqu.music.logic.MusicLogic;
import com.lyriqu.music.models.SongDTO;
import com.lyriqu.music.rabbitmq.logic.SendLogic;
import com.lyriqu.music.service.StorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.print.attribute.standard.Media;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;


@RestController
@RequestMapping("/music/song")
@Slf4j
public class MusicController {

    private StorageService storageService;
    private MusicLogic musicLogic;

    @Autowired
    public MusicController(StorageService storageService, MusicLogic musicLogic) {
        this.storageService = storageService;
        this.musicLogic = musicLogic;
    }

    @GetMapping(value = "/stream/{id}", produces = { MediaType.APPLICATION_OCTET_STREAM_VALUE })
    public ResponseEntity<?> downloadFile(@PathVariable String id) {
        try {
            Music music = musicLogic.getSong(id);

            if(music == null) {
                throw new Exception("Song couldn't be retrieved");
            }

            S3Object object = storageService.downloadFile(music.getPath());

            final StreamingResponseBody body = outputStream -> {
                int numberOfBytesToWrite = 0;
                byte[] data = new byte[1024];
                while ((numberOfBytesToWrite = object.getObjectContent().read(data, 0, data.length)) != -1) {
                    System.out.println("Writing some bytes..");
                    outputStream.write(data, 0, numberOfBytesToWrite);
                }
                object.getObjectContent().close();
            };

            return new ResponseEntity<>(body, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getMusicByUser(@AuthenticationPrincipal Jwt user) {
        try {
            return new ResponseEntity<>(this.musicLogic.getSongsByUser(user.getSubject()), HttpStatus.OK);
        } catch(Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping
    public ResponseEntity<?> publishMusic(@AuthenticationPrincipal Jwt user, SongDTO dto) {
        try {

            if(dto.getCategories() == null || dto.getFile() == null || dto.getImage() == null || dto.getName() == null) {
                throw new Exception("Invalid data");
            }

            return ResponseEntity.ok().body(this.musicLogic.publishSong(dto, user.getSubject()));
        } catch(Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
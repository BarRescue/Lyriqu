package com.lyriqu.music.models;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Data
public class SongDTO {

    private String name;
    private String[] categories;
    private MultipartFile file;
    private MultipartFile image;

    private UUID musicId = UUID.randomUUID();

}

package com.lyriqu.music.rabbitmq.models;

import lombok.Data;

@Data
public class MusicDTO {

    private String songPath;
    private String imagePath;
    private String songName;

    public MusicDTO(String songPath, String imagePath, String songName) {
        this.songName = songName;
        this.songPath = songPath;
        this.imagePath = imagePath;
    }
}

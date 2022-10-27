package com.lyriqu.music.rabbitmq.models;
import lombok.Data;

@Data
public class PublishSongDTO {

    private byte[] songFile;
    private byte[] imageFile;
    private String songName;
    private String imageName;

    public PublishSongDTO(byte[] songFile, byte[] imageFile, String imageName, String songName) {
        this.songFile = songFile;
        this.imageFile = imageFile;
        this.imageName = imageName;
        this.songName = songName;
    }
}
package com.lyriqu.storageservice.rabbitmq.models;

import lombok.Data;

@Data
public class SongDTO {

    private byte[] songFile;
    private String songName;
    private byte[] imageFile;
    private String imageName;

}

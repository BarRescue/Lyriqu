package com.lyriqu.storageservice.rabbitmq.models;

import lombok.Data;

@Data
public class AwsSongReturnDTO {

    private String songPath;
    private String imagePath;

    public AwsSongReturnDTO(String songPath, String imagePath) {
        this.songPath = songPath;
        this.imagePath = imagePath;
    }

}

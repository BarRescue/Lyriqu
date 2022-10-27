package com.lyriqu.music.rabbitmq.models;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class CreateCategoryDTO {

    private byte[] file;
    private String fileName;

    public CreateCategoryDTO(byte[] file, String fileName) {
        this.file = file;
        this.fileName = fileName;
    }
}

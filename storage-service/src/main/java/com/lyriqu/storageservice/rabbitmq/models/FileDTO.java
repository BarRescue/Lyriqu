package com.lyriqu.storageservice.rabbitmq.models;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class FileDTO {

    private byte[] file;
    private String fileName;

}

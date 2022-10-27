package com.lyriqu.storageservice.logic;

import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.util.IOUtils;
import com.lyriqu.storageservice.rabbitmq.models.AwsSongReturnDTO;
import com.lyriqu.storageservice.rabbitmq.models.ReturnMessage;
import com.lyriqu.storageservice.service.AwsService;
import com.rabbitmq.client.Return;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

@Component
@Slf4j
public class StorageLogic {

    private final AwsService awsService;

    @Autowired
    public StorageLogic(AwsService awsService) {
        this.awsService = awsService;
    }

    public String uploadFile(byte[] file, String fileName) {
        return this.awsService.uploadFile(file, fileName);
    }

    public AwsSongReturnDTO uploadSong(byte[] songFile, String songName, byte[] imageFile, String imageName) {
        String songPath = this.awsService.uploadFile(songFile, songName);
        String imagePath = this.awsService.uploadFile(imageFile, imageName);

        AwsSongReturnDTO dto = new AwsSongReturnDTO(songPath, imagePath);

        return dto;
    }
}

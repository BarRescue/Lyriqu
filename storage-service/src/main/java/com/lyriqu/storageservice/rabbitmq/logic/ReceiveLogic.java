package com.lyriqu.storageservice.rabbitmq.logic;

import com.amazonaws.services.s3.internal.Mimetypes;
import com.amazonaws.services.s3.model.S3Object;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lyriqu.storageservice.logic.StorageLogic;
import com.lyriqu.storageservice.rabbitmq.models.AwsSongReturnDTO;
import com.lyriqu.storageservice.rabbitmq.models.FileDTO;
import com.lyriqu.storageservice.rabbitmq.models.ReturnMessage;
import com.lyriqu.storageservice.rabbitmq.models.SongDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

@Service
@Slf4j
public class ReceiveLogic {

    private final StorageLogic storageLogic;
    private final ObjectMapper objectMapper;

    @Autowired
    public ReceiveLogic(StorageLogic storageLogic) {
        this.storageLogic = storageLogic;
        this.objectMapper = new ObjectMapper();
    }

    @RabbitListener(queues = {"uploadFile"})
    @Transactional
    public String uploadFileToStorage(String in) throws JsonProcessingException {
        try {
            FileDTO dto = objectMapper.readValue(in, FileDTO.class);

            String url = this.storageLogic.uploadFile(dto.getFile(), dto.getFileName());

            return objectMapper.writeValueAsString(new ReturnMessage(true, url));
        } catch (Exception e) {
            return objectMapper.writeValueAsString(new ReturnMessage(false, e.getMessage()));
        }
    }

    @RabbitListener(queues = {"publishSong"})
    @Transactional
    public String publishSongToStorage(String in) throws JsonProcessingException {
        try {
            SongDTO dto = objectMapper.readValue(in, SongDTO.class);

            AwsSongReturnDTO returnDTO = this.storageLogic.uploadSong(dto.getSongFile(), dto.getSongName(), dto.getImageFile(), dto.getImageName());

            String mappedDTO = objectMapper.writeValueAsString(returnDTO);

            return objectMapper.writeValueAsString(new ReturnMessage(true, mappedDTO));
        } catch (Exception e) {
            return objectMapper.writeValueAsString(new ReturnMessage(false, e.getMessage()));
        }
    }
}

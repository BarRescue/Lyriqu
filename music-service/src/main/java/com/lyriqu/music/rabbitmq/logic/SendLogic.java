package com.lyriqu.music.rabbitmq.logic;

import com.amazonaws.services.s3.model.S3Object;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lyriqu.music.rabbitmq.models.CreateCategoryDTO;
import com.lyriqu.music.rabbitmq.models.PublishSongDTO;
import com.lyriqu.music.rabbitmq.models.ReturnMessage;
import com.lyriqu.music.rabbitmq.models.publishMonitoringDTO;
import com.rabbitmq.client.Return;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@Service
public class SendLogic {

    private final RabbitTemplate template;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    public SendLogic(RabbitTemplate template) {
        this.template = template;
    }

    public ReturnMessage createCategoryQueue(MultipartFile file) throws IOException {

        String response = objectMapper.writeValueAsString(new CreateCategoryDTO(file.getBytes(), file.getOriginalFilename()));

        Message message = template.sendAndReceive("storage-exchange", "upload-storage", new Message(response.getBytes(), new MessageProperties()));

        byte[] body = message.getBody();

        return objectMapper.readValue(body, ReturnMessage.class);
    }

    public ReturnMessage publishSongQueue(MultipartFile songFile, MultipartFile imageFile) throws IOException {
        String response = objectMapper.writeValueAsString(new PublishSongDTO(songFile.getBytes(), imageFile.getBytes(), imageFile.getOriginalFilename(), songFile.getOriginalFilename()));

        Message message = template.sendAndReceive("storage-exchange", "publish-storage", new Message(response.getBytes(), new MessageProperties()));

        byte[] body = message.getBody();

        return objectMapper.readValue(body, ReturnMessage.class);
    }

    public ReturnMessage publishToMonitoring(String musicId, String userId) throws IOException {
        String response = objectMapper.writeValueAsString(new publishMonitoringDTO(musicId, userId));

        Message message = template.sendAndReceive("monitor-exchange", "new-publish", new Message(response.getBytes(), new MessageProperties()));

        byte[] body = message.getBody();

        return objectMapper.readValue(body, ReturnMessage.class);
    }
}

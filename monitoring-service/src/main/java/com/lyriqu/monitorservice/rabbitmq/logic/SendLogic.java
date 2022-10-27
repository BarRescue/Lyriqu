package com.lyriqu.monitorservice.rabbitmq.logic;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lyriqu.monitorservice.rabbitmq.models.ReturnMessage;
import com.lyriqu.monitorservice.rabbitmq.models.ReviewedDTO;
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

    public ReturnMessage getMusicInfo(String id) throws IOException {

        Message message = template.sendAndReceive("music-exchange", "music-info", new Message(id.getBytes(), new MessageProperties()));

        byte[] body = message.getBody();

        return objectMapper.readValue(body, ReturnMessage.class);
    }

    public ReturnMessage entryReviewed(ReviewedDTO dto) throws IOException {

        String response = objectMapper.writeValueAsString(dto);

        Message message = template.sendAndReceive("music-exchange", "publish-reviewed", new Message(response.getBytes(), new MessageProperties()));

        byte[] body = message.getBody();

        return objectMapper.readValue(body, ReturnMessage.class);
    }
}

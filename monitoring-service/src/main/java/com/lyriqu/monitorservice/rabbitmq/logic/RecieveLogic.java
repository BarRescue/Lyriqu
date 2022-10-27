package com.lyriqu.monitorservice.rabbitmq.logic;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lyriqu.monitorservice.entity.Monitor;
import com.lyriqu.monitorservice.logic.MonitorLogic;
import com.lyriqu.monitorservice.rabbitmq.models.NewEntryDTO;
import com.lyriqu.monitorservice.rabbitmq.models.ReturnMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RecieveLogic {

    private final MonitorLogic monitorLogic;
    private final ObjectMapper objectMapper;

    @Autowired
    public RecieveLogic(MonitorLogic monitorLogic) {
        this.monitorLogic = monitorLogic;
        this.objectMapper = new ObjectMapper();
    }

    @RabbitListener(queues = {"newPublish"})
    public String newPublishQueue(String in) throws Exception {
        try {
            NewEntryDTO dto = objectMapper.readValue(in, NewEntryDTO.class);

            this.monitorLogic.newEntry(dto);

            return objectMapper.writeValueAsString(new ReturnMessage(true, "OK"));
        } catch (Exception e) {
            return objectMapper.writeValueAsString(new ReturnMessage(false, e.getMessage()));
        }
    }
}

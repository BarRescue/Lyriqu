package com.lyriqu.music.rabbitmq.logic;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lyriqu.music.entity.Music;
import com.lyriqu.music.enums.Status;
import com.lyriqu.music.logic.MusicLogic;
import com.lyriqu.music.rabbitmq.models.MusicDTO;
import com.lyriqu.music.rabbitmq.models.ReturnMessage;
import com.lyriqu.music.rabbitmq.models.ReviewedDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ReceiveLogic {

    private final MusicLogic musicLogic;
    private final ObjectMapper objectMapper;

    @Autowired
    public ReceiveLogic(MusicLogic musicLogic) {
        this.musicLogic = musicLogic;
        this.objectMapper = new ObjectMapper();
    }

    @RabbitListener(queues = {"musicInfo"})
    public String getMusicInfo(String in) throws JsonProcessingException {
        try {
            Music music = this.musicLogic.getSong(in);
            MusicDTO dto = new MusicDTO(music.getPath(), music.getThumbnail(), music.getName());

            String mappedDTO = objectMapper.writeValueAsString(dto);

            return objectMapper.writeValueAsString(new ReturnMessage(true, mappedDTO));
        } catch(Exception e) {
            return objectMapper.writeValueAsString(new ReturnMessage(false, e.getMessage()));
        }
    }

    @RabbitListener(queues = {"publishReviewed"})
    public String entryReviewed(String in) throws JsonProcessingException {
        try {
            ReviewedDTO dto = objectMapper.readValue(in, ReviewedDTO.class);

            Music music = this.musicLogic.getSong(dto.getMusicId());

            music.setStatus(dto.getStatus());
            music.setReason(dto.getReason());

            if(dto.getStatus() == Status.SUCCESS) {
                music.setActive(true);
            }

            this.musicLogic.saveMusic(music);

            return objectMapper.writeValueAsString(new ReturnMessage(true, "OK"));
        } catch(Exception e) {
            return objectMapper.writeValueAsString(new ReturnMessage(false, e.getMessage()));
        }

    }

}

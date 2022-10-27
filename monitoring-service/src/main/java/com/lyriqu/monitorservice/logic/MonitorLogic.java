package com.lyriqu.monitorservice.logic;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lyriqu.monitorservice.entity.Monitor;
import com.lyriqu.monitorservice.enums.Status;
import com.lyriqu.monitorservice.rabbitmq.logic.SendLogic;
import com.lyriqu.monitorservice.rabbitmq.models.MusicDTO;
import com.lyriqu.monitorservice.rabbitmq.models.NewEntryDTO;
import com.lyriqu.monitorservice.rabbitmq.models.ReturnMessage;
import com.lyriqu.monitorservice.rabbitmq.models.ReviewedDTO;
import com.lyriqu.monitorservice.service.MonitorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
public class MonitorLogic {

    private final MonitorService monitorService;
    private final SendLogic sendLogic;

    @Autowired
    public MonitorLogic(MonitorService monitorService, SendLogic sendLogic) {
        this.monitorService = monitorService;
        this.sendLogic = sendLogic;
    }

    public void newEntry(NewEntryDTO dto) throws Exception {
        Monitor monitor = new Monitor();
        monitor.setCreated(new Date());
        monitor.setStatus(Status.WAITING);
        monitor.setMusicId(dto.getMusicId());
        monitor.setUserId(dto.getUserId());

        Monitor created = this.monitorService.createOrUpdate(monitor);

        if(created == null) {
            throw new Exception("Failed to add.");
        }
    }

    public List<Monitor> getAllWaitingEntries() {
        return this.monitorService.findAllByStatus(Status.WAITING);
    }

    public MusicDTO getMusicInfo(String id) throws Exception {
        Optional<Monitor> monitor = this.monitorService.findById(UUID.fromString(id));

        if(monitor.isPresent()) {
            ReturnMessage returnMessage = this.sendLogic.getMusicInfo(monitor.get().getMusicId());

            if(returnMessage.getStatus()) {
                ObjectMapper objectMapper = new ObjectMapper();
                return objectMapper.readValue(returnMessage.getMessage(), MusicDTO.class);
            }
        }

        throw new Exception("An unknown error occured.");
    }

    public Monitor approveEntry(String id) throws Exception {
        Optional<Monitor> monitor = this.monitorService.findById(UUID.fromString(id));

        if(monitor.isPresent()) {
            ReviewedDTO dto = new ReviewedDTO(monitor.get().getMusicId(), Status.SUCCESS, monitor.get().getReason());
            ReturnMessage returnMessage = this.sendLogic.entryReviewed(dto);

            if(!returnMessage.getStatus()) {
                throw new Exception("Could not approve entry.");
            }

            monitor.get().setStatus(Status.SUCCESS);
            monitor.get().setDateApplied(new Date());
            return this.monitorService.createOrUpdate(monitor.get());
        }

        throw new Exception("An unknown error occured.");
    }

    public Monitor denyEntry(String id, String reason) throws Exception {
        Optional<Monitor> monitor = this.monitorService.findById(UUID.fromString(id));

        if(monitor.isPresent()) {
            ReviewedDTO dto = new ReviewedDTO(monitor.get().getMusicId(), Status.DENIED, reason);
            ReturnMessage returnMessage = this.sendLogic.entryReviewed(dto);

            if(!returnMessage.getStatus()) {
                throw new Exception("Could not deny entry.");
            }

            monitor.get().setStatus(Status.DENIED);
            monitor.get().setDateApplied(new Date());
            monitor.get().setReason(reason);
            return this.monitorService.createOrUpdate(monitor.get());
        }

        throw new Exception("An unknown error occured.");
    }

}

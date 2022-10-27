package com.lyriqu.monitorservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lyriqu.monitorservice.controller.MonitorController;
import com.lyriqu.monitorservice.entity.Monitor;
import com.lyriqu.monitorservice.enums.Status;
import com.lyriqu.monitorservice.logic.MonitorLogic;
import com.lyriqu.monitorservice.rabbitmq.logic.SendLogic;
import com.lyriqu.monitorservice.rabbitmq.models.MusicDTO;
import com.lyriqu.monitorservice.rabbitmq.models.ReturnMessage;
import com.lyriqu.monitorservice.rabbitmq.models.ReviewedDTO;
import com.lyriqu.monitorservice.repository.MonitorRepository;
import com.lyriqu.monitorservice.service.MonitorService;
import com.rabbitmq.client.Return;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.lenient;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class MonitorTests {

    @InjectMocks
    private MonitorLogic monitorLogic;

    @Mock
    private MonitorService monitorService;

    @Mock
    private SendLogic sendLogic;

    ObjectMapper objectMapper;

    // Get music info
    UUID musicId = UUID.fromString("123e4567-e89b-42d3-a456-556642440050");
    UUID monitorId = UUID.fromString("123e4567-e89b-42d3-a456-556642440010");

    Monitor monitor;
    MusicDTO musicDTO;

    @BeforeEach
    void setUp() {
        this.objectMapper = new ObjectMapper();
        this.monitor = this.getMonitor(Status.WAITING);
        this.musicDTO = this.getMusicDTO();
    }

    @Test
    void GetAllWaitingEntries() {
        List<Monitor> monitors = new ArrayList<>();
        monitors.add(new Monitor());

        lenient().when(monitorService.findAllByStatus(Status.WAITING)).thenReturn(monitors);

        List<Monitor> monitorList = this.monitorLogic.getAllWaitingEntries();
        assertEquals(1, monitorList.size());
    }

    @Test
    void getMusicInfoValidId() throws Exception {
        lenient().when(monitorService.findById(monitorId)).thenReturn(Optional.of(monitor));
        lenient().when(sendLogic.getMusicInfo(musicId.toString())).thenReturn(new ReturnMessage(true, objectMapper.writeValueAsString(musicDTO)));

        MusicDTO dto = this.monitorLogic.getMusicInfo(monitorId.toString());
        assertEquals("Army", dto.getSongName());
    }

    @Test
    void getMusicInfoInvalidId() throws Exception {
        lenient().when(monitorService.findById(monitorId)).thenReturn(Optional.of(monitor));
        lenient().when(sendLogic.getMusicInfo(musicId.toString())).thenReturn(new ReturnMessage(false, "Couldn't find music"));

        assertThrows(Exception.class, () -> {
            this.monitorLogic.getMusicInfo(monitorId.toString());
        });
    }

    @Test
    void denyInvalidId() throws IOException {
        ReviewedDTO dto = this.getReviewedDTO(Status.DENIED);

        lenient().when(monitorService.findById(monitorId)).thenReturn(Optional.of(monitor));
        lenient().when(sendLogic.entryReviewed(dto)).thenReturn(new ReturnMessage(false, "Couldn't find music"));

        assertThrows(Exception.class, () -> {
            this.monitorLogic.denyEntry(monitorId.toString(), "");
        });
    }

    @Test
    void denyValidId() throws Exception {
        ReviewedDTO dto = this.getReviewedDTO(Status.DENIED);

        lenient().when(monitorService.findById(monitorId)).thenReturn(Optional.of(monitor));
        lenient().when(monitorService.createOrUpdate(monitor)).thenReturn(this.getMonitor(Status.DENIED));
        lenient().when(sendLogic.entryReviewed(dto)).thenReturn(new ReturnMessage(true, "OK"));

        assertEquals("DENIED", monitorLogic.denyEntry(monitorId.toString(), "").getStatus().name());
    }

    @Test
    void approveValidId() throws Exception {
        ReviewedDTO dto = this.getReviewedDTO(Status.SUCCESS);

        lenient().when(monitorService.findById(monitorId)).thenReturn(Optional.of(monitor));
        lenient().when(monitorService.createOrUpdate(monitor)).thenReturn(this.getMonitor(Status.SUCCESS));
        lenient().when(sendLogic.entryReviewed(dto)).thenReturn(new ReturnMessage(true, "OK"));

        assertEquals("SUCCESS", monitorLogic.approveEntry(monitorId.toString()).getStatus().name());
    }

    @Test
    void approveInvalidId() throws Exception {
        ReviewedDTO dto = this.getReviewedDTO(Status.DENIED);

        lenient().when(monitorService.findById(monitorId)).thenReturn(Optional.of(monitor));
        lenient().when(sendLogic.entryReviewed(dto)).thenReturn(new ReturnMessage(false, "Couldn't find music"));

        assertThrows(Exception.class, () -> {
            this.monitorLogic.approveEntry(monitorId.toString());
        });
    }

    private Monitor getMonitor(Status status) {
        Monitor monitor = new Monitor();
        monitor.setId(monitorId);
        monitor.setStatus(status);
        monitor.setMusicId(musicId.toString());
        monitor.setReason("");

        return monitor;
    }

    private MusicDTO getMusicDTO() {
        MusicDTO musicDTO = new MusicDTO();
        musicDTO.setSongName("Army");
        musicDTO.setImagePath("test");
        musicDTO.setSongPath("test");

        return musicDTO;
    }

    private ReviewedDTO getReviewedDTO(Status status) {
        return new ReviewedDTO(musicId.toString(), status, "");
    }
}

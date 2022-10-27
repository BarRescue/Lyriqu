package com.lyriqu.monitorservice.rabbitmq.models;

import com.lyriqu.monitorservice.enums.Status;
import lombok.Data;

@Data
public class ReviewedDTO {

    private String musicId;
    private Status status;
    private String reason;

    public ReviewedDTO(String musicId, Status status, String reason) {
        this.musicId = musicId;
        this.status = status;
        this.reason = reason;
    }

}

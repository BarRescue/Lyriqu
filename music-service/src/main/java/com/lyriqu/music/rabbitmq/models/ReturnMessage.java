package com.lyriqu.music.rabbitmq.models;

import com.rabbitmq.client.Return;
import lombok.Data;

import java.util.UUID;

@Data
public class ReturnMessage {

    private UUID id;
    private Boolean status;
    private String message;

    public ReturnMessage() {}

    public ReturnMessage(Boolean status, String message) {
        this.id = UUID.randomUUID();
        this.status = status;
        this.message = message;
    }

}

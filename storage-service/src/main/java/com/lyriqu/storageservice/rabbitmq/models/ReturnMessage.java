package com.lyriqu.storageservice.rabbitmq.models;

import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.rabbitmq.client.Return;
import lombok.Data;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.util.UUID;

@Data
public class ReturnMessage {

    private UUID id;
    private Boolean status;
    private String message;

    public ReturnMessage(Boolean status, String message) {
        this.id = UUID.randomUUID();
        this.status = status;
        this.message = message;
    }

}

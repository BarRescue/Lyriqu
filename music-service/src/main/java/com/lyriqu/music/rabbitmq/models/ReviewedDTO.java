package com.lyriqu.music.rabbitmq.models;

import com.lyriqu.music.enums.Status;
import lombok.Data;

@Data
public class ReviewedDTO {

    private String musicId;
    private Status status;
    private String reason;

}

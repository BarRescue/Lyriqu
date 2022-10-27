package com.lyriqu.music.rabbitmq.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class publishMonitoringDTO {

    private String musicId;

    private String userId;

}

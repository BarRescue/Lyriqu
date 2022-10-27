package com.lyriqu.payment.rabbitmq.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class customerDTO {

    private String user_id;
    private String customer_id;

    public customerDTO(String user_id) {
        this.user_id = user_id;
    }

}

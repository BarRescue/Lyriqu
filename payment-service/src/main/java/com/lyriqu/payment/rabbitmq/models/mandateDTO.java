package com.lyriqu.payment.rabbitmq.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class mandateDTO {

    private String customerId;
    private String mandateId;

}

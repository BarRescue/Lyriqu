package com.lyriqu.payment.rabbitmq.logic;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lyriqu.payment.rabbitmq.models.ReturnMessage;
import com.lyriqu.payment.rabbitmq.models.customerDTO;
import com.lyriqu.payment.rabbitmq.models.mandateDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Slf4j
public class SendLogic {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final RabbitTemplate template;

    @Autowired
    public SendLogic(RabbitTemplate rabbitTemplate) {
        this.template = rabbitTemplate;
    }

    public ReturnMessage sendCustomerData(String user, String customer) throws IOException {
        String response = objectMapper.writeValueAsString(new customerDTO(user, customer));

        byte[] b = response.getBytes();
        Message message = template.sendAndReceive("subscription-exchange", "create-customer", new Message(b, new MessageProperties()));

        byte[] body = message.getBody();

        return objectMapper.readValue(body, ReturnMessage.class);
    }

    public ReturnMessage getCustomerId(String user) throws IOException {
        String response = objectMapper.writeValueAsString(new customerDTO(user));

        byte[] b = response.getBytes();
        Message message = template.sendAndReceive("subscription-exchange", "get-customer", new Message(b, new MessageProperties()));

        byte[] body = message.getBody();
        return objectMapper.readValue(body, ReturnMessage.class);
    }

    public void addMandate(String customerId, String mandateId) throws IOException {
        String response = objectMapper.writeValueAsString(new mandateDTO(customerId, mandateId));

        byte[] b = response.getBytes();
        template.send("subscription-exchange", "add-mandate", new Message(b, new MessageProperties()));
    }
}

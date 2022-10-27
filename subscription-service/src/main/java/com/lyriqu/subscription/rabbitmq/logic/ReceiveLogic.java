package com.lyriqu.subscription.rabbitmq.logic;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.lyriqu.subscription.entity.Subscription;
import com.lyriqu.subscription.entity.SubscriptionOptions;
import com.lyriqu.subscription.logic.SubscriptionLogic;
import com.lyriqu.subscription.rabbitmq.models.ReturnMessage;
import com.lyriqu.subscription.rabbitmq.models.customerDTO;
import com.lyriqu.subscription.rabbitmq.models.mandateDTO;
import com.lyriqu.subscription.repository.SubscriptionOptionsRepository;
import com.lyriqu.subscription.repository.SubscriptionRepository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class ReceiveLogic {

    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionOptionsRepository subscriptionOptionsRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final SubscriptionLogic subscriptionLogic;

    @Autowired
    public ReceiveLogic(SubscriptionLogic subscriptionLogic, SubscriptionRepository subscriptionRepository, SubscriptionOptionsRepository subscriptionOptionsRepository) {
        this.subscriptionRepository = subscriptionRepository;
        this.subscriptionOptionsRepository = subscriptionOptionsRepository;
        this.subscriptionLogic = subscriptionLogic;
    }

    @RabbitListener(queues = {"createCustomer"})
    @Transactional
    public String createCustomerRequest(String in) throws JsonProcessingException {
        try {
            customerDTO message = objectMapper.readValue(in, customerDTO.class);

            log.info(message.toString());

            Optional<Subscription> s = subscriptionRepository.findByCustomerId(message.getCustomer_id());

            if(s.isPresent()) {
                throw new Exception("Already registered.");
            }

            // Save options
            SubscriptionOptions subscriptionOptions = new SubscriptionOptions();
            subscriptionOptionsRepository.save(subscriptionOptions);

            // Save subscription
            Subscription subscription = new Subscription(UUID.fromString(message.getUser_id()), subscriptionOptions);
            subscription.setCustomerId(message.getCustomer_id());
            subscriptionRepository.save(subscription);

            return objectMapper.writeValueAsString(new ReturnMessage(true, subscription.toString()));
        } catch (Exception e) {
            return objectMapper.writeValueAsString(new ReturnMessage(false, e.getMessage()));
        }
    }

    @RabbitListener(queues = {"getCustomer"})
    @Transactional
    public String getCustomerRequest(String in) throws JsonProcessingException {
        try {
            customerDTO message = objectMapper.readValue(in, customerDTO.class);

            Optional<Subscription> s = subscriptionRepository.findByUserId(UUID.fromString(message.getUser_id()));

            if(s.isEmpty()) {
                throw new Exception("No customer found.");
            }

            return objectMapper.writeValueAsString(new ReturnMessage(true, s.get().getCustomerId()));

        } catch(Exception e) {
            return objectMapper.writeValueAsString(new ReturnMessage(false, e.getMessage()));
        }
    }

    @RabbitListener(queues = {"addMandate"})
    @Transactional
    public void addMandateAndStartSubscription(String in) {
        try {
            this.subscriptionLogic.addMandateAndStartSubscription(objectMapper.readValue(in, mandateDTO.class));
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}

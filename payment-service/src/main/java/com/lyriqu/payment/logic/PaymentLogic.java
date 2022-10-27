package com.lyriqu.payment.logic;

import be.woutschoovaerts.mollie.Client;
import be.woutschoovaerts.mollie.ClientBuilder;
import be.woutschoovaerts.mollie.data.common.Amount;
import be.woutschoovaerts.mollie.data.common.Link;
import be.woutschoovaerts.mollie.data.common.Pagination;
import be.woutschoovaerts.mollie.data.customer.CustomerRequest;
import be.woutschoovaerts.mollie.data.customer.CustomerResponse;
import be.woutschoovaerts.mollie.data.mandate.MandatePaymentMethod;
import be.woutschoovaerts.mollie.data.mandate.MandateRequest;
import be.woutschoovaerts.mollie.data.mandate.MandateResponse;
import be.woutschoovaerts.mollie.data.payment.*;
import be.woutschoovaerts.mollie.data.subscription.SubscriptionRequest;
import be.woutschoovaerts.mollie.data.subscription.SubscriptionResponse;
import be.woutschoovaerts.mollie.exception.MollieException;
import com.lyriqu.payment.rabbitmq.logic.SendLogic;
import com.lyriqu.payment.rabbitmq.models.ReturnMessage;
import com.rabbitmq.client.Return;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class PaymentLogic {

    private final SendLogic sendLogic;
    private final MollieLogic mollieLogic;

    @Autowired
    public PaymentLogic(SendLogic sendLogic, MollieLogic mollieLogic) {
        this.sendLogic = sendLogic;
        this.mollieLogic = mollieLogic;
    }

    public String createCustomer(String user_id) throws Exception {
        try {
            CustomerResponse response = this.mollieLogic.createCustomer();
            ReturnMessage returnMessage = this.sendLogic.sendCustomerData(user_id, response.getId());

            if(!returnMessage.getStatus()) {
                throw new Exception(returnMessage.getMessage());
            }

            return response.getId();

        } catch (MollieException e) {
            throw new IOException(e.getMessage());
        }
    }

    public Link createMandate(String userId) throws Exception {
        ReturnMessage returnMessage = this.sendLogic.getCustomerId(userId);
        log.info(returnMessage.toString());
        String customerId = returnMessage.getMessage();

        if(!returnMessage.getStatus()) {
            customerId = this.createCustomer(userId);
        }

        PaymentResponse response = this.mollieLogic.createMandate(customerId);

        return response.getLinks().getCheckout();
    }

    public List<PaymentResponse> getPayments(String userId) throws Exception {
        ReturnMessage returnMessage = this.sendLogic.getCustomerId(userId);

        if(!returnMessage.getStatus()) {
            throw new Exception("Customer not found.");
        }

        return this.mollieLogic.getPayments(returnMessage.getMessage()).getEmbedded().getPayments();
    }

    public void checkPayment(String transactionId) throws Exception {
        PaymentResponse response = this.mollieLogic.getPayment(transactionId);

        if(response.getStatus() == PaymentStatus.PAID && response.getCustomerId().isPresent() && response.getMandateId().isPresent()) {
            this.sendLogic.addMandate(response.getCustomerId().get(), response.getMandateId().get());
        }
    }
}

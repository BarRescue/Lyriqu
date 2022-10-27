package com.lyriqu.payment.logic;

import be.woutschoovaerts.mollie.Client;
import be.woutschoovaerts.mollie.ClientBuilder;
import be.woutschoovaerts.mollie.data.common.Amount;
import be.woutschoovaerts.mollie.data.common.Pagination;
import be.woutschoovaerts.mollie.data.customer.CustomerRequest;
import be.woutschoovaerts.mollie.data.customer.CustomerResponse;
import be.woutschoovaerts.mollie.data.payment.PaymentListResponse;
import be.woutschoovaerts.mollie.data.payment.PaymentRequest;
import be.woutschoovaerts.mollie.data.payment.PaymentResponse;
import be.woutschoovaerts.mollie.data.payment.SequenceType;
import be.woutschoovaerts.mollie.exception.MollieException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Optional;

@Component
public class MollieLogic {

    private final Client client;

    @Autowired
    public MollieLogic() {
        this.client = new ClientBuilder()
                .withApiKey("test_yt6QwQKA68xs82mmzbJj8A3n5CKHn6")
                .build();
    }

    public CustomerResponse createCustomer() throws MollieException {
        return client.customers().createCustomer(new CustomerRequest());
    }

    public PaymentResponse getPayment(String transactionId) throws MollieException {
        return client.payments().getPayment(transactionId);
    }

    public Pagination<PaymentListResponse> getPayments(String customerId) throws MollieException {
        return client.customers().listCustomerPayments(customerId);
    }

    public PaymentResponse createMandate(String customerId) throws MollieException {
        PaymentRequest request = new  PaymentRequest();
        request.setAmount(new Amount("EUR", new BigDecimal("10.00")));
        request.setCustomerId(java.util.Optional.ofNullable(customerId));
        request.setSequenceType(java.util.Optional.of(SequenceType.FIRST));
        request.setDescription("First payment - Lyriqu");
        request.setRedirectUrl(Optional.of("https://lyriqu.nl/subscription"));
        request.setWebhookUrl(Optional.of("https://api.lyriqu.nl/payment/update/payment"));

        return client.payments().createPayment(request);
    }
}

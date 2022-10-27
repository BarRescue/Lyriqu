package com.lyriqu.subscription.logic;

import be.woutschoovaerts.mollie.Client;
import be.woutschoovaerts.mollie.ClientBuilder;
import be.woutschoovaerts.mollie.data.common.Amount;
import be.woutschoovaerts.mollie.data.subscription.SubscriptionRequest;
import be.woutschoovaerts.mollie.data.subscription.SubscriptionResponse;
import be.woutschoovaerts.mollie.data.subscription.SubscriptionStatus;
import be.woutschoovaerts.mollie.exception.MollieException;
import com.lyriqu.subscription.entity.Subscription;
import com.lyriqu.subscription.service.SubscriptionOptionService;
import com.lyriqu.subscription.service.SubscriptionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Optional;

@Component
@Slf4j
public class MollieLogic {

    private final Client client;

    public MollieLogic() {
        this.client = new ClientBuilder()
                .withApiKey("test_yt6QwQKA68xs82mmzbJj8A3n5CKHn6")
                .build();
    }

    public SubscriptionResponse startSubscription(Subscription subscription, String mandateId) throws Exception {
        try {
            SubscriptionRequest request = new SubscriptionRequest();
            request.setMandateId(Optional.of(mandateId));
            request.setWebhookUrl(Optional.of("https://api.lyriqu.nl/subscription/update/subscription"));
            request.setAmount(new Amount("EUR", new BigDecimal("10.00")));
            request.setInterval("1 month");
            request.setDescription("Lyriqu | subscription €10");

            SubscriptionResponse response = client.subscriptions().createSubscription(subscription.getCustomerId(), request);

            if(response.getStatus() != SubscriptionStatus.ACTIVE) {
                throw new Exception("Subscription couldn't be created");
            }

            return response;

        } catch(Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public SubscriptionResponse cancelSubscription(Subscription subscription) throws MollieException {
        return this.client.subscriptions().cancelSubscription(subscription.getCustomerId(), subscription.getSubscriptionId());
    }
}

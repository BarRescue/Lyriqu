package com.lyriqu.subscription.logic;

import be.woutschoovaerts.mollie.data.subscription.SubscriptionResponse;
import be.woutschoovaerts.mollie.data.subscription.SubscriptionStatus;
import com.lyriqu.subscription.entity.Subscription;
import com.lyriqu.subscription.entity.SubscriptionOptions;
import com.lyriqu.subscription.rabbitmq.models.mandateDTO;
import com.lyriqu.subscription.service.SubscriptionOptionService;
import com.lyriqu.subscription.service.SubscriptionService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Component
@Slf4j
public class SubscriptionLogic {

    private final SubscriptionService subscriptionService;
    private final SubscriptionOptionService subscriptionOptionService;
    private final MollieLogic mollieLogic;

    @Autowired
    public SubscriptionLogic(MollieLogic mollieLogic, SubscriptionOptionService subscriptionOptionService, SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
        this.subscriptionOptionService = subscriptionOptionService;
        this.mollieLogic = mollieLogic;
    }

    public Subscription getSubscription(String userId) throws Exception {
        Optional<Subscription> subscription = this.subscriptionService.findByUserId(UUID.fromString(userId));

        if(subscription.isEmpty()) {
            throw new Exception("Subscription not found");
        }

        return subscription.get();
    }

    public Subscription cancelSubscription(String userId) throws Exception {
        Optional<Subscription> subscription = this.subscriptionService.findByUserId(UUID.fromString(userId));

        if(subscription.isEmpty()) {
            throw new Exception("Subscription not found");
        }

        SubscriptionResponse response = this.mollieLogic.cancelSubscription(subscription.get());

        if(response.getStatus() == SubscriptionStatus.CANCELED) {
            subscription.get().setSubscriptionId(null);
            subscription.get().getOptions().setEndDate(subscription.get().getOptions().getNextPayment());
            subscription.get().getOptions().setNextPayment(null);
            subscription.get().getOptions().setMandateId(null);
            subscription.get().getOptions().setStartDate(null);
            return this.subscriptionService.createOrUpdate(subscription.get());
        } else {
            throw new Exception("Subscription couldn't be cancelled.");
        }
    }

    public Subscription addMandateAndStartSubscription(mandateDTO dto) throws Exception {
        if(!this.customerExists(dto.getCustomerId())) {
            throw new Exception("Customer does not exist.");
        }

        Optional<Subscription> subscription = this.subscriptionService.findByCustomerId(dto.getCustomerId());

        if(subscription.isPresent() && !subscription.get().getStatus().equals("active")) {
            if(!this.addMandate(subscription.get().getOptions(), dto.getMandateId())) {
                throw new Exception("Mandate couldn't be added.");
            }

            SubscriptionResponse response = this.mollieLogic.startSubscription(subscription.get(), dto.getMandateId());

            if(response.getStatus() == SubscriptionStatus.ACTIVE) {
                subscription.get().setStatus("active");
                subscription.get().setSubscriptionId(response.getId());
                Subscription updatedSubscription = this.subscriptionService.createOrUpdate(subscription.get());

                SubscriptionOptions options = updatedSubscription.getOptions();
                options.setStartDate(new Date());
                options.setNextPayment(DateUtils.addMonths(new Date(), 1));

                return updatedSubscription;
            } else {
                throw new Exception("Subscription couldn't be started.");
            }
        }

        throw new Exception("Subscription couldn't be started.");
    }

    private boolean addMandate(SubscriptionOptions options, String mandateId) {
        options.setMandateId(mandateId);
        Optional<SubscriptionOptions> updatedOptions = this.subscriptionOptionService.createOrUpdate(options);
        return updatedOptions.isPresent();
    }

    private boolean customerExists(String customerId) {
        return this.subscriptionService.customerExists(customerId);
    }
}

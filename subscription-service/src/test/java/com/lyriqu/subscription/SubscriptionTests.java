package com.lyriqu.subscription;

import be.woutschoovaerts.mollie.data.subscription.SubscriptionResponse;
import be.woutschoovaerts.mollie.data.subscription.SubscriptionStatus;
import be.woutschoovaerts.mollie.exception.MollieException;
import com.lyriqu.subscription.entity.Subscription;
import com.lyriqu.subscription.entity.SubscriptionOptions;
import com.lyriqu.subscription.logic.MollieLogic;
import com.lyriqu.subscription.logic.SubscriptionLogic;
import com.lyriqu.subscription.rabbitmq.models.mandateDTO;
import com.lyriqu.subscription.service.SubscriptionOptionService;
import com.lyriqu.subscription.service.SubscriptionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestExecutionListeners;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
public class SubscriptionTests {

    @InjectMocks
    private SubscriptionLogic subscriptionLogic;

    @Mock
    private SubscriptionService subscriptionService;

    @Mock
    private SubscriptionOptionService subscriptionOptionService;

    @Mock
    private MollieLogic mollieLogic;

    UUID customerId = UUID.fromString("123e4567-e89b-42d3-a456-556642440010");
    UUID subscriptionId = UUID.fromString("123e4567-e89b-42d3-a456-556642440020");
    UUID mandateId = UUID.fromString("123e4567-e89b-42d3-a456-556642440030");
    UUID userId = UUID.fromString("123e4567-e89b-42d3-a456-556642440040");

    @Test
    void getSubscriptionInvalidId() {
        lenient().when(subscriptionService.findByUserId(userId)).thenReturn(Optional.empty());

        assertThrows(Exception.class, () -> {
            this.subscriptionLogic.getSubscription(userId.toString());
        });
    }

    @Test
    void getSubscriptionValidId() throws Exception {
        lenient().when(subscriptionService.findByUserId(userId)).thenReturn(Optional.of(this.getSubscription()));

        assertEquals(subscriptionId.toString(), subscriptionLogic.getSubscription(userId.toString()).getId().toString());
    }

    @Test
    void cancelSubscriptionWrongUserId() {
        lenient().when(subscriptionService.findByUserId(userId)).thenReturn(Optional.empty());

        assertThrows(Exception.class, () -> {
            this.subscriptionLogic.cancelSubscription(userId.toString());
        });
    }

    @Test
    void cancelSubscriptionUserId() throws Exception {
        Subscription subscription = this.getSubscription();
        SubscriptionResponse status = new SubscriptionResponse();
        status.setStatus(SubscriptionStatus.CANCELED);

        Subscription subscriptionToReturn = this.getSubscription();
        subscriptionToReturn.setStatus("active");

        lenient().when(subscriptionService.findByUserId(userId)).thenReturn(Optional.of(subscription));
        lenient().when(mollieLogic.cancelSubscription(subscription)).thenReturn(status);
        lenient().when(subscriptionService.createOrUpdate(any(Subscription.class))).thenReturn(subscriptionToReturn);

        assertEquals("active", this.subscriptionLogic.cancelSubscription(userId.toString()).getStatus());
    }

    @Test
    void cancelSubscriptionNotActive() throws MollieException {
        Subscription subscription = this.getSubscription();
        SubscriptionResponse status = new SubscriptionResponse();
        status.setStatus(SubscriptionStatus.PENDING);

        lenient().when(subscriptionService.findByUserId(userId)).thenReturn(Optional.of(subscription));
        lenient().when(mollieLogic.cancelSubscription(subscription)).thenReturn(status);

        assertThrows(Exception.class, () -> {
            this.subscriptionLogic.cancelSubscription(userId.toString());
        });
    }

    @Test
    void startSubscriptionNoCustomerId() {
        lenient().when(subscriptionService.customerExists(customerId.toString())).thenReturn(false);

        assertThrows(Exception.class, () -> {
            this.subscriptionLogic.addMandateAndStartSubscription(this.getMandateDTO());
        });
    }

    @Test
    void startSubscriptionValid() throws Exception {
        Subscription subscription = this.getSubscription();
        SubscriptionResponse status = new SubscriptionResponse();
        status.setStatus(SubscriptionStatus.ACTIVE);

        Subscription subscriptionToReturn = this.getSubscription();
        subscriptionToReturn.setStatus("active");

        lenient().when(subscriptionService.customerExists(customerId.toString())).thenReturn(true);
        lenient().when(subscriptionService.findByCustomerId(customerId.toString())).thenReturn(Optional.of(subscription));
        lenient().when(subscriptionOptionService.createOrUpdate(any(SubscriptionOptions.class))).thenReturn(Optional.of(new SubscriptionOptions()));
        lenient().when(mollieLogic.startSubscription(subscription, mandateId.toString())).thenReturn(status);
        lenient().when(subscriptionService.createOrUpdate(any(Subscription.class))).thenReturn(subscriptionToReturn);

        assertEquals("active", this.subscriptionLogic.addMandateAndStartSubscription(this.getMandateDTO()).getStatus());
    }

    @Test
    void startSubscriptionInvalid() throws Exception {
        Subscription subscription = this.getSubscription();
        SubscriptionResponse status = new SubscriptionResponse();
        status.setStatus(null);

        lenient().when(subscriptionService.customerExists(customerId.toString())).thenReturn(true);
        lenient().when(subscriptionService.findByCustomerId(customerId.toString())).thenReturn(Optional.of(subscription));
        lenient().when(subscriptionOptionService.createOrUpdate(any(SubscriptionOptions.class))).thenReturn(Optional.of(new SubscriptionOptions()));
        lenient().when(mollieLogic.startSubscription(subscription, mandateId.toString())).thenReturn(status);

        assertThrows(Exception.class, () -> {
            this.subscriptionLogic.addMandateAndStartSubscription(this.getMandateDTO());
        });
    }

    private mandateDTO getMandateDTO() {
        mandateDTO mandateDTO = new mandateDTO();
        mandateDTO.setCustomerId(customerId.toString());
        mandateDTO.setMandateId(mandateId.toString());

        return mandateDTO;
    }

    private Subscription getSubscription() {
        Subscription subscription = new Subscription();
        subscription.setId(subscriptionId);
        subscription.setStatus("not-active");
        subscription.setUserId(userId);
        subscription.setOptions(new SubscriptionOptions());
        subscription.getOptions().setNextPayment(new Date());

        return subscription;
    }
}

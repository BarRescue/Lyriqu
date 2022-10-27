package com.lyriqu.subscription.controller;

import com.lyriqu.subscription.entity.Subscription;
import com.lyriqu.subscription.logic.SubscriptionLogic;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/subscription")
public class SubscriptionController {

    private final SubscriptionLogic subscriptionLogic;

    @Autowired
    public SubscriptionController(SubscriptionLogic subscriptionLogic) {
        this.subscriptionLogic = subscriptionLogic;
    }

    @PostMapping("update/subscription")
    public void updateSubscription(@RequestParam String id) {
        log.info(id);
    }

    @GetMapping
    public ResponseEntity getSubscription(@AuthenticationPrincipal Jwt user) {
        try {
            return new ResponseEntity<>(this.subscriptionLogic.getSubscription(user.getSubject()), HttpStatus.OK);
        } catch(Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/cancel")
    public ResponseEntity cancelSubscription(@AuthenticationPrincipal Jwt user) {
        try {
            this.subscriptionLogic.cancelSubscription(user.getSubject());
            return new ResponseEntity<>("Subscription cancelled.", HttpStatus.OK);
        } catch(Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}

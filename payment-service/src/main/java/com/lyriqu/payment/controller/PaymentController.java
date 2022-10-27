package com.lyriqu.payment.controller;

import com.lyriqu.payment.logic.PaymentLogic;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/payment")
public class PaymentController {

    private final PaymentLogic paymentLogic;

    @Autowired
    public PaymentController(PaymentLogic paymentLogic) {
        this.paymentLogic = paymentLogic;
    }

    @GetMapping
    public ResponseEntity getPayments(@AuthenticationPrincipal Jwt user) {
        try {
            return new ResponseEntity<>(this.paymentLogic.getPayments(user.getSubject()), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/create/customer")
    public void createCustomer(@AuthenticationPrincipal Jwt user) {
        try {
            this.paymentLogic.createCustomer(user.getSubject());
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @PostMapping("/create/mandate")
    public ResponseEntity createMandate(@AuthenticationPrincipal Jwt user) {
        try {
            return new ResponseEntity<>(this.paymentLogic.createMandate(user.getSubject()), HttpStatus.OK);
        } catch(Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/update/payment")
    public void updatePayment(@RequestParam String id) throws Exception {
        try {
            this.paymentLogic.checkPayment(id);
        } catch(Exception e) {
            log.error(e.getMessage());
        }
    }
}

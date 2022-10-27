package com.lyriqu.subscription.service;

import com.lyriqu.subscription.entity.Subscription;
import com.lyriqu.subscription.repository.SubscriptionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class SubscriptionService {

    private final SubscriptionRepository repository;

    @Autowired
    public SubscriptionService(SubscriptionRepository repository) {
        this.repository = repository;
    }

    public boolean customerExists(String customerId) {
        return this.repository.existsByCustomerId(customerId);
    }

    public Optional<Subscription> findByCustomerId(String customerId) {
        return this.repository.findByCustomerId(customerId);
    }

    public Subscription createOrUpdate(Subscription subscription) {
        return this.repository.save(subscription);
    }

    public Optional<Subscription> findByUserId(UUID id) {
        return this.repository.findByUserId(id);
    }
}

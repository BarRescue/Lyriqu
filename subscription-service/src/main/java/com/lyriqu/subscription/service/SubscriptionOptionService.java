package com.lyriqu.subscription.service;

import com.lyriqu.subscription.entity.SubscriptionOptions;
import com.lyriqu.subscription.repository.SubscriptionOptionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SubscriptionOptionService {

    private final SubscriptionOptionsRepository repository;

    @Autowired
    public SubscriptionOptionService(SubscriptionOptionsRepository repository) {
        this.repository = repository;
    }

    public Optional<SubscriptionOptions> createOrUpdate(SubscriptionOptions options) {
        return Optional.of(this.repository.save(options));
    }
}

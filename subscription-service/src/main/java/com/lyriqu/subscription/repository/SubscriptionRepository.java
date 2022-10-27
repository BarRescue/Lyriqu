package com.lyriqu.subscription.repository;

import com.lyriqu.subscription.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, UUID> {
    Optional<Subscription> findByCustomerId(String id);
    Optional<Subscription> findByUserId(UUID id);
    boolean existsByCustomerId(String customerId);
}

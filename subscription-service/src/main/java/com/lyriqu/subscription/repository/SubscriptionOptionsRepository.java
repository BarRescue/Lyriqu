package com.lyriqu.subscription.repository;

import com.lyriqu.subscription.entity.SubscriptionOptions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SubscriptionOptionsRepository extends JpaRepository<SubscriptionOptions, UUID> {

}

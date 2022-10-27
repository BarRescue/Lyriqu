package com.lyriqu.subscription.entity;

import com.sun.istack.NotNull;
import com.sun.istack.Nullable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.UniqueConstraint;
import java.util.UUID;

@Entity
@Getter @Setter
@NoArgsConstructor
public class Subscription extends BaseEntity {

    @NotNull
    @Column(unique=true)
    private UUID userId;

    @NotNull
    private String status = "not-active";

    @Nullable
    private String subscriptionId;

    @Nullable
    private String paymentMethod;

    @Nullable
    private String customerId;

    @OneToOne
    private SubscriptionOptions options;

    public Subscription(UUID userId, SubscriptionOptions options) {
        this.userId = userId;
        this.options = options;
    }
}

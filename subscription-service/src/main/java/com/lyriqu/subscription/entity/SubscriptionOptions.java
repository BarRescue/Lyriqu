package com.lyriqu.subscription.entity;

import com.sun.istack.Nullable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import java.util.Date;

@Entity
@Getter @Setter
@NoArgsConstructor
public class SubscriptionOptions extends BaseEntity {

    @Nullable
    private String mandateId;

    @Nullable
    private Date endDate;

    @Nullable
    private Date nextPayment;

    @Nullable
    private Date startDate;

}

package org.climbing.domain;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "subscription")
public class Subscription implements java.io.Serializable {

    @EmbeddedId
    private SubscriptionId subscriptionId;

    @ManyToOne(fetch = FetchType.EAGER)
    private SubscriptionType subscriptionType;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "end_date")
    private Date endDate;

    public SubscriptionId getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(SubscriptionId subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public SubscriptionType getSubscriptionType() {
        return subscriptionType;
    }

    public void setSubscriptionType(SubscriptionType subscriptionType) {
        this.subscriptionType = subscriptionType;
    }
}

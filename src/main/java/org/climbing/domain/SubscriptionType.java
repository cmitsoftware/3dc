package org.climbing.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@Entity(name = "subscription_type")
public class SubscriptionType implements Serializable {

    @Id
    @Column(name = "name")
    private String name;
    @Column(name = "start_month")
    private Integer startMonth;
    @Column(name = "end_month")
    private Integer endMonth;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getStartMonth() {
        return startMonth;
    }

    public void setStartMonth(Integer startMonth) {
        this.startMonth = startMonth;
    }

    public Integer getEndMonth() {
        return endMonth;
    }

    public void setEndMonth(Integer endMonth) {
        this.endMonth = endMonth;
    }
}

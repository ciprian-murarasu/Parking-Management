package com.portfolio.parkingmanagement.model;


import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

@Entity
@Table(name = "subscriptions_usage")
public class SubscriptionUsage {
    @Id
    @GeneratedValue
    private long id;

    private Timestamp enterDate;
    private Timestamp exitDate;

    @ManyToOne
    @JoinColumn(name = "subscription_id")
    private Subscription subscription;

    public SubscriptionUsage() {
    }

    public SubscriptionUsage(Timestamp enterDate, Timestamp exitDate, Subscription subscription) {
        this.enterDate = enterDate;
        this.exitDate = exitDate;
        this.subscription = subscription;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Timestamp getEnterDate() {
        return enterDate;
    }

    public void setEnterDate(Timestamp enterDate) {
        enterDate = new Timestamp(new Date().getTime());
    }

    public Timestamp getExitDate() {
        return exitDate;
    }

    public void setExitDate(Timestamp exitDate) {
        this.exitDate = exitDate;
    }

    public Subscription getSubscription() {
        return subscription;
    }

    public void setSubscription(Subscription subscription) {
        this.subscription = subscription;
    }
}

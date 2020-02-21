package com.portfolio.parkingmanagement.model;


import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "subscriptions_usage")
public class SubscriptionUsage {
    @Id
    @GeneratedValue
    private long id;

    private LocalDateTime enterDate;
    private LocalDateTime exitDate;

    @ManyToOne
    @JoinColumn(name = "subscription_id")
    private Subscription subscription;

    public SubscriptionUsage() {
    }

    public SubscriptionUsage(LocalDateTime enterDate, LocalDateTime exitDate, Subscription subscription) {
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

    public LocalDateTime getEnterDate() {
        return enterDate;
    }

    public void setEnterDate(LocalDateTime enterDate) {
        enterDate = LocalDateTime.now();
    }

    public LocalDateTime getExitDate() {
        return exitDate;
    }

    public void setExitDate(LocalDateTime exitDate) {
        this.exitDate = exitDate;
    }

    public Subscription getSubscription() {
        return subscription;
    }

    public void setSubscription(Subscription subscription) {
        this.subscription = subscription;
    }
}

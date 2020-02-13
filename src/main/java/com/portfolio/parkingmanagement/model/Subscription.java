package com.portfolio.parkingmanagement.model;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "subscriptions")
public class Subscription {
    @Id
    @GeneratedValue
    private Long id;

    private String code;

    @ManyToOne
    private SubscriptionType subscriptionType;

    private Timestamp startDate;

    private Timestamp endDate;

    private int price;

    @OneToOne(mappedBy = "subscription", fetch = FetchType.EAGER)
    private ParkingSpace parkingSpace;

    @OneToMany(mappedBy = "subscription")
    private List<SubscriptionUsage> subscriptionUsages;

    public Subscription() {
        code = "S" + (long) (Math.random() * 1_000_000_000);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public SubscriptionType getSubscriptionType() {
        return subscriptionType;
    }

    public void setSubscriptionType(SubscriptionType subscriptionType) {
        this.subscriptionType = subscriptionType;
    }

    public Timestamp getStartDate() {
        return startDate;
    }

    public void setStartDate(Timestamp startDate) {
        this.startDate = startDate;
    }

    public Timestamp getEndDate() {
        return endDate;
    }

    public void setEndDate() {
        int period = subscriptionType.getPeriod();
        endDate = new Timestamp(startDate.getTime() + (period * 24 * 3600 - 1) * 1000L);
    }

    public int getPrice() {
        return price;
    }

    public void setPrice() {
        price = subscriptionType.getPrice();
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public ParkingSpace getParkingSpace() {
        return parkingSpace;
    }

    public void setParkingSpace(ParkingSpace parkingSpace) {
        this.parkingSpace = parkingSpace;
    }

    public List<SubscriptionUsage> getSubscriptionUsages() {
        return subscriptionUsages;
    }

    public void setSubscriptionUsages(List<SubscriptionUsage> subscriptionUsages) {
        this.subscriptionUsages = subscriptionUsages;
    }
}

package com.portfolio.parkingmanagement.service;

import com.portfolio.parkingmanagement.model.Subscription;
import com.portfolio.parkingmanagement.model.SubscriptionUsage;
import com.portfolio.parkingmanagement.repository.SubscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Service
public class SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;

    @Autowired
    public SubscriptionService(SubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
    }

    public void save(Subscription subscription) {
        subscriptionRepository.save(subscription);
    }

    public Subscription generateSubscription() {
        Subscription newSubscription = new Subscription();
        save(newSubscription);
        return newSubscription;
    }

    public Subscription getByCode(String code) {
        return subscriptionRepository.findByCode(code);
    }

    public boolean isValid(String code) {
        return getByCode(code) != null;
    }

    public boolean hasEntered(String code) {
        return getByCode(code).getParkingSpace() != null;
    }

    public boolean isExpired(String code) {
        Subscription subscription = getByCode(code);
        Timestamp currentDate = new Timestamp(new Date().getTime());
        return currentDate.getTime() > subscription.getEndDate().getTime();
    }
//
//    public List<SubscriptionUsage> getAllUsages(String code) {
//        return subscriptionRepository.findAllUsages(code);
//    }
}

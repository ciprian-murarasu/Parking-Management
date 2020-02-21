package com.portfolio.parkingmanagement.service;

import com.portfolio.parkingmanagement.model.Subscription;
import com.portfolio.parkingmanagement.repository.SubscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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

    public boolean hasStarted(String code) {
        Subscription subscription = getByCode(code);
        return subscription.getStartDate().compareTo(LocalDateTime.now()) <= 0;
    }

    public boolean hasExpired(String code) {
        Subscription subscription = getByCode(code);
        return subscription.getEndDate().isBefore(LocalDateTime.now());
    }
//
//    public List<SubscriptionUsage> getAllUsages(String code) {
//        return subscriptionRepository.findAllUsages(code);
//    }
}

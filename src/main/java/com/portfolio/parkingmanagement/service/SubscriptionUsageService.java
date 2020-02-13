package com.portfolio.parkingmanagement.service;

import com.portfolio.parkingmanagement.model.Subscription;
import com.portfolio.parkingmanagement.repository.SubscriptionUsageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubscriptionUsageService {
    private final SubscriptionUsageRepository subscriptionUsageRepository;

    @Autowired
    public SubscriptionUsageService(SubscriptionUsageRepository subscriptionUsageRepository) {
        this.subscriptionUsageRepository = subscriptionUsageRepository;
    }

//    public Subscription getSubscription(Subscription subscription) {
//        return subscriptionUsageRepository.findSubscription(subscription);
//    }

}

package com.portfolio.parkingmanagement.repository;

import com.portfolio.parkingmanagement.model.Subscription;
import com.portfolio.parkingmanagement.model.SubscriptionUsage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionUsageRepository extends JpaRepository<SubscriptionUsage, Long> {
//    public Subscription findSubscription(Subscription subscription);
}

package com.portfolio.parkingmanagement.repository;

import com.portfolio.parkingmanagement.model.Subscription;
import com.portfolio.parkingmanagement.model.SubscriptionUsage;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubscriptionRepository extends CrudRepository<Subscription, Long> {
    Subscription findByCode(String code);

//    List<SubscriptionUsage> findAllUsages(String code);
}

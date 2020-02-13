package com.portfolio.parkingmanagement.repository;

import com.portfolio.parkingmanagement.model.SubscriptionType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionTypeRepository extends CrudRepository<SubscriptionType, Integer> {
    SubscriptionType findByName(String name);
}

package com.portfolio.parkingmanagement.service;

import com.portfolio.parkingmanagement.model.SubscriptionType;
import com.portfolio.parkingmanagement.repository.SubscriptionTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SubscriptionTypeService {
    private final SubscriptionTypeRepository subscriptionTypeRepository;

    @Autowired
    public SubscriptionTypeService(SubscriptionTypeRepository subscriptionTypeRepository) {
        this.subscriptionTypeRepository = subscriptionTypeRepository;
    }

    public List<SubscriptionType> getAllSubscriptionTypes() {
        List<SubscriptionType> subscriptionTypes = new ArrayList<>();
        subscriptionTypeRepository.findAll().iterator().forEachRemaining(subscriptionTypes::add);
        return subscriptionTypes;
    }

    public SubscriptionType findByName(String name) {
        return subscriptionTypeRepository.findByName(name);
    }
}

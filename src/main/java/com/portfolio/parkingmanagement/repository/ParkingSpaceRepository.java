package com.portfolio.parkingmanagement.repository;

import com.portfolio.parkingmanagement.model.ParkingSpace;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParkingSpaceRepository extends CrudRepository<ParkingSpace, Integer> {
    ParkingSpace findByNumber(int number);

    ParkingSpace findByTicketCode(String code);

    ParkingSpace findBySubscriptionCode(String code);
}

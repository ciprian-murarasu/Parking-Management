package com.portfolio.parkingmanagement.service;

import com.portfolio.parkingmanagement.model.ParkingSpace;
import com.portfolio.parkingmanagement.repository.ParkingSpaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.TreeSet;

@Service
public class ParkingSpaceService {
    private ParkingSpaceRepository parkingSpaceRepository;

    @Autowired
    public ParkingSpaceService(ParkingSpaceRepository parkingSpaceRepository) {
        this.parkingSpaceRepository = parkingSpaceRepository;
    }

    public Set<ParkingSpace> getAllParkingSpaces() {
        Set<ParkingSpace> parkingSpaces = new TreeSet<>();
        parkingSpaceRepository.findAll().iterator().forEachRemaining(parkingSpaces::add);
        return parkingSpaces;
    }

    public void save(ParkingSpace parkingSpace) {
        parkingSpaceRepository.save(parkingSpace);
    }

    public boolean hasAvailableSpaces() {
        Set<ParkingSpace> allParkingSpaces = getAllParkingSpaces();
        for (ParkingSpace parkingSpace : allParkingSpaces) {
            if (parkingSpace.getAvailability()) {
                return true;
            }
        }
        return false;
    }

    public ParkingSpace getByTicketCode(String code) {
        return parkingSpaceRepository.findByTicketCode(code);
    }

    public ParkingSpace getBySubscriptionCode(String code) {
        return parkingSpaceRepository.findBySubscriptionCode(code);
    }
}

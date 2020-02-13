package com.portfolio.parkingmanagement.service;

import com.portfolio.parkingmanagement.model.ParkingSpace;
import com.portfolio.parkingmanagement.model.Sector;
import com.portfolio.parkingmanagement.repository.SectorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.TreeSet;

@Service
public class SectorService {
    private SectorRepository sectorRepository;

    @Autowired
    public SectorService(SectorRepository sectorRepository) {
        this.sectorRepository = sectorRepository;
    }

    public Sector createSector(String name) {
        Sector sector = new Sector(name);
        save(sector);
        return sector;
    }

    public Set<Sector> getAllSectors() {
        Set<Sector> sectors = new TreeSet<>();
        sectorRepository.findAll().iterator().forEachRemaining(sectors::add);
        return sectors;
    }

    public Sector getByName(String name) {
        return sectorRepository.findByName(name);
    }

    public void save(Sector sector) {
        sectorRepository.save(sector);
    }

    public Sector getById(long id) {
        return sectorRepository.findById(id).get();
    }

    public boolean hasOccupiedSpaces(Sector sector) {
        Set<ParkingSpace> parkingSpaces = sector.getParkingSpaces();
        for (ParkingSpace p : parkingSpaces) {
            if (!p.getAvailability()) {
                return true;
            }
        }
        return false;
    }

    public void delete(Sector sector) {
        sectorRepository.delete(sector);
    }
}

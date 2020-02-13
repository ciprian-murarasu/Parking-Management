package com.portfolio.parkingmanagement.repository;

import com.portfolio.parkingmanagement.model.Sector;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SectorRepository extends CrudRepository<Sector, Long> {
    Sector findByName(String name);
}

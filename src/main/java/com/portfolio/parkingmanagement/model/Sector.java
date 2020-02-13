package com.portfolio.parkingmanagement.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;
import java.util.TreeSet;

@Entity
@Table(name = "sectors")
public class Sector implements Comparable<Sector> {
    @Id
    @GeneratedValue
    private Long id;

    @NotNull(message = "Sector name is missing")
    private String name;

    @OneToMany(mappedBy = "sector", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("number ASC")
    private Set<ParkingSpace> parkingSpaces = new TreeSet<>();

    public Sector(@NotNull(message = "Sector name is missing") String name) {
        this.name = name;
    }

    public Sector() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<ParkingSpace> getParkingSpaces() {
        return parkingSpaces;
    }

    public void setParkingSpaces(Set<ParkingSpace> parkingSpaces) {
        this.parkingSpaces = parkingSpaces;
    }

    @Override
    public int compareTo(Sector o) {
        return this.name.compareTo(o.getName());
    }

    public void addParkingSpace(ParkingSpace space) {
        parkingSpaces.add(space);
    }

    public void deleteParkingSpace(ParkingSpace space) {
        parkingSpaces.remove(space);
    }

    public static String extractSectorName(String fullSpaceName) {
        String sectorName = "";
        char[] fullSpaceNameArray = fullSpaceName.toCharArray();
        for (char c : fullSpaceNameArray) {
            if (c >= 'A' && c <= 'Z') {
                sectorName += c;
            } else break;
        }
        return sectorName;
    }
}

package com.portfolio.parkingmanagement.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "parking_spaces")
public class ParkingSpace implements Comparable<ParkingSpace> {
    @Id
    @GeneratedValue
    private Long id;

    @NotNull(message = "Parking space status is missing")
    private boolean isAvailable;

    @NotNull(message = "Parking space number is missing")
    private Integer number;

    @ManyToOne
    @JoinColumn(name = "sector_id")
    private Sector sector;

    @OneToOne()
    @JoinColumn(name = "ticket_id")
    private Ticket ticket;

    @OneToOne()
    @JoinColumn(name = "subscription_id")
    private Subscription subscription;

    public ParkingSpace() {
    }

    public ParkingSpace(Integer number, Sector sector) {
        this.isAvailable = true;
        this.number = number;
        this.sector = sector;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean getAvailability() {
        return isAvailable;
    }

    public void setAvailability(boolean available) {
        isAvailable = available;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Sector getSector() {
        return sector;
    }

    public void setSector(Sector sector) {
        this.sector = sector;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    public Subscription getSubscription() {
        return subscription;
    }

    public void setSubscription(Subscription subscription) {
        this.subscription = subscription;
    }

    @Override
    public int compareTo(ParkingSpace o) {
        return o.getNumber() - this.number;
    }
}

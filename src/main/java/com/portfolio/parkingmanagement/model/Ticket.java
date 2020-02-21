package com.portfolio.parkingmanagement.model;

import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "tickets")
public class Ticket {
    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    private String code;

    @Range(min = 0, message = "Invalid paid amount")
    private Integer paidAmount;

    @NotNull
    private LocalDateTime enterDate;

    private LocalDateTime exitDate;

    private LocalDateTime payDate;

    @OneToMany(/*mappedBy = "ticket"*/)
    List<TicketPriceType> ticketPriceTypes;

    @OneToOne(mappedBy = "ticket", cascade = CascadeType.ALL)
    private ParkingSpace parkingSpace;

    public Ticket() {
        code = "T" + (long) Math.ceil(Math.random() * 1_000_000_000);
        paidAmount = 0;
        enterDate = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(Integer paidAmount) {
        this.paidAmount = paidAmount;
    }

    public LocalDateTime getEnterDate() {
        return enterDate;
    }

    public void setEnterDate(LocalDateTime enterDate) {
        this.enterDate = enterDate;
    }

    public LocalDateTime getExitDate() {
        return exitDate;
    }

    public void setExitDate(LocalDateTime exitDate) {
        this.exitDate = exitDate;
    }

    public LocalDateTime getPayDate() {
        return payDate;
    }

    public void setPayDate(LocalDateTime payDate) {
        this.payDate = payDate;
    }

    public ParkingSpace getParkingSpace() {
        return parkingSpace;
    }

    public void setParkingSpace(ParkingSpace parkingSpace) {
        this.parkingSpace = parkingSpace;
    }
}

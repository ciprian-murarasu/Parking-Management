package com.portfolio.parkingmanagement.model;

import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.Date;

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
    private Timestamp enterDate;

    private Timestamp exitDate;

    private Timestamp payDate;

    @OneToOne(mappedBy = "ticket", cascade = CascadeType.ALL)
    private ParkingSpace parkingSpace;

    public Ticket() {
        code = "T" + (long) Math.ceil(Math.random() * 1_000_000_000);
        paidAmount = 0;
        enterDate = new Timestamp(new Date().getTime());
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

    public Timestamp getEnterDate() {
        return enterDate;
    }

    public void setEnterDate(Timestamp enterDate) {
        this.enterDate = enterDate;
    }

    public Timestamp getExitDate() {
        return exitDate;
    }

    public void setExitDate(Timestamp exitDate) {
        this.exitDate = exitDate;
    }

    public Timestamp getPayDate() {
        return payDate;
    }

    public void setPayDate(Timestamp payDate) {
        this.payDate = payDate;
    }

    public ParkingSpace getParkingSpace() {
        return parkingSpace;
    }

    public void setParkingSpace(ParkingSpace parkingSpace) {
        this.parkingSpace = parkingSpace;
    }
}

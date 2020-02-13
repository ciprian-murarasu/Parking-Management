package com.portfolio.parkingmanagement.service;

import com.portfolio.parkingmanagement.model.Subscription;
import com.portfolio.parkingmanagement.model.Ticket;
import com.portfolio.parkingmanagement.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;

@Service
public class TicketService {
    private TicketRepository ticketRepository;
    private final int PRICE_PER_HOUR = 3;

    @Autowired
    public TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    public void save(Ticket ticket) {
        ticketRepository.save(ticket);
    }

    public Ticket generateTicket() {
        Ticket ticket = new Ticket();
        save(ticket);
        return ticket;
    }

    public Ticket generateTicketForSubscription(Subscription subscription) {
        Ticket ticket = new Ticket();
        long enterDate = subscription.getEndDate().getTime() + 1000;
        ticket.setEnterDate(new Timestamp(enterDate));
        save(ticket);
        return ticket;
    }

    public Ticket getByCode(String code) {
        return ticketRepository.findByCode(code);
    }

    public boolean isValid(String code) {
        return getByCode(code) != null;
    }

    public boolean hasEntered(String code) {
        return getByCode(code).getEnterDate() != null;
    }

    public boolean isPaid(String code) {
        return getByCode(code).getPayDate() != null;
    }

    public boolean payTicket(String code) {
        Ticket ticket = getByCode(code);
        if (ticket.getPayDate() == null) {
            Timestamp enterDate = ticket.getEnterDate();
            Timestamp payDate = new Timestamp(new Date().getTime());
            int minutesSpent = (int) Math.ceil((payDate.getTime() - enterDate.getTime()) / 60_000);
            int hoursSpent = minutesSpent / 60;
            if (minutesSpent % 60 > 0) {
                hoursSpent++;
            }
            int paidAmount = hoursSpent * PRICE_PER_HOUR;
            ticket.setPayDate(payDate);
            ticket.setPaidAmount(paidAmount);
            save(ticket);
            return true;
        }
        return false;
    }

    public boolean hasToPayExtra(String code) {
        Ticket ticket = getByCode(code);
        Timestamp exitDate = new Timestamp(new Date().getTime());
        if (ticket.getExitDate() == null && ticket.getPayDate() != null) {
            Timestamp payDate = ticket.getPayDate();
            int extraMinutesSpent = (int) Math.ceil((exitDate.getTime() - payDate.getTime()) / 60_000);
            return extraMinutesSpent > 30;
        }
        return false;
    }

    public int payExtra(String code) {
        Ticket ticket = getByCode(code);
        Timestamp enterDate = ticket.getEnterDate();
        int extraHours = 0;
        if (ticket.getExitDate() == null) {
            Timestamp exitDate = new Timestamp(new Date().getTime());
            int totalMinutesSpent = (int) Math.ceil((exitDate.getTime() - enterDate.getTime()) / 60_000);
            int minutesPaid = ticket.getPaidAmount() / PRICE_PER_HOUR * 60;
            int extraMinutes = totalMinutesSpent - minutesPaid;
            extraHours = extraMinutes / 60;
            if (extraMinutes % 60 > 0) {
                extraHours++;
            }
            ticket.setPayDate(new Timestamp(new Date().getTime()));
            ticket.setPaidAmount(ticket.getPaidAmount() + extraHours * PRICE_PER_HOUR);
            save(ticket);
        }
        return extraHours * PRICE_PER_HOUR;
    }

    public boolean hasExited(String code) {
        return getByCode(code).getExitDate() != null;
    }

    public boolean exitTicket(String code) {
        Ticket ticket = getByCode(code);
        Timestamp exitDate = new Timestamp(new Date().getTime());
        if (!hasToPayExtra(code)) {
            ticket.setExitDate(exitDate);
            save(ticket);
            return true;
        }
        return false;
    }
}

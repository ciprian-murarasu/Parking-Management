package com.portfolio.parkingmanagement.controller;

import com.portfolio.parkingmanagement.service.ParkingSpaceService;
import com.portfolio.parkingmanagement.service.SectorService;
import com.portfolio.parkingmanagement.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TicketController {
    private final ParkingSpaceService parkingSpaceService;
    private final TicketService ticketService;
    private final SectorService sectorService;

    @Autowired
    public TicketController(ParkingSpaceService parkingSpaceService, TicketService ticketService, SectorService sectorService) {
        this.parkingSpaceService = parkingSpaceService;
        this.ticketService = ticketService;
        this.sectorService = sectorService;
    }

//    @GetMapping("/tickets")
//    public String showTickets(Model model){
//        model.addAttribute(ticketService.)
//    }

    @GetMapping("/newTicket")
    public String buyTicket(Model model) {
        model.addAttribute("sectors", sectorService.getAllSectors());
        model.addAttribute("parkingSpaces", parkingSpaceService.getAllParkingSpaces());
        if (parkingSpaceService.hasAvailableSpaces()) {
            model.addAttribute("code", ticketService.generateTicket().getCode());
            return "parkingSpaces";
        } else {
            model.addAttribute("error_message", "Parking lot is full");
            return "index";
        }
    }
}

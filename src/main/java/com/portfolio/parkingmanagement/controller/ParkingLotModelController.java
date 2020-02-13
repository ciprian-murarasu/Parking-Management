package com.portfolio.parkingmanagement.controller;

import com.portfolio.parkingmanagement.service.ParkingSpaceService;
import com.portfolio.parkingmanagement.service.SectorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

@Controller
class ParkingLotModelController {
    private SectorService sectorService;
    private ParkingSpaceService parkingSpaceService;

    @Autowired
    public ParkingLotModelController(SectorService sectorService, ParkingSpaceService parkingSpaceService) {
        this.sectorService = sectorService;
        this.parkingSpaceService = parkingSpaceService;
    }

    String addModel(Model model, String message, String page) {
        if (!message.isEmpty()) {
            model.addAttribute("error_message", message);
        }
        model.addAttribute("sectors", sectorService.getAllSectors());
        model.addAttribute("parkingSpaces", parkingSpaceService.getAllParkingSpaces());
        return page;
    }
}

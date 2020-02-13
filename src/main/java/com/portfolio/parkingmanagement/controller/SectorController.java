package com.portfolio.parkingmanagement.controller;

import com.portfolio.parkingmanagement.model.Sector;
import com.portfolio.parkingmanagement.service.SectorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.NoSuchElementException;

@Controller
public class SectorController {
    private SectorService sectorService;

    @Autowired
    public SectorController(SectorService sectorService) {
        this.sectorService = sectorService;
    }

//    private String addModel(Model model, String message, String page) {
//        if (!message.isEmpty()) {
//            model.addAttribute("error_message", message);
//        }
//        model.addAttribute("sectors", sectorService.getAllSectors());
//        return page;
//    }

    @PostMapping("/delete-sector")
    public String deleteSector(HttpServletRequest request, Model model) {
        String selectedSector = request.getParameter("selected_sector");
        if (selectedSector.isEmpty()) {
            model.addAttribute("error_message", "You must select a sector to delete");
        } else {
            Sector sector = null;
            try {
                sector = sectorService.getById(Long.parseLong(selectedSector));
            } catch (NoSuchElementException ignored) {
            }
            if (sector == null) {
                model.addAttribute("error_message", "The sector you selected is not valid");
            } else if (sectorService.hasOccupiedSpaces(sector)) {
                model.addAttribute("error_message", "You can't delete sectors with occupied spaces");
            } else {
                sectorService.delete(sector);
            }
        }
        model.addAttribute("sectors", sectorService.getAllSectors());
        return "delete";
    }
}

package com.portfolio.parkingmanagement.controller;

import com.portfolio.parkingmanagement.model.ParkingSpace;
import com.portfolio.parkingmanagement.model.Sector;
import com.portfolio.parkingmanagement.service.ParkingSpaceService;
import com.portfolio.parkingmanagement.service.SectorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Iterator;
import java.util.Set;

@Controller
public class ParkingSpaceController {
    private SectorService sectorService;
    private ParkingSpaceService parkingSpaceService;

    @Autowired
    public ParkingSpaceController(SectorService sectorService, ParkingSpaceService parkingSpaceService) {
        this.sectorService = sectorService;
        this.parkingSpaceService = parkingSpaceService;
    }

    @GetMapping("/add-space")
    public String showAdd(Model model) {
        model.addAttribute("sectors", sectorService.getAllSectors());
        model.addAttribute("parkingSpaces", parkingSpaceService.getAllParkingSpaces());
        return "maintenance/addSpaces";
    }

    @PostMapping("/add-space")
    public String addSpace(HttpServletRequest request, Model model) {
        String newSectorName = request.getParameter("new_sector").toUpperCase();
        int numberOfNewSpaces = Integer.parseInt(request.getParameter("number_of_spaces"));
        Set<Sector> sectors = sectorService.getAllSectors();
        if (!newSectorName.isEmpty()) {
            boolean alreadyExists = false;
            for (Sector s : sectors) {
                if (newSectorName.equals(s.getName())) {
                    alreadyExists = true;
                    break;
                }
            }
            if (alreadyExists) {
                model.addAttribute("error_message", "Sector " + newSectorName + " already exists");
            } else {
                Sector sector = sectorService.createSector(newSectorName);
                for (int i = 1; i <= numberOfNewSpaces; i++) {
                    ParkingSpace space = new ParkingSpace(i, sector);
                    parkingSpaceService.save(space);
                    sector.addParkingSpace(space);
                }
                sectorService.save(sector);
            }
        } else {
            String selectedSector = request.getParameter("selected_sector");// If no sector is selected, it will be null, not empty string
            if (selectedSector == null) {
                model.addAttribute("error_message", "You must create a new sector or select an existing one");
            } else {
                Sector sector = sectorService.getById(Long.parseLong(selectedSector));
                Set<ParkingSpace> spaces = sector.getParkingSpaces();
                int firstNewSpaceNumber = 1;
                ParkingSpace lastExistingSpace = null;
                for (ParkingSpace p : spaces) {
                    lastExistingSpace = p;
                }
                if (lastExistingSpace != null) {
                    firstNewSpaceNumber += lastExistingSpace.getNumber();
                }
                for (int i = firstNewSpaceNumber; i < firstNewSpaceNumber + numberOfNewSpaces; i++) {
                    ParkingSpace space = new ParkingSpace(i, sector);
                    parkingSpaceService.save(space);
                    sector.addParkingSpace(space);
                }
                sectorService.save(sector);
            }
        }
        model.addAttribute("sectors", sectorService.getAllSectors());
        model.addAttribute("parkingSpaces", parkingSpaceService.getAllParkingSpaces());
        return "maintenance/addSpaces";
    }

    @GetMapping("/delete-space")
    public String showDelete(Model model) {
        model.addAttribute("sectors", sectorService.getAllSectors());
        model.addAttribute("parkingSpaces", parkingSpaceService.getAllParkingSpaces());
        return "maintenance/deleteSpaces";
    }

    @PostMapping("/delete-space")
    public String deleteSpace(HttpServletRequest request, Model model) {
        String deletedSpaces = request.getParameter("deleted_spaces");
        if (deletedSpaces.isEmpty()) {
            model.addAttribute("error_message", "You must select at least one space to delete");
        } else {
            String[] deletedSpacesArray = deletedSpaces.split(", ");
            for (String space : deletedSpacesArray) {
                String sectorName = Sector.extractSectorName(space);
                int parkingSpaceNumber = Integer.parseInt(space.substring(sectorName.length()));
                Sector sector = sectorService.getByName(sectorName);
                Iterator<ParkingSpace> iterator = null;
                try {
                    iterator = sector.getParkingSpaces().iterator();
                } catch (NullPointerException ignored) {
                }
                if (iterator == null) {
                    model.addAttribute("error_message", "Some spaces you selected are not valid");
                } else {
                    while (iterator.hasNext()) {
                        ParkingSpace ps = iterator.next();
                        if (ps.getNumber() == parkingSpaceNumber) {
                            iterator.remove();
                            sector.deleteParkingSpace(ps);
                            sectorService.save(sector);
                        }
                    }
                }
            }
        }
        model.addAttribute("sectors", sectorService.getAllSectors());
        model.addAttribute("parkingSpaces", parkingSpaceService.getAllParkingSpaces());
        return "maintenance/deleteSpaces";
    }
}

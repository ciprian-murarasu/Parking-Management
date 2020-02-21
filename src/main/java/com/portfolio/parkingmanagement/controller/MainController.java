package com.portfolio.parkingmanagement.controller;

import com.portfolio.parkingmanagement.model.*;
import com.portfolio.parkingmanagement.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;

@Controller
public class MainController {
    private SectorService sectorService;
    private TicketService ticketService;
    private SubscriptionService subscriptionService;
    private SubscriptionTypeService subscriptionTypeService;
    private ParkingSpaceService parkingSpaceService;
    private UserService userService;

    @Autowired
    public MainController(SectorService sectorService, TicketService ticketService, SubscriptionService subscriptionService,
                          SubscriptionTypeService subscriptionTypeService, ParkingSpaceService parkingSpaceService, UserService userService) {
        this.sectorService = sectorService;
        this.ticketService = ticketService;
        this.subscriptionService = subscriptionService;
        this.subscriptionTypeService = subscriptionTypeService;
        this.parkingSpaceService = parkingSpaceService;
        this.userService = userService;
    }

    @GetMapping("/")
    public String goToIndex(Model model) {
        model.addAttribute("sectors", sectorService.getAllSectors());
        model.addAttribute("parkingSpaces", parkingSpaceService.getAllParkingSpaces());
        if (!parkingSpaceService.hasAvailableSpaces()) {
            model.addAttribute("error_message", "Parking lot is full");
        }
        return "index";
    }

    @PostMapping("/")
    public String showIndex(HttpServletRequest request, Model model) {
        String selectedSpace = request.getParameter("selected_space");
        Sector selectedSector = sectorService.getByName(Sector.extractSectorName(selectedSpace));
        int selectedSpaceNumber = Integer.parseInt(selectedSpace.substring(selectedSector.getName().length()));
        Set<ParkingSpace> selectedSectorSpaces = selectedSector.getParkingSpaces();
        for (ParkingSpace p : selectedSectorSpaces) {
            if (p.getNumber() == selectedSpaceNumber) {
                p.setAvailability(false);
                String code = request.getParameter("code");
                if (ticketService.isValid(code)) {
                    p.setTicket(ticketService.getByCode(code));
                } else if (subscriptionService.isValid(code)) {
                    p.setSubscription(subscriptionService.getByCode(code));
                }
                parkingSpaceService.save(p);
                break;
            }
        }
        model.addAttribute("sectors", sectorService.getAllSectors());
        model.addAttribute("parkingSpaces", parkingSpaceService.getAllParkingSpaces());
        return "index";
    }

    @GetMapping("/buy")
    public String goToBuy(Model model) {
        model.addAttribute("subscriptionTypes", subscriptionTypeService.getAllSubscriptionTypes());
        return "buySubscription";
    }

    @GetMapping("/access")
    public String accessParking(Model model) {
        if (parkingSpaceService.hasAvailableSpaces()) {
            return "accessParking";
        } else {
            model.addAttribute("error_message", "Parking lot is full");
            model.addAttribute("sectors", sectorService.getAllSectors());
            model.addAttribute("parkingSpaces", parkingSpaceService.getAllParkingSpaces());
            return "index";
        }
    }

    @GetMapping("/parkingSpaces")
    public String showParkingSpaces(HttpServletRequest request, Model model) {
        String code = request.getParameter("code");
        String errorMessage = "";
        if (!subscriptionService.isValid(code)) {
            errorMessage = "Subscription code is not valid";
        } else if (!subscriptionService.hasStarted(code)) {
            errorMessage = "Your subscription can be used starting with " + subscriptionService.getByCode(code).getStartDate().toString().replace("T"," ");
        } else if (subscriptionService.hasExpired(code)) {
            errorMessage = "Your subscription has expired";
        } else if (subscriptionService.hasEntered(code)) {
            errorMessage = "This subscription is already using the parking lot";
        }
        if (!errorMessage.isEmpty()) {
            model.addAttribute("error_message", errorMessage);
            return "accessParking";
        }
        model.addAttribute("sectors", sectorService.getAllSectors());
        model.addAttribute("parkingSpaces", parkingSpaceService.getAllParkingSpaces());
        model.addAttribute("code", code);
        return "parkingSpaces";
    }

    @GetMapping("/pay")
    public String goToPay() {
        return "payTicket";
    }

    @PostMapping("/pay")
    public String payTicket(HttpServletRequest request, Model model) {
        String code = request.getParameter("code");
        String errorMessage = "";
        if (ticketService.isValid(code)) {
            if (ticketService.hasToPayExtra(code)) {
                model.addAttribute("paidExtra", ticketService.payExtra(code));
                model.addAttribute("ticket", ticketService.getByCode(code));
            } else {
                if (!ticketService.payTicket(code)) {
                    errorMessage = "Ticket is already paid. Proceed to exit";
                } else {
                    model.addAttribute("ticket", ticketService.getByCode(code));
                }
            }
        } else {
            errorMessage = "Ticket code is not valid";
        }
        if (!errorMessage.isEmpty()) {
            model.addAttribute("error_message", errorMessage);
        }
        return "payTicket";
    }

    @GetMapping("/exit")
    public String goToExit() {
        return "exitParking";
    }

    @PostMapping("/exit")
    public String processExit(HttpServletRequest request, Model model) {
        String code = request.getParameter("code");
        String errorMessage = "";
        if (ticketService.isValid(code)) {
            if (ticketService.hasExited(code)) {
                errorMessage = "Ticket was already used for exit";
            } else {
                if (!ticketService.isPaid(code)) {
                    errorMessage = "Please pay the ticket first";
                } else if (!ticketService.exitTicket(code)) {
                    errorMessage = "You exceeded 30 mins. Must pay extra to exit";
                } else {
                    if (ticketService.hasEntered(code)) {
                        ParkingSpace space = parkingSpaceService.getByTicketCode(code);
                        space.setAvailability(true);
                        space.setTicket(null);
                        parkingSpaceService.save(space);
                    }
                    model.addAttribute("ticket", ticketService.getByCode(code));
                }
            }
        } else if (subscriptionService.isValid(code)) {
            if (subscriptionService.hasExpired(code)) {
                errorMessage = "Your subscription has expired. A ticket was emitted to pay the difference";
                Subscription subscription = subscriptionService.getByCode(code);
                ParkingSpace parkingSpace = subscription.getParkingSpace();
                Ticket newTicket = ticketService.generateTicketForExpiredSubscription(subscription);
                newTicket.setParkingSpace(parkingSpace);
                ticketService.save(newTicket);
                parkingSpace.setTicket(newTicket);
                parkingSpace.setSubscription(null);
                parkingSpaceService.save(parkingSpace);
                model.addAttribute("ticket", newTicket);
            } else {
                ParkingSpace space = parkingSpaceService.getBySubscriptionCode(code);
                space.setAvailability(true);
                space.setSubscription(null);
                parkingSpaceService.save(space);
                model.addAttribute("subscription", subscriptionService.getByCode(code));
            }
        } else {
            errorMessage = "The code is not valid";
        }
        if (!errorMessage.isEmpty()) {
            model.addAttribute("error_message", errorMessage);
        }
        return "exitParking";
    }

    @GetMapping("/maintenance")
    public String goToMaintenance() {
        User user = userService.getByUsername("admin");
        if (user.isLogged()) {
            return "maintenance/maintenance";
        } else return "maintenance/login";
    }
}

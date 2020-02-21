package com.portfolio.parkingmanagement.controller;

import com.portfolio.parkingmanagement.model.Subscription;
import com.portfolio.parkingmanagement.model.SubscriptionType;
import com.portfolio.parkingmanagement.service.SubscriptionService;
import com.portfolio.parkingmanagement.service.SubscriptionTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
public class SubscriptionController {
    private final SubscriptionService subscriptionService;
    private final SubscriptionTypeService subscriptionTypeService;

    @Autowired
    public SubscriptionController(SubscriptionService subscriptionService, SubscriptionTypeService subscriptionTypeService) {
        this.subscriptionService = subscriptionService;
        this.subscriptionTypeService = subscriptionTypeService;
    }

    @GetMapping("/newSubscription")
    public String getSubscriptionType(Model model) {
        model.addAttribute("subscriptionTypes", subscriptionTypeService.getAllSubscriptionTypes());
        return "buySubscription";
    }

    @PostMapping("/newSubscription")
    public String buySubscription(HttpServletRequest request, Model model) {
        String purchaseType = request.getParameter("purchase_type");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime startDate = LocalDate.parse(request.getParameter("start_date"), formatter).atStartOfDay();
        String alertMessage = "";
        Subscription subscription;
        boolean isPurchased = false;
        if (purchaseType.equals("new")) {
            subscription = subscriptionService.generateSubscription();
            isPurchased = true;
        } else {
            String code = request.getParameter("subscription_code");
            subscription = subscriptionService.getByCode(code);
            if (subscription == null) {
                alertMessage = "Subscription code is not valid";
            } else if (!subscriptionService.hasExpired(code)) {
                alertMessage = "Subscription will expire on " + subscription.getEndDate().toString().substring(0, 10) + ", you can renew it after this date";
            } else {
                isPurchased = true;
            }
        }
        if (isPurchased) {
            alertMessage = "Your purchase was successful. You can see below the details";
            SubscriptionType subscriptionType = subscriptionTypeService.findByName(request.getParameter("subscription_type"));
            subscription.setSubscriptionType(subscriptionType);
            subscription.setPrice();
            subscription.setStartDate(startDate);
            subscription.setEndDate();
            subscriptionService.save(subscription);
            model.addAttribute("subscription", subscription);
        }
        model.addAttribute("alert_message", alertMessage);
        model.addAttribute("subscriptionTypes", subscriptionTypeService.getAllSubscriptionTypes());
        return "buySubscription";
    }
}

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
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
        return "newSubscription";
    }

    @PostMapping("/newSubscription")
    public String buySubscription(HttpServletRequest request, Model model) {
        String code = request.getParameter("subscription_code");
        String purchaseType = request.getParameter("purchase_type");
        String errorMessage;
        Subscription subscription;
        if (purchaseType.equals("new")) {
            subscription = subscriptionService.generateSubscription();
        } else {
            if (code.isEmpty()) {
                model.addAttribute("error_message", "Subscription code is empty");
                model.addAttribute("subscriptionTypes", subscriptionTypeService.getAllSubscriptionTypes());
                return "newSubscription";
            } else {
                subscription = subscriptionService.getByCode(code);
                if (subscription == null) {
                    errorMessage = "Subscription code is not valid";
                    model.addAttribute("error_message", errorMessage);
                    model.addAttribute("subscriptionTypes", subscriptionTypeService.getAllSubscriptionTypes());
                    return "newSubscription";
                }
            }
        }
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = formatter.parse(request.getParameter("start_date"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (date != null) {
            if (subscription.getEndDate() != null && !subscriptionService.isExpired(subscription.getCode())) {
                if (date.getTime() <= subscription.getEndDate().getTime()) {
                    errorMessage = "Please select a date later than " + subscription.getEndDate().toString();
                    model.addAttribute("error_message", errorMessage);
                    model.addAttribute("subscription", subscription);
                    model.addAttribute("subscriptionTypes", subscriptionTypeService.getAllSubscriptionTypes());
                    return "newSubscription";
                }
            }
            subscription.setStartDate(new Timestamp(date.getTime()));
        }
        SubscriptionType subscriptionType = subscriptionTypeService.findByName(request.getParameter("subscription_type"));
        subscription.setSubscriptionType(subscriptionType);
        subscription.setPrice();
        subscription.setEndDate();
        subscriptionService.save(subscription);
        model.addAttribute("subscription", subscription);
        model.addAttribute("subscriptionTypes", subscriptionTypeService.getAllSubscriptionTypes());
        return "newSubscription";
    }
}

package com.portfolio.parkingmanagement.controller;

import com.portfolio.parkingmanagement.model.User;
import com.portfolio.parkingmanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public String login(HttpServletRequest request, Model model) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String encryptedPassword = DigestUtils.md5DigestAsHex(password.getBytes());
        User user = userService.getByUsername(username);
        if (user == null || !encryptedPassword.equals(user.getPassword())) {
            model.addAttribute("error_message", "Incorrect username or password");
            return "login";
        }
        user.setLogged(true);
        userService.save(user);
        return "maintenance";
    }

    @GetMapping("/change-password")
    public String showChangePassword() {
        return "changePassword";
    }

    @PostMapping("/change-password")
    public String changePassword(HttpServletRequest request, Model model) {
        String currentPassword = request.getParameter("current_password");
        String newPassword = request.getParameter("new_password");
        String newPasswordConfirm = request.getParameter("new_password_confirm");
        User user = userService.getByUsername("admin");
        String encryptedCurrentPassword = DigestUtils.md5DigestAsHex(currentPassword.getBytes());
        if (!encryptedCurrentPassword.equals(user.getPassword())) {
            model.addAttribute("error_message", "Current password is invalid");
        } else if (!newPassword.equals(newPasswordConfirm)) {
            model.addAttribute("error_message", "New password fields do not match");
        } else if (currentPassword.equals(newPassword)) {
            model.addAttribute("error_message", "New password must be different than the old one");
        } else if (newPassword.length() < 5) {
            model.addAttribute("error_message", "New password must have at least 5 characters");
        } else {
            model.addAttribute("success_message", "Password successfully changed");
            user.setPassword(DigestUtils.md5DigestAsHex(newPassword.getBytes()));
            userService.save(user);
        }
        return "changePassword";
    }

    @GetMapping("/logout")
    public String logout() {
        User user = userService.getByUsername("admin");
        user.setLogged(false);
        userService.save(user);
        return "index";
    }
}
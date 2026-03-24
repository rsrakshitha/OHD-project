package com.ohd.ohd_project.controller;

import com.ohd.ohd_project.model.User;
import com.ohd.ohd_project.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class AdminController {

    @Autowired
    private UserRepository userRepository;
    
    @GetMapping("/admin/dashboard")
    public String adminDashboard(HttpSession session) {

        User user = (User) session.getAttribute("loggedInUser");

        if (user == null || !"ADMIN".equalsIgnoreCase(user.getRole())) {
            return "redirect:/login";
        }

        return "admin_dashboard";
    }

    @GetMapping("/admin/users")
    public String viewUsers(Model model, HttpSession session) {

        User user = (User) session.getAttribute("loggedInUser");

        if (user == null || !"ADMIN".equalsIgnoreCase(user.getRole())) {
            return "redirect:/login";
        }

        List<User> users = userRepository.findAll();
        System.out.println("Users size: " + users.size());
        model.addAttribute("users", users);
        return "view_users"; 
    }
}
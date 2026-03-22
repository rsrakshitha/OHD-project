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

    // ========================
    // ADMIN DASHBOARD
    // ========================
    @GetMapping("/admin/dashboard")
    public String adminDashboard(HttpSession session) {

        User user = (User) session.getAttribute("loggedInUser");

        // 🔒 Only admin can access
        if (user == null || !"ADMIN".equals(user.getRole())) {
            return "redirect:/login";
        }

        return "admin_dashboard";
    }

    // ========================
    // VIEW ALL USERS
    // ========================
    @GetMapping("/admin/users")
    public String viewUsers(Model model, HttpSession session) {

        User user = (User) session.getAttribute("loggedInUser");

        if (user == null || !"ADMIN".equals(user.getRole())) {
            return "redirect:/login";
        }

        List<User> users = userRepository.findAll();
        model.addAttribute("users", users);

        return "view_users";
    }
}
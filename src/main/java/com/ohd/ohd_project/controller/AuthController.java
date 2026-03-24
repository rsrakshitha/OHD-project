package com.ohd.ohd_project.controller;

import com.ohd.ohd_project.model.User;
import com.ohd.ohd_project.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController 
{

    @Autowired
    private UserRepository userRepository;

  
    @GetMapping("/register")
    public String showRegister() 
    {
        return "register";
    }


    @PostMapping("/register")
    public String register(@ModelAttribute User user, Model model) 
    {

        try {
            if (user.getRole() == null || user.getRole().isEmpty()) 
            {
                user.setRole("STUDENT");
            }

            userRepository.save(user);

            return "redirect:/login";

        } catch (Exception e) 
        {
            model.addAttribute("error", "Registration failed");
            return "register";
        }
    }


    @GetMapping("/login")
    public String showLogin() 
    {
        return "login";
    }

 
    @PostMapping("/login")
    public String login(@RequestParam String email,
                        @RequestParam String password,
                        Model model,
                        HttpSession session) 
    {

        User user = userRepository.findByEmail(email);

        if (user == null || !password.equals(user.getPassword())) 
        {
            model.addAttribute("error", "Invalid email or password");
            return "login";
        }

        session.setAttribute("loggedInUser", user);
        session.setAttribute("role", user.getRole());

        if ("ADMIN".equals(user.getRole())) 
        {
            return "redirect:/dashboard";
        } 
        else if ("FACULTY".equals(user.getRole())) 
        {
            return "redirect:/home";
        } 
        else {
            return "redirect:/home";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) 
    {
        session.invalidate();
        return "redirect:/login";
    }

    @GetMapping("/change-password")
    public String showChangePasswordPage() {
        return "change-password";
    }


    @PostMapping("/change-password")
    public String changePassword(@RequestParam String oldPassword,
                                 @RequestParam String newPassword,
                                 @RequestParam String confirmPassword,
                                 HttpSession session,
                                 Model model) {

        User user = (User) session.getAttribute("loggedInUser");

        if (user == null) {
            return "redirect:/login";
        }

        if (!user.getPassword().equals(oldPassword)) {
            model.addAttribute("error", "Old password is incorrect");
            return "change-password";
        }

        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("error", "Passwords do not match");
            return "change-password";
        }

        user.setPassword(newPassword);
        userRepository.save(user);

        model.addAttribute("success", "Password changed successfully");

        return "change-password";
    }
}
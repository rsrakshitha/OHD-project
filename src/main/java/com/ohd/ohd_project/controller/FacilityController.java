package com.ohd.ohd_project.controller;

import com.ohd.ohd_project.model.Facility;
import com.ohd.ohd_project.model.User;
import com.ohd.ohd_project.repository.FacilityRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class FacilityController {

    @Autowired
    private FacilityRepository facilityRepository;

   
    @GetMapping("/admin/facilities")
    public String viewFacilities(Model model, HttpSession session) {

        User user = (User) session.getAttribute("loggedInUser");

        if (user == null || !"ADMIN".equals(user.getRole())) {
            return "redirect:/login";
        }

        model.addAttribute("facilities", facilityRepository.findAll());
        return "facilities";
    }

    @GetMapping("/admin/facilities/add")
    public String showAddForm(Model model, HttpSession session) {

        User user = (User) session.getAttribute("loggedInUser");

        if (user == null || !"ADMIN".equals(user.getRole())) {
            return "redirect:/login";
        }

        model.addAttribute("facility", new Facility());
        return "add-facility";
    }

    @PostMapping("/admin/facilities/save")
    public String saveFacility(@ModelAttribute Facility facility, HttpSession session) {

        User user = (User) session.getAttribute("loggedInUser");

        if (user == null || !"ADMIN".equals(user.getRole())) {
            return "redirect:/login";
        }

        facilityRepository.save(facility);
        return "redirect:/admin/facilities";
    }

    @GetMapping("/admin/facilities/delete/{id}")
    public String deleteFacility(@PathVariable Long id, HttpSession session) {

        User user = (User) session.getAttribute("loggedInUser");

        if (user == null || !"ADMIN".equals(user.getRole())) {
            return "redirect:/login";
        }

        facilityRepository.deleteById(id);
        return "redirect:/admin/facilities";
    }

    @GetMapping("/admin/facilities/edit/{id}")
    public String editFacility(@PathVariable Long id, Model model, HttpSession session) {

        User user = (User) session.getAttribute("loggedInUser");

        if (user == null || !"ADMIN".equals(user.getRole())) {
            return "redirect:/login";
        }

        Facility facility = facilityRepository.findById(id).orElse(null);
        model.addAttribute("facility", facility);

        return "add-facility";
    }
}
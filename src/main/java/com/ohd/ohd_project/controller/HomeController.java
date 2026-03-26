package com.ohd.ohd_project.controller;

import com.ohd.ohd_project.model.Request;
import com.ohd.ohd_project.model.User;
import com.ohd.ohd_project.repository.RequestRepository;
import com.ohd.ohd_project.service.EmailService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class HomeController {

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private EmailService emailService;

    private final String ADMIN_EMAIL = "rsrakshitha17@gmail.com";

    private void notifyUserAndAdmin(Request r, String subject, String message) {
        emailService.sendEmail(ADMIN_EMAIL, subject, message);
        if (r.getCreatedBy() != null) {
            emailService.sendEmail(r.getCreatedBy().getEmail(), subject, message);
        }
    }

    @GetMapping("/")
    public String index() {
        return "login";
    }

    @GetMapping("/home")
    public String home(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) return "redirect:/";
        model.addAttribute("user", user);
        return "index";
    }

    @GetMapping("/create")
    public String createForm(HttpSession session) {
        if (session.getAttribute("loggedInUser") == null) return "redirect:/";
        return "create";
    }

    @PostMapping("/save")
    public String saveRequest(@ModelAttribute Request request, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) return "redirect:/";
        request.setAssignedTo(null);
        request.setStatus("UNASSIGNED");
        request.setCreatedBy(user);
        requestRepository.save(request);
        notifyUserAndAdmin(request, "Request Created", "Request created:\nTitle: " + request.getTitle());
        return "redirect:/list";
    }

    @GetMapping("/list")
    public String viewRequests(Model model, HttpSession session, @RequestParam(required = false) String status) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) return "redirect:/";
        if ("ADMIN".equals(user.getRole())) {
            if (status == null || status.isEmpty()) model.addAttribute("requests", requestRepository.findAll());
            else model.addAttribute("requests", requestRepository.findByStatus(status));
        } else {
            if (status == null || status.isEmpty()) model.addAttribute("requests", requestRepository.findByCreatedBy(user));
            else model.addAttribute("requests",
                    requestRepository.findByStatus(status).stream()
                            .filter(r -> r.getCreatedBy() != null && r.getCreatedBy().getId() == user.getId())
                            .toList()
            );
        }
        return "request-list";
    }

    @GetMapping("/updateStatus/{id}")
    public String updateStatus(@PathVariable int id) {
        Request r = requestRepository.findById(id).orElse(null);
        if (r != null) {
            if ("UNASSIGNED".equals(r.getStatus())) r.setStatus("ASSIGNED");
            else if ("ASSIGNED".equals(r.getStatus())) r.setStatus("IN_PROGRESS");
            else if ("IN_PROGRESS".equals(r.getStatus())) r.setStatus("CLOSED");
            requestRepository.save(r);
            notifyUserAndAdmin(r, "Status Updated", "Request '" + r.getTitle() + "' updated to: " + r.getStatus());
        }
        return "redirect:/list";
    }

    @GetMapping("/assign/{id}")
    public String assignRequest(@PathVariable int id, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        Request r = requestRepository.findById(id).orElse(null);
        if (user != null && r != null) {
            r.setAssignedTo(user.getName());
            r.setStatus("ASSIGNED");
            requestRepository.save(r);
            notifyUserAndAdmin(r, "Request Assigned", "Request '" + r.getTitle() + "' assigned to: " + user.getName());
        }
        return "redirect:/list";
    }

    @GetMapping("/close/{id}")
    public String closeRequest(@PathVariable int id) {
        Request r = requestRepository.findById(id).orElse(null);
        if (r != null) {
            r.setStatus("CLOSED");
            r.setCloseReason("Issue resolved");
            requestRepository.save(r);
            notifyUserAndAdmin(r, "Request Closed", "Request '" + r.getTitle() + "' has been CLOSED.");
        }
        return "redirect:/list";
    }

    @GetMapping("/reject/{id}")
    public String rejectRequest(@PathVariable int id) {
        Request r = requestRepository.findById(id).orElse(null);
        if (r != null) {
            r.setStatus("REJECTED");
            requestRepository.save(r);
            notifyUserAndAdmin(r, "Request Rejected", "Request '" + r.getTitle() + "' has been REJECTED.");
        }
        return "redirect:/list";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, HttpSession session) {
        if (session.getAttribute("loggedInUser") == null) return "redirect:/";
        model.addAttribute("total", requestRepository.count());
        model.addAttribute("open", requestRepository.countByStatusNot("CLOSED"));
        model.addAttribute("closed", requestRepository.countByStatus("CLOSED"));
        model.addAttribute("rejected", requestRepository.countByStatus("REJECTED"));
        return "dashboard";
    }

    @GetMapping("/generateReport")
    public String generateReport(HttpSession session) {
        if (session.getAttribute("loggedInUser") == null) return "redirect:/";
        long total = requestRepository.count();
        long closed = requestRepository.countByStatus("CLOSED");
        long open = requestRepository.countByStatusNot("CLOSED");
        String report = "Report:\n\nTotal: " + total + "\nOpen: " + open + "\nClosed: " + closed;
        emailService.sendEmail(ADMIN_EMAIL, "Report", report);
        return "redirect:/dashboard";
    }

    @GetMapping("/help")
    public String help(HttpSession session) {
        if (session.getAttribute("loggedInUser") == null) return "redirect:/";
        return "help";
    }
}

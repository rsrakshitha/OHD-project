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

    @GetMapping("/")
    public String index() {
        return "login";
    }

    @GetMapping("/home")
    public String home(HttpSession session, Model model) {

        User user = (User) session.getAttribute("loggedInUser");

        if (user == null) {
            return "redirect:/";
        }

        model.addAttribute("user", user);
        return "index";
    }

    @GetMapping("/create")
    public String createForm(HttpSession session) {

        User user = (User) session.getAttribute("loggedInUser");

        if (user == null) {
            return "redirect:/";
        }

        return "create";
    }

    @PostMapping("/save")
    public String saveRequest(@ModelAttribute Request request, HttpSession session) {

        User user = (User) session.getAttribute("loggedInUser");

        if (user == null) {
            return "redirect:/";
        }

        request.setAssignedTo(null);
        request.setStatus("UNASSIGNED");
        request.setCreatedBy(user);

        requestRepository.save(request);

        emailService.sendEmail(
                user.getEmail(),
                "Request Created",
                "Your request has been created.\nTitle: " + request.getTitle()
        );

        return "redirect:/list";
    }

    @GetMapping("/list")
    public String viewRequests(Model model,
                               HttpSession session,
                               @RequestParam(required = false) String status) {

        User user = (User) session.getAttribute("loggedInUser");

        if (user == null) {
            return "redirect:/";
        }

        if ("ADMIN".equals(user.getRole())) {

            if (status == null || status.isEmpty()) {
                model.addAttribute("requests", requestRepository.findAll());
            } else {
                model.addAttribute("requests", requestRepository.findByStatus(status));
            }

        } else {

            if (status == null || status.isEmpty()) {
                model.addAttribute("requests", requestRepository.findByCreatedBy(user));
            } else {
                model.addAttribute("requests",
                        requestRepository.findByStatus(status)
                                .stream()
                                .filter(r -> r.getCreatedBy() != null &&
                                        r.getCreatedBy().getId() == user.getId())
                                .toList()
                );
            }
        }

        return "request-list";
    }

    @GetMapping("/updateStatus/{id}")
    public String updateStatus(@PathVariable int id) {

        Request r = requestRepository.findById(id).orElse(null);

        if (r != null) {

            if ("UNASSIGNED".equals(r.getStatus())) {
                r.setStatus("ASSIGNED");
            } else if ("ASSIGNED".equals(r.getStatus())) {
                r.setStatus("IN_PROGRESS");
            } else if ("IN_PROGRESS".equals(r.getStatus())) {
                r.setStatus("CLOSED");
            }

            requestRepository.save(r);

            emailService.sendEmail(
                    ADMIN_EMAIL,
                    "Status Updated",
                    "Request status updated to: " + r.getStatus()
            );
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

            emailService.sendEmail(
                    user.getEmail(),
                    "Request Assigned",
                    "You have been assigned: " + r.getTitle()
            );
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

            emailService.sendEmail(
                    ADMIN_EMAIL,
                    "Request Closed",
                    "Request closed: " + r.getTitle()
            );
        }

        return "redirect:/list";
    }

    @GetMapping("/reject/{id}")
    public String rejectRequest(@PathVariable int id) {

        Request r = requestRepository.findById(id).orElse(null);

        if (r != null) {

            r.setStatus("REJECTED");

            requestRepository.save(r);

            emailService.sendEmail(
                    ADMIN_EMAIL,
                    "Request Rejected",
                    "Request rejected: " + r.getTitle()
            );
        }

        return "redirect:/list";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, HttpSession session) {

        if (session.getAttribute("loggedInUser") == null) {
            return "redirect:/";
        }

        long total = requestRepository.count();
        long open = requestRepository.countByStatusNot("CLOSED");
        long closed = requestRepository.countByStatus("CLOSED");
        long rejected = requestRepository.countByStatus("REJECTED");

        model.addAttribute("total", total);
        model.addAttribute("open", open);
        model.addAttribute("closed", closed);
        model.addAttribute("rejected", rejected);

        return "dashboard";
    }

    @GetMapping("/generateReport")
    public String generateReport(HttpSession session) {

        User user = (User) session.getAttribute("loggedInUser");

        if (user == null) {
            return "redirect:/";
        }

        long total = requestRepository.count();

        long closed = requestRepository.findAll()
                .stream()
                .filter(r -> "CLOSED".equalsIgnoreCase(r.getStatus()))
                .count();

        long open = requestRepository.findAll()
                .stream()
                .filter(r -> !"CLOSED".equalsIgnoreCase(r.getStatus()))
                .count();

        String report = "Manual Report:\n\n"
                + "Total Requests: " + total + "\n"
                + "Open Requests: " + open + "\n"
                + "Closed Requests: " + closed;

        emailService.sendEmail(
                ADMIN_EMAIL,
                "Manual Report",
                report
        );

        return "redirect:/dashboard";
    }

    @GetMapping("/help")
    public String help(HttpSession session) {

        if (session.getAttribute("loggedInUser") == null) {
            return "redirect:/";
        }

        return "help";
    }

    @GetMapping("/reports")
    public String reports(Model model, HttpSession session) {

        String role = (String) session.getAttribute("role");

        if (role == null || !role.equals("ADMIN")) {
            return "redirect:/home";
        }

        model.addAttribute("requests", requestRepository.findAll());

        return "reports";
    }

    @GetMapping("/test")
    @ResponseBody
    public String test() {
        return "Controller is working!";
    }
}
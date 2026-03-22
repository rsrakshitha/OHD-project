package com.ohd.ohd_project.scheduler;

import com.ohd.ohd_project.repository.RequestRepository;
import com.ohd.ohd_project.service.EmailService;

import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.scheduling.annotation.Scheduled;  // ❌ kept commented
import org.springframework.stereotype.Service;

@Service
public class ReportScheduler {

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private EmailService emailService;

    // ============================
    // SCHEDULER DISABLED
    // ============================

    /*
    // For testing (every 1 minute)
    @Scheduled(fixedRate = 60000)

    // For daily (9 AM)
    @Scheduled(cron = "0 0 9 * * ?")
    */

    public void sendDailyReport() {

        long total = requestRepository.count();

        long closed = requestRepository.findAll()
                .stream()
                .filter(r -> "CLOSED".equalsIgnoreCase(r.getStatus()))
                .count();

        long open = requestRepository.findAll()
                .stream()
                .filter(r -> !"CLOSED".equalsIgnoreCase(r.getStatus()))
                .count();

        StringBuilder report = new StringBuilder();

        report.append("Daily Report:\n\n");
        report.append("Total Requests: ").append(total).append("\n");
        report.append("Open Requests: ").append(open).append("\n");
        report.append("Closed Requests: ").append(closed).append("\n");

        emailService.sendEmail(
                "rsrakshitha17@gmail.com",
                "Daily Report",
                report.toString()
        );

        System.out.println("Report sent manually");
    }
}
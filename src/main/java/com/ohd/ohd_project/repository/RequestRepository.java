package com.ohd.ohd_project.repository;

import com.ohd.ohd_project.model.Request;
import com.ohd.ohd_project.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Integer> {

    // Find requests by status
    List<Request> findByStatus(String status);

    // Count by status
    long countByStatus(String status);

    // Count where status is NOT equal
    long countByStatusNot(String status);

    // Get requests created by a specific user
    List<Request> findByCreatedBy(User user);

    // ✅ OPTIONAL (better filtering: user + status)
    List<Request> findByCreatedByAndStatus(User user, String status);
}
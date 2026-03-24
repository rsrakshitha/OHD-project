package com.ohd.ohd_project.repository;

import com.ohd.ohd_project.model.Request;
import com.ohd.ohd_project.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Integer> {

    List<Request> findByStatus(String status);
    long countByStatus(String status);
    long countByStatusNot(String status);
    List<Request> findByCreatedBy(User user);
    List<Request> findByCreatedByAndStatus(User user, String status);
}
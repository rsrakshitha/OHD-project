package com.ohd.ohd_project.repository;

import com.ohd.ohd_project.model.Facility;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FacilityRepository extends JpaRepository<Facility, Long> {
}
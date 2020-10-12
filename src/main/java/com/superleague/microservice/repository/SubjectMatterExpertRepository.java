package com.superleague.microservice.repository;

import com.superleague.microservice.domain.SubjectMatterExpert;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the SubjectMatterExpert entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SubjectMatterExpertRepository extends JpaRepository<SubjectMatterExpert, Long> {
}

package com.superleague.microservice.repository;

import com.superleague.microservice.domain.Learning;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Learning entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LearningRepository extends JpaRepository<Learning, Long> {
}

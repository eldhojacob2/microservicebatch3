package com.superleague.microservice.web.rest;

import com.superleague.microservice.domain.Learning;
import com.superleague.microservice.repository.LearningRepository;
import com.superleague.microservice.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.superleague.microservice.domain.Learning}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class LearningResource {

    private final Logger log = LoggerFactory.getLogger(LearningResource.class);

    private static final String ENTITY_NAME = "superleagueLearning";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LearningRepository learningRepository;

    public LearningResource(LearningRepository learningRepository) {
        this.learningRepository = learningRepository;
    }

    /**
     * {@code POST  /learnings} : Create a new learning.
     *
     * @param learning the learning to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new learning, or with status {@code 400 (Bad Request)} if the learning has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/learnings")
    public ResponseEntity<Learning> createLearning(@RequestBody Learning learning) throws URISyntaxException {
        log.debug("REST request to save Learning : {}", learning);
        if (learning.getId() != null) {
            throw new BadRequestAlertException("A new learning cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Learning result = learningRepository.save(learning);
        return ResponseEntity.created(new URI("/api/learnings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /learnings} : Updates an existing learning.
     *
     * @param learning the learning to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated learning,
     * or with status {@code 400 (Bad Request)} if the learning is not valid,
     * or with status {@code 500 (Internal Server Error)} if the learning couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/learnings")
    public ResponseEntity<Learning> updateLearning(@RequestBody Learning learning) throws URISyntaxException {
        log.debug("REST request to update Learning : {}", learning);
        if (learning.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Learning result = learningRepository.save(learning);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, learning.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /learnings} : get all the learnings.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of learnings in body.
     */
    @GetMapping("/learnings")
    public List<Learning> getAllLearnings() {
        log.debug("REST request to get all Learnings");
        return learningRepository.findAll();
    }

    /**
     * {@code GET  /learnings/:id} : get the "id" learning.
     *
     * @param id the id of the learning to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the learning, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/learnings/{id}")
    public ResponseEntity<Learning> getLearning(@PathVariable Long id) {
        log.debug("REST request to get Learning : {}", id);
        Optional<Learning> learning = learningRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(learning);
    }

    /**
     * {@code DELETE  /learnings/:id} : delete the "id" learning.
     *
     * @param id the id of the learning to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/learnings/{id}")
    public ResponseEntity<Void> deleteLearning(@PathVariable Long id) {
        log.debug("REST request to delete Learning : {}", id);
        learningRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}

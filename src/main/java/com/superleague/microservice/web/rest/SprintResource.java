package com.superleague.microservice.web.rest;

import com.superleague.microservice.domain.Sprint;
import com.superleague.microservice.repository.SprintRepository;
import com.superleague.microservice.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.superleague.microservice.domain.Sprint}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class SprintResource {

    private final Logger log = LoggerFactory.getLogger(SprintResource.class);

    private static final String ENTITY_NAME = "superleagueSprint";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SprintRepository sprintRepository;

    public SprintResource(SprintRepository sprintRepository) {
        this.sprintRepository = sprintRepository;
    }

    /**
     * {@code POST  /sprints} : Create a new sprint.
     *
     * @param sprint the sprint to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new sprint, or with status {@code 400 (Bad Request)} if the sprint has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/sprints")
    public ResponseEntity<Sprint> createSprint(@Valid @RequestBody Sprint sprint) throws URISyntaxException {
        log.debug("REST request to save Sprint : {}", sprint);
        if (sprint.getId() != null) {
            throw new BadRequestAlertException("A new sprint cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Sprint result = sprintRepository.save(sprint);
        return ResponseEntity.created(new URI("/api/sprints/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /sprints} : Updates an existing sprint.
     *
     * @param sprint the sprint to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sprint,
     * or with status {@code 400 (Bad Request)} if the sprint is not valid,
     * or with status {@code 500 (Internal Server Error)} if the sprint couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/sprints")
    public ResponseEntity<Sprint> updateSprint(@Valid @RequestBody Sprint sprint) throws URISyntaxException {
        log.debug("REST request to update Sprint : {}", sprint);
        if (sprint.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Sprint result = sprintRepository.save(sprint);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, sprint.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /sprints} : get all the sprints.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of sprints in body.
     */
    @GetMapping("/sprints")
    public List<Sprint> getAllSprints() {
        log.debug("REST request to get all Sprints");
        return sprintRepository.findAll();
    }

    /**
     * {@code GET  /sprints/:id} : get the "id" sprint.
     *
     * @param id the id of the sprint to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the sprint, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/sprints/{id}")
    public ResponseEntity<Sprint> getSprint(@PathVariable Long id) {
        log.debug("REST request to get Sprint : {}", id);
        Optional<Sprint> sprint = sprintRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(sprint);
    }

    /**
     * {@code DELETE  /sprints/:id} : delete the "id" sprint.
     *
     * @param id the id of the sprint to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/sprints/{id}")
    public ResponseEntity<Void> deleteSprint(@PathVariable Long id) {
        log.debug("REST request to delete Sprint : {}", id);
        sprintRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}

package com.superleague.microservice.web.rest;

import com.superleague.microservice.domain.SubjectMatterExpert;
import com.superleague.microservice.repository.SubjectMatterExpertRepository;
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
 * REST controller for managing {@link com.superleague.microservice.domain.SubjectMatterExpert}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class SubjectMatterExpertResource {

    private final Logger log = LoggerFactory.getLogger(SubjectMatterExpertResource.class);

    private static final String ENTITY_NAME = "superleagueSubjectMatterExpert";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SubjectMatterExpertRepository subjectMatterExpertRepository;

    public SubjectMatterExpertResource(SubjectMatterExpertRepository subjectMatterExpertRepository) {
        this.subjectMatterExpertRepository = subjectMatterExpertRepository;
    }

    /**
     * {@code POST  /subject-matter-experts} : Create a new subjectMatterExpert.
     *
     * @param subjectMatterExpert the subjectMatterExpert to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new subjectMatterExpert, or with status {@code 400 (Bad Request)} if the subjectMatterExpert has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/subject-matter-experts")
    public ResponseEntity<SubjectMatterExpert> createSubjectMatterExpert(@Valid @RequestBody SubjectMatterExpert subjectMatterExpert) throws URISyntaxException {
        log.debug("REST request to save SubjectMatterExpert : {}", subjectMatterExpert);
        if (subjectMatterExpert.getId() != null) {
            throw new BadRequestAlertException("A new subjectMatterExpert cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SubjectMatterExpert result = subjectMatterExpertRepository.save(subjectMatterExpert);
        return ResponseEntity.created(new URI("/api/subject-matter-experts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /subject-matter-experts} : Updates an existing subjectMatterExpert.
     *
     * @param subjectMatterExpert the subjectMatterExpert to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated subjectMatterExpert,
     * or with status {@code 400 (Bad Request)} if the subjectMatterExpert is not valid,
     * or with status {@code 500 (Internal Server Error)} if the subjectMatterExpert couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/subject-matter-experts")
    public ResponseEntity<SubjectMatterExpert> updateSubjectMatterExpert(@Valid @RequestBody SubjectMatterExpert subjectMatterExpert) throws URISyntaxException {
        log.debug("REST request to update SubjectMatterExpert : {}", subjectMatterExpert);
        if (subjectMatterExpert.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SubjectMatterExpert result = subjectMatterExpertRepository.save(subjectMatterExpert);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, subjectMatterExpert.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /subject-matter-experts} : get all the subjectMatterExperts.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of subjectMatterExperts in body.
     */
    @GetMapping("/subject-matter-experts")
    public List<SubjectMatterExpert> getAllSubjectMatterExperts() {
        log.debug("REST request to get all SubjectMatterExperts");
        return subjectMatterExpertRepository.findAll();
    }

    /**
     * {@code GET  /subject-matter-experts/:id} : get the "id" subjectMatterExpert.
     *
     * @param id the id of the subjectMatterExpert to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the subjectMatterExpert, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/subject-matter-experts/{id}")
    public ResponseEntity<SubjectMatterExpert> getSubjectMatterExpert(@PathVariable Long id) {
        log.debug("REST request to get SubjectMatterExpert : {}", id);
        Optional<SubjectMatterExpert> subjectMatterExpert = subjectMatterExpertRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(subjectMatterExpert);
    }

    /**
     * {@code DELETE  /subject-matter-experts/:id} : delete the "id" subjectMatterExpert.
     *
     * @param id the id of the subjectMatterExpert to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/subject-matter-experts/{id}")
    public ResponseEntity<Void> deleteSubjectMatterExpert(@PathVariable Long id) {
        log.debug("REST request to delete SubjectMatterExpert : {}", id);
        subjectMatterExpertRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}

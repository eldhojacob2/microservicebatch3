package com.superleague.microservice.web.rest;

import com.superleague.microservice.domain.Batch;
import com.superleague.microservice.repository.BatchRepository;
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
 * REST controller for managing {@link com.superleague.microservice.domain.Batch}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class BatchResource {

    private final Logger log = LoggerFactory.getLogger(BatchResource.class);

    private static final String ENTITY_NAME = "superleagueBatch";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BatchRepository batchRepository;

    public BatchResource(BatchRepository batchRepository) {
        this.batchRepository = batchRepository;
    }

    /**
     * {@code POST  /batches} : Create a new batch.
     *
     * @param batch the batch to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new batch, or with status {@code 400 (Bad Request)} if the batch has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/batches")
    public ResponseEntity<Batch> createBatch(@Valid @RequestBody Batch batch) throws URISyntaxException {
        log.debug("REST request to save Batch : {}", batch);
        if (batch.getId() != null) {
            throw new BadRequestAlertException("A new batch cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Batch result = batchRepository.save(batch);
        return ResponseEntity.created(new URI("/api/batches/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /batches} : Updates an existing batch.
     *
     * @param batch the batch to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated batch,
     * or with status {@code 400 (Bad Request)} if the batch is not valid,
     * or with status {@code 500 (Internal Server Error)} if the batch couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/batches")
    public ResponseEntity<Batch> updateBatch(@Valid @RequestBody Batch batch) throws URISyntaxException {
        log.debug("REST request to update Batch : {}", batch);
        if (batch.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Batch result = batchRepository.save(batch);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, batch.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /batches} : get all the batches.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of batches in body.
     */
    @GetMapping("/batches")
    public List<Batch> getAllBatches() {
        log.debug("REST request to get all Batches");
        return batchRepository.findAll();
    }

    /**
     * {@code GET  /batches/:id} : get the "id" batch.
     *
     * @param id the id of the batch to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the batch, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/batches/{id}")
    public ResponseEntity<Batch> getBatch(@PathVariable Long id) {
        log.debug("REST request to get Batch : {}", id);
        Optional<Batch> batch = batchRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(batch);
    }

    /**
     * {@code DELETE  /batches/:id} : delete the "id" batch.
     *
     * @param id the id of the batch to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/batches/{id}")
    public ResponseEntity<Void> deleteBatch(@PathVariable Long id) {
        log.debug("REST request to delete Batch : {}", id);
        batchRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}

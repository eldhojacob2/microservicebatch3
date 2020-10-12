package com.superleague.microservice.web.rest;

import com.superleague.microservice.SuperleagueApp;
import com.superleague.microservice.config.TestSecurityConfiguration;
import com.superleague.microservice.domain.Batch;
import com.superleague.microservice.repository.BatchRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.superleague.microservice.domain.enumeration.Status;
/**
 * Integration tests for the {@link BatchResource} REST controller.
 */
@SpringBootTest(classes = { SuperleagueApp.class, TestSecurityConfiguration.class })
@AutoConfigureMockMvc
@WithMockUser
public class BatchResourceIT {

    private static final String DEFAULT_BATCH_NO = "AAAAAAAAAA";
    private static final String UPDATED_BATCH_NO = "BBBBBBBBBB";

    private static final String DEFAULT_BATCH_NAME = "AAAAAAAAAA";
    private static final String UPDATED_BATCH_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_PARTICIPANT_COUNT = 1;
    private static final Integer UPDATED_PARTICIPANT_COUNT = 2;

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Status DEFAULT_STATUS = Status.PLANNED;
    private static final Status UPDATED_STATUS = Status.INPROGRESS;

    @Autowired
    private BatchRepository batchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBatchMockMvc;

    private Batch batch;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Batch createEntity(EntityManager em) {
        Batch batch = new Batch()
            .batchNo(DEFAULT_BATCH_NO)
            .batchName(DEFAULT_BATCH_NAME)
            .participantCount(DEFAULT_PARTICIPANT_COUNT)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .status(DEFAULT_STATUS);
        return batch;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Batch createUpdatedEntity(EntityManager em) {
        Batch batch = new Batch()
            .batchNo(UPDATED_BATCH_NO)
            .batchName(UPDATED_BATCH_NAME)
            .participantCount(UPDATED_PARTICIPANT_COUNT)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .status(UPDATED_STATUS);
        return batch;
    }

    @BeforeEach
    public void initTest() {
        batch = createEntity(em);
    }

    @Test
    @Transactional
    public void createBatch() throws Exception {
        int databaseSizeBeforeCreate = batchRepository.findAll().size();
        // Create the Batch
        restBatchMockMvc.perform(post("/api/batches").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(batch)))
            .andExpect(status().isCreated());

        // Validate the Batch in the database
        List<Batch> batchList = batchRepository.findAll();
        assertThat(batchList).hasSize(databaseSizeBeforeCreate + 1);
        Batch testBatch = batchList.get(batchList.size() - 1);
        assertThat(testBatch.getBatchNo()).isEqualTo(DEFAULT_BATCH_NO);
        assertThat(testBatch.getBatchName()).isEqualTo(DEFAULT_BATCH_NAME);
        assertThat(testBatch.getParticipantCount()).isEqualTo(DEFAULT_PARTICIPANT_COUNT);
        assertThat(testBatch.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testBatch.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testBatch.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void createBatchWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = batchRepository.findAll().size();

        // Create the Batch with an existing ID
        batch.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBatchMockMvc.perform(post("/api/batches").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(batch)))
            .andExpect(status().isBadRequest());

        // Validate the Batch in the database
        List<Batch> batchList = batchRepository.findAll();
        assertThat(batchList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkBatchNoIsRequired() throws Exception {
        int databaseSizeBeforeTest = batchRepository.findAll().size();
        // set the field null
        batch.setBatchNo(null);

        // Create the Batch, which fails.


        restBatchMockMvc.perform(post("/api/batches").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(batch)))
            .andExpect(status().isBadRequest());

        List<Batch> batchList = batchRepository.findAll();
        assertThat(batchList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkBatchNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = batchRepository.findAll().size();
        // set the field null
        batch.setBatchName(null);

        // Create the Batch, which fails.


        restBatchMockMvc.perform(post("/api/batches").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(batch)))
            .andExpect(status().isBadRequest());

        List<Batch> batchList = batchRepository.findAll();
        assertThat(batchList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllBatches() throws Exception {
        // Initialize the database
        batchRepository.saveAndFlush(batch);

        // Get all the batchList
        restBatchMockMvc.perform(get("/api/batches?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(batch.getId().intValue())))
            .andExpect(jsonPath("$.[*].batchNo").value(hasItem(DEFAULT_BATCH_NO)))
            .andExpect(jsonPath("$.[*].batchName").value(hasItem(DEFAULT_BATCH_NAME)))
            .andExpect(jsonPath("$.[*].participantCount").value(hasItem(DEFAULT_PARTICIPANT_COUNT)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }
    
    @Test
    @Transactional
    public void getBatch() throws Exception {
        // Initialize the database
        batchRepository.saveAndFlush(batch);

        // Get the batch
        restBatchMockMvc.perform(get("/api/batches/{id}", batch.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(batch.getId().intValue()))
            .andExpect(jsonPath("$.batchNo").value(DEFAULT_BATCH_NO))
            .andExpect(jsonPath("$.batchName").value(DEFAULT_BATCH_NAME))
            .andExpect(jsonPath("$.participantCount").value(DEFAULT_PARTICIPANT_COUNT))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingBatch() throws Exception {
        // Get the batch
        restBatchMockMvc.perform(get("/api/batches/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBatch() throws Exception {
        // Initialize the database
        batchRepository.saveAndFlush(batch);

        int databaseSizeBeforeUpdate = batchRepository.findAll().size();

        // Update the batch
        Batch updatedBatch = batchRepository.findById(batch.getId()).get();
        // Disconnect from session so that the updates on updatedBatch are not directly saved in db
        em.detach(updatedBatch);
        updatedBatch
            .batchNo(UPDATED_BATCH_NO)
            .batchName(UPDATED_BATCH_NAME)
            .participantCount(UPDATED_PARTICIPANT_COUNT)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .status(UPDATED_STATUS);

        restBatchMockMvc.perform(put("/api/batches").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedBatch)))
            .andExpect(status().isOk());

        // Validate the Batch in the database
        List<Batch> batchList = batchRepository.findAll();
        assertThat(batchList).hasSize(databaseSizeBeforeUpdate);
        Batch testBatch = batchList.get(batchList.size() - 1);
        assertThat(testBatch.getBatchNo()).isEqualTo(UPDATED_BATCH_NO);
        assertThat(testBatch.getBatchName()).isEqualTo(UPDATED_BATCH_NAME);
        assertThat(testBatch.getParticipantCount()).isEqualTo(UPDATED_PARTICIPANT_COUNT);
        assertThat(testBatch.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testBatch.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testBatch.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void updateNonExistingBatch() throws Exception {
        int databaseSizeBeforeUpdate = batchRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBatchMockMvc.perform(put("/api/batches").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(batch)))
            .andExpect(status().isBadRequest());

        // Validate the Batch in the database
        List<Batch> batchList = batchRepository.findAll();
        assertThat(batchList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteBatch() throws Exception {
        // Initialize the database
        batchRepository.saveAndFlush(batch);

        int databaseSizeBeforeDelete = batchRepository.findAll().size();

        // Delete the batch
        restBatchMockMvc.perform(delete("/api/batches/{id}", batch.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Batch> batchList = batchRepository.findAll();
        assertThat(batchList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

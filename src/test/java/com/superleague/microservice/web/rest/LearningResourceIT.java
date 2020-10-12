package com.superleague.microservice.web.rest;

import com.superleague.microservice.SuperleagueApp;
import com.superleague.microservice.config.TestSecurityConfiguration;
import com.superleague.microservice.domain.Learning;
import com.superleague.microservice.repository.LearningRepository;

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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.superleague.microservice.domain.enumeration.Status;
/**
 * Integration tests for the {@link LearningResource} REST controller.
 */
@SpringBootTest(classes = { SuperleagueApp.class, TestSecurityConfiguration.class })
@AutoConfigureMockMvc
@WithMockUser
public class LearningResourceIT {

    private static final Status DEFAULT_STATUS = Status.PLANNED;
    private static final Status UPDATED_STATUS = Status.INPROGRESS;

    @Autowired
    private LearningRepository learningRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLearningMockMvc;

    private Learning learning;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Learning createEntity(EntityManager em) {
        Learning learning = new Learning()
            .status(DEFAULT_STATUS);
        return learning;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Learning createUpdatedEntity(EntityManager em) {
        Learning learning = new Learning()
            .status(UPDATED_STATUS);
        return learning;
    }

    @BeforeEach
    public void initTest() {
        learning = createEntity(em);
    }

    @Test
    @Transactional
    public void createLearning() throws Exception {
        int databaseSizeBeforeCreate = learningRepository.findAll().size();
        // Create the Learning
        restLearningMockMvc.perform(post("/api/learnings").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(learning)))
            .andExpect(status().isCreated());

        // Validate the Learning in the database
        List<Learning> learningList = learningRepository.findAll();
        assertThat(learningList).hasSize(databaseSizeBeforeCreate + 1);
        Learning testLearning = learningList.get(learningList.size() - 1);
        assertThat(testLearning.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void createLearningWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = learningRepository.findAll().size();

        // Create the Learning with an existing ID
        learning.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restLearningMockMvc.perform(post("/api/learnings").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(learning)))
            .andExpect(status().isBadRequest());

        // Validate the Learning in the database
        List<Learning> learningList = learningRepository.findAll();
        assertThat(learningList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllLearnings() throws Exception {
        // Initialize the database
        learningRepository.saveAndFlush(learning);

        // Get all the learningList
        restLearningMockMvc.perform(get("/api/learnings?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(learning.getId().intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }
    
    @Test
    @Transactional
    public void getLearning() throws Exception {
        // Initialize the database
        learningRepository.saveAndFlush(learning);

        // Get the learning
        restLearningMockMvc.perform(get("/api/learnings/{id}", learning.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(learning.getId().intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingLearning() throws Exception {
        // Get the learning
        restLearningMockMvc.perform(get("/api/learnings/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLearning() throws Exception {
        // Initialize the database
        learningRepository.saveAndFlush(learning);

        int databaseSizeBeforeUpdate = learningRepository.findAll().size();

        // Update the learning
        Learning updatedLearning = learningRepository.findById(learning.getId()).get();
        // Disconnect from session so that the updates on updatedLearning are not directly saved in db
        em.detach(updatedLearning);
        updatedLearning
            .status(UPDATED_STATUS);

        restLearningMockMvc.perform(put("/api/learnings").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedLearning)))
            .andExpect(status().isOk());

        // Validate the Learning in the database
        List<Learning> learningList = learningRepository.findAll();
        assertThat(learningList).hasSize(databaseSizeBeforeUpdate);
        Learning testLearning = learningList.get(learningList.size() - 1);
        assertThat(testLearning.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void updateNonExistingLearning() throws Exception {
        int databaseSizeBeforeUpdate = learningRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLearningMockMvc.perform(put("/api/learnings").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(learning)))
            .andExpect(status().isBadRequest());

        // Validate the Learning in the database
        List<Learning> learningList = learningRepository.findAll();
        assertThat(learningList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteLearning() throws Exception {
        // Initialize the database
        learningRepository.saveAndFlush(learning);

        int databaseSizeBeforeDelete = learningRepository.findAll().size();

        // Delete the learning
        restLearningMockMvc.perform(delete("/api/learnings/{id}", learning.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Learning> learningList = learningRepository.findAll();
        assertThat(learningList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

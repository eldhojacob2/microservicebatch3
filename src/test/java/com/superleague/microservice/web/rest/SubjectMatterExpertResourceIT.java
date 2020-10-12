package com.superleague.microservice.web.rest;

import com.superleague.microservice.SuperleagueApp;
import com.superleague.microservice.config.TestSecurityConfiguration;
import com.superleague.microservice.domain.SubjectMatterExpert;
import com.superleague.microservice.repository.SubjectMatterExpertRepository;

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

/**
 * Integration tests for the {@link SubjectMatterExpertResource} REST controller.
 */
@SpringBootTest(classes = { SuperleagueApp.class, TestSecurityConfiguration.class })
@AutoConfigureMockMvc
@WithMockUser
public class SubjectMatterExpertResourceIT {

    private static final String DEFAULT_EMP_ID = "AAAAAAAAAA";
    private static final String UPDATED_EMP_ID = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final Integer DEFAULT_CONTACT_NUMBER = 1;
    private static final Integer UPDATED_CONTACT_NUMBER = 2;

    @Autowired
    private SubjectMatterExpertRepository subjectMatterExpertRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSubjectMatterExpertMockMvc;

    private SubjectMatterExpert subjectMatterExpert;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SubjectMatterExpert createEntity(EntityManager em) {
        SubjectMatterExpert subjectMatterExpert = new SubjectMatterExpert()
            .empId(DEFAULT_EMP_ID)
            .name(DEFAULT_NAME)
            .email(DEFAULT_EMAIL)
            .contactNumber(DEFAULT_CONTACT_NUMBER);
        return subjectMatterExpert;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SubjectMatterExpert createUpdatedEntity(EntityManager em) {
        SubjectMatterExpert subjectMatterExpert = new SubjectMatterExpert()
            .empId(UPDATED_EMP_ID)
            .name(UPDATED_NAME)
            .email(UPDATED_EMAIL)
            .contactNumber(UPDATED_CONTACT_NUMBER);
        return subjectMatterExpert;
    }

    @BeforeEach
    public void initTest() {
        subjectMatterExpert = createEntity(em);
    }

    @Test
    @Transactional
    public void createSubjectMatterExpert() throws Exception {
        int databaseSizeBeforeCreate = subjectMatterExpertRepository.findAll().size();
        // Create the SubjectMatterExpert
        restSubjectMatterExpertMockMvc.perform(post("/api/subject-matter-experts").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(subjectMatterExpert)))
            .andExpect(status().isCreated());

        // Validate the SubjectMatterExpert in the database
        List<SubjectMatterExpert> subjectMatterExpertList = subjectMatterExpertRepository.findAll();
        assertThat(subjectMatterExpertList).hasSize(databaseSizeBeforeCreate + 1);
        SubjectMatterExpert testSubjectMatterExpert = subjectMatterExpertList.get(subjectMatterExpertList.size() - 1);
        assertThat(testSubjectMatterExpert.getEmpId()).isEqualTo(DEFAULT_EMP_ID);
        assertThat(testSubjectMatterExpert.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSubjectMatterExpert.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testSubjectMatterExpert.getContactNumber()).isEqualTo(DEFAULT_CONTACT_NUMBER);
    }

    @Test
    @Transactional
    public void createSubjectMatterExpertWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = subjectMatterExpertRepository.findAll().size();

        // Create the SubjectMatterExpert with an existing ID
        subjectMatterExpert.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSubjectMatterExpertMockMvc.perform(post("/api/subject-matter-experts").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(subjectMatterExpert)))
            .andExpect(status().isBadRequest());

        // Validate the SubjectMatterExpert in the database
        List<SubjectMatterExpert> subjectMatterExpertList = subjectMatterExpertRepository.findAll();
        assertThat(subjectMatterExpertList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkEmpIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = subjectMatterExpertRepository.findAll().size();
        // set the field null
        subjectMatterExpert.setEmpId(null);

        // Create the SubjectMatterExpert, which fails.


        restSubjectMatterExpertMockMvc.perform(post("/api/subject-matter-experts").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(subjectMatterExpert)))
            .andExpect(status().isBadRequest());

        List<SubjectMatterExpert> subjectMatterExpertList = subjectMatterExpertRepository.findAll();
        assertThat(subjectMatterExpertList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = subjectMatterExpertRepository.findAll().size();
        // set the field null
        subjectMatterExpert.setName(null);

        // Create the SubjectMatterExpert, which fails.


        restSubjectMatterExpertMockMvc.perform(post("/api/subject-matter-experts").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(subjectMatterExpert)))
            .andExpect(status().isBadRequest());

        List<SubjectMatterExpert> subjectMatterExpertList = subjectMatterExpertRepository.findAll();
        assertThat(subjectMatterExpertList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = subjectMatterExpertRepository.findAll().size();
        // set the field null
        subjectMatterExpert.setEmail(null);

        // Create the SubjectMatterExpert, which fails.


        restSubjectMatterExpertMockMvc.perform(post("/api/subject-matter-experts").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(subjectMatterExpert)))
            .andExpect(status().isBadRequest());

        List<SubjectMatterExpert> subjectMatterExpertList = subjectMatterExpertRepository.findAll();
        assertThat(subjectMatterExpertList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkContactNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = subjectMatterExpertRepository.findAll().size();
        // set the field null
        subjectMatterExpert.setContactNumber(null);

        // Create the SubjectMatterExpert, which fails.


        restSubjectMatterExpertMockMvc.perform(post("/api/subject-matter-experts").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(subjectMatterExpert)))
            .andExpect(status().isBadRequest());

        List<SubjectMatterExpert> subjectMatterExpertList = subjectMatterExpertRepository.findAll();
        assertThat(subjectMatterExpertList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSubjectMatterExperts() throws Exception {
        // Initialize the database
        subjectMatterExpertRepository.saveAndFlush(subjectMatterExpert);

        // Get all the subjectMatterExpertList
        restSubjectMatterExpertMockMvc.perform(get("/api/subject-matter-experts?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(subjectMatterExpert.getId().intValue())))
            .andExpect(jsonPath("$.[*].empId").value(hasItem(DEFAULT_EMP_ID)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].contactNumber").value(hasItem(DEFAULT_CONTACT_NUMBER)));
    }
    
    @Test
    @Transactional
    public void getSubjectMatterExpert() throws Exception {
        // Initialize the database
        subjectMatterExpertRepository.saveAndFlush(subjectMatterExpert);

        // Get the subjectMatterExpert
        restSubjectMatterExpertMockMvc.perform(get("/api/subject-matter-experts/{id}", subjectMatterExpert.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(subjectMatterExpert.getId().intValue()))
            .andExpect(jsonPath("$.empId").value(DEFAULT_EMP_ID))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.contactNumber").value(DEFAULT_CONTACT_NUMBER));
    }
    @Test
    @Transactional
    public void getNonExistingSubjectMatterExpert() throws Exception {
        // Get the subjectMatterExpert
        restSubjectMatterExpertMockMvc.perform(get("/api/subject-matter-experts/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSubjectMatterExpert() throws Exception {
        // Initialize the database
        subjectMatterExpertRepository.saveAndFlush(subjectMatterExpert);

        int databaseSizeBeforeUpdate = subjectMatterExpertRepository.findAll().size();

        // Update the subjectMatterExpert
        SubjectMatterExpert updatedSubjectMatterExpert = subjectMatterExpertRepository.findById(subjectMatterExpert.getId()).get();
        // Disconnect from session so that the updates on updatedSubjectMatterExpert are not directly saved in db
        em.detach(updatedSubjectMatterExpert);
        updatedSubjectMatterExpert
            .empId(UPDATED_EMP_ID)
            .name(UPDATED_NAME)
            .email(UPDATED_EMAIL)
            .contactNumber(UPDATED_CONTACT_NUMBER);

        restSubjectMatterExpertMockMvc.perform(put("/api/subject-matter-experts").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedSubjectMatterExpert)))
            .andExpect(status().isOk());

        // Validate the SubjectMatterExpert in the database
        List<SubjectMatterExpert> subjectMatterExpertList = subjectMatterExpertRepository.findAll();
        assertThat(subjectMatterExpertList).hasSize(databaseSizeBeforeUpdate);
        SubjectMatterExpert testSubjectMatterExpert = subjectMatterExpertList.get(subjectMatterExpertList.size() - 1);
        assertThat(testSubjectMatterExpert.getEmpId()).isEqualTo(UPDATED_EMP_ID);
        assertThat(testSubjectMatterExpert.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSubjectMatterExpert.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testSubjectMatterExpert.getContactNumber()).isEqualTo(UPDATED_CONTACT_NUMBER);
    }

    @Test
    @Transactional
    public void updateNonExistingSubjectMatterExpert() throws Exception {
        int databaseSizeBeforeUpdate = subjectMatterExpertRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSubjectMatterExpertMockMvc.perform(put("/api/subject-matter-experts").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(subjectMatterExpert)))
            .andExpect(status().isBadRequest());

        // Validate the SubjectMatterExpert in the database
        List<SubjectMatterExpert> subjectMatterExpertList = subjectMatterExpertRepository.findAll();
        assertThat(subjectMatterExpertList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteSubjectMatterExpert() throws Exception {
        // Initialize the database
        subjectMatterExpertRepository.saveAndFlush(subjectMatterExpert);

        int databaseSizeBeforeDelete = subjectMatterExpertRepository.findAll().size();

        // Delete the subjectMatterExpert
        restSubjectMatterExpertMockMvc.perform(delete("/api/subject-matter-experts/{id}", subjectMatterExpert.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SubjectMatterExpert> subjectMatterExpertList = subjectMatterExpertRepository.findAll();
        assertThat(subjectMatterExpertList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

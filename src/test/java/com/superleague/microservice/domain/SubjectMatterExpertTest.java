package com.superleague.microservice.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.superleague.microservice.web.rest.TestUtil;

public class SubjectMatterExpertTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SubjectMatterExpert.class);
        SubjectMatterExpert subjectMatterExpert1 = new SubjectMatterExpert();
        subjectMatterExpert1.setId(1L);
        SubjectMatterExpert subjectMatterExpert2 = new SubjectMatterExpert();
        subjectMatterExpert2.setId(subjectMatterExpert1.getId());
        assertThat(subjectMatterExpert1).isEqualTo(subjectMatterExpert2);
        subjectMatterExpert2.setId(2L);
        assertThat(subjectMatterExpert1).isNotEqualTo(subjectMatterExpert2);
        subjectMatterExpert1.setId(null);
        assertThat(subjectMatterExpert1).isNotEqualTo(subjectMatterExpert2);
    }
}

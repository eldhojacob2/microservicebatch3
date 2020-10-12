package com.superleague.microservice.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.superleague.microservice.web.rest.TestUtil;

public class LearningTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Learning.class);
        Learning learning1 = new Learning();
        learning1.setId(1L);
        Learning learning2 = new Learning();
        learning2.setId(learning1.getId());
        assertThat(learning1).isEqualTo(learning2);
        learning2.setId(2L);
        assertThat(learning1).isNotEqualTo(learning2);
        learning1.setId(null);
        assertThat(learning1).isNotEqualTo(learning2);
    }
}

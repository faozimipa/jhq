package id.zitech.base.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import id.zitech.base.TestUtil;

public class SubDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SubDTO.class);
        SubDTO subDTO1 = new SubDTO();
        subDTO1.id = 1L;
        SubDTO subDTO2 = new SubDTO();
        assertThat(subDTO1).isNotEqualTo(subDTO2);
        subDTO2.id = subDTO1.id;
        assertThat(subDTO1).isEqualTo(subDTO2);
        subDTO2.id = 2L;
        assertThat(subDTO1).isNotEqualTo(subDTO2);
        subDTO1.id = null;
        assertThat(subDTO1).isNotEqualTo(subDTO2);
    }
}

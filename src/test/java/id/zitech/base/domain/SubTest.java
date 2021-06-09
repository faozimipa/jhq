package id.zitech.base.domain;

import static org.assertj.core.api.Assertions.assertThat;

import id.zitech.base.TestUtil;
import org.junit.jupiter.api.Test;


public class SubTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Sub.class);
        Sub sub1 = new Sub();
        sub1.id = 1L;
        Sub sub2 = new Sub();
        sub2.id = sub1.id;
        assertThat(sub1).isEqualTo(sub2);
        sub2.id = 2L;
        assertThat(sub1).isNotEqualTo(sub2);
        sub1.id = null;
        assertThat(sub1).isNotEqualTo(sub2);
    }
}

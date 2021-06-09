package id.zitech.base.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class SubMapperTest {

    private SubMapper subMapper;

    @BeforeEach
    public void setUp() {
        subMapper = new SubMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(subMapper.fromId(id).id).isEqualTo(id);
        assertThat(subMapper.fromId(null)).isNull();
    }
}

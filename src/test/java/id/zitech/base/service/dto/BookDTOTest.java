package id.zitech.base.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import id.zitech.base.TestUtil;

public class BookDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BookDTO.class);
        BookDTO bookDTO1 = new BookDTO();
        bookDTO1.id = 1L;
        BookDTO bookDTO2 = new BookDTO();
        assertThat(bookDTO1).isNotEqualTo(bookDTO2);
        bookDTO2.id = bookDTO1.id;
        assertThat(bookDTO1).isEqualTo(bookDTO2);
        bookDTO2.id = 2L;
        assertThat(bookDTO1).isNotEqualTo(bookDTO2);
        bookDTO1.id = null;
        assertThat(bookDTO1).isNotEqualTo(bookDTO2);
    }
}

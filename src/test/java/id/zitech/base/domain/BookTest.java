package id.zitech.base.domain;

import static org.assertj.core.api.Assertions.assertThat;

import id.zitech.base.TestUtil;
import org.junit.jupiter.api.Test;


public class BookTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Book.class);
        Book book1 = new Book();
        book1.id = 1L;
        Book book2 = new Book();
        book2.id = book1.id;
        assertThat(book1).isEqualTo(book2);
        book2.id = 2L;
        assertThat(book1).isNotEqualTo(book2);
        book1.id = null;
        assertThat(book1).isNotEqualTo(book2);
    }
}

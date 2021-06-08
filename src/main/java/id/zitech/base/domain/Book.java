package id.zitech.base.domain;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import javax.json.bind.annotation.JsonbTransient;
import io.quarkus.runtime.annotations.RegisterForReflection;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * A Book.
 */
@Entity
@Table(name = "book")
@RegisterForReflection
public class Book extends PanacheEntityBase implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    public Long id;

    @Column(name = "title")
    public String title;

    @NotNull
    @Column(name = "isbn", nullable = false)
    public Integer isbn;

    @NotNull
    @Column(name = "author", nullable = false)
    public String author;

    @NotNull
    @Column(name = "publisher", nullable = false)
    public String publisher;

    @NotNull
    @Column(name = "city", nullable = false)
    public String city;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Book)) {
            return false;
        }
        return id != null && id.equals(((Book) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Book{" +
            "id=" + id +
            ", title='" + title + "'" +
            ", isbn=" + isbn +
            ", author='" + author + "'" +
            ", publisher='" + publisher + "'" +
            ", city='" + city + "'" +
            "}";
    }
}

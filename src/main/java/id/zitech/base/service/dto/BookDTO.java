package id.zitech.base.service.dto;


import io.quarkus.runtime.annotations.RegisterForReflection;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link id.zitech.base.domain.Book} entity.
 */
@RegisterForReflection
public class BookDTO implements Serializable {
    
    public Long id;

    public String title;

    @NotNull
    public Integer isbn;

    @NotNull
    public String author;

    @NotNull
    public String publisher;

    @NotNull
    public String city;


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BookDTO)) {
            return false;
        }

        return id != null && id.equals(((BookDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "BookDTO{" +
            "id=" + id +
            ", title='" + title + "'" +
            ", isbn=" + isbn +
            ", author='" + author + "'" +
            ", publisher='" + publisher + "'" +
            ", city='" + city + "'" +
            "}";
    }
}

package id.zitech.base.service.dto;


import io.quarkus.runtime.annotations.RegisterForReflection;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link id.zitech.base.domain.Category} entity.
 */
@RegisterForReflection
public class CategoryDTO implements Serializable {
    
    public Long id;

    @NotNull
    public String categoryName;


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CategoryDTO)) {
            return false;
        }

        return id != null && id.equals(((CategoryDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "CategoryDTO{" +
            "id=" + id +
            ", categoryName='" + categoryName + "'" +
            "}";
    }
}

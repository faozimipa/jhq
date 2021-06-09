package id.zitech.base.service.dto;


import io.quarkus.runtime.annotations.RegisterForReflection;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link id.zitech.base.domain.Sub} entity.
 */
@RegisterForReflection
public class SubDTO implements Serializable {
    
    public Long id;

    public String subName;

    @NotNull
    public String code;

    public Long categoryId;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SubDTO)) {
            return false;
        }

        return id != null && id.equals(((SubDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "SubDTO{" +
            "id=" + id +
            ", subName='" + subName + "'" +
            ", code='" + code + "'" +
            ", categoryId=" + categoryId +
            "}";
    }
}

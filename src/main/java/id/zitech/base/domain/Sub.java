package id.zitech.base.domain;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import javax.json.bind.annotation.JsonbTransient;
import io.quarkus.runtime.annotations.RegisterForReflection;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.List;
import io.quarkus.panache.common.Page;

/**
 * A Sub.
 */
@Entity
@Table(name = "sub")
@RegisterForReflection
public class Sub extends PanacheEntityBase implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    public Long id;

    @Column(name = "sub_name")
    public String subName;

    @NotNull
    @Column(name = "code", nullable = false, unique = true)
    public String code;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    @JsonbTransient
    public Category category;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not
    // remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Sub)) {
            return false;
        }
        return id != null && id.equals(((Sub) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Sub{" + "id=" + id + ", subName='" + subName + "'" + ", code='" + code + "'" + "}";
    }

}

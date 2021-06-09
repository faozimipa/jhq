package id.zitech.base.repository;

import id.zitech.base.domain.Sub;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import javax.enterprise.context.ApplicationScoped;

/**
 * Hibernate Panache repository for the Sub entity.
 */
@SuppressWarnings("unused")
@ApplicationScoped
public class SubRepository implements PanacheRepository<Sub> {

    public Sub update(Sub sub) {
        if (sub == null) {
            throw new IllegalArgumentException("sub can't be null");
        }
        var entity = Sub.<Sub>findById(sub.id);
        if (entity != null) {
            entity.subName = sub.subName;
            entity.code = sub.code;
            entity.category = sub.category;
        }
        return entity;
    }

    public Sub persistOrUpdate(Sub sub) {
        if (sub == null) {
            throw new IllegalArgumentException("sub can't be null");
        }
        if (sub.id == null) {
            persist(sub);
            return sub;
        } else {
            return update(sub);
        }
    }


}

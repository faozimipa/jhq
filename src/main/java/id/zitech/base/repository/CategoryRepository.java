package id.zitech.base.repository;

import id.zitech.base.domain.Category;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import javax.enterprise.context.ApplicationScoped;

/**
 * Hibernate Panache repository for the Category entity.
 */
@SuppressWarnings("unused")
@ApplicationScoped
public class CategoryRepository implements PanacheRepository<Category> {

    public Category update(Category category) {
        if (category == null) {
            throw new IllegalArgumentException("category can't be null");
        }
        var entity = Category.<Category>findById(category.id);
        if (entity != null) {
            entity.categoryName = category.categoryName;
            entity.subs = category.subs;
        }
        return entity;
    }

    public Category persistOrUpdate(Category category) {
        if (category == null) {
            throw new IllegalArgumentException("category can't be null");
        }
        if (category.id == null) {
            persist(category);
            return category;
        } else {
            return update(category);
        }
    }


}

package id.zitech.base.service.impl;

import id.zitech.base.service.CategoryService;
import id.zitech.base.domain.Category;
import id.zitech.base.repository.CategoryRepository;
import id.zitech.base.service.dto.CategoryDTO;
import id.zitech.base.service.mapper.CategoryMapper;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
@Transactional
public class CategoryServiceImpl implements CategoryService {

    private final Logger log = LoggerFactory.getLogger(CategoryServiceImpl.class);

    @Inject
    CategoryRepository categoryRepository;

    @Inject
    CategoryMapper categoryMapper;

    @Override
    @Transactional
    public CategoryDTO persistOrUpdate(CategoryDTO categoryDTO) {
        log.debug("Request to save Category : {}", categoryDTO);
        var category = categoryMapper.toEntity(categoryDTO);
        category = categoryRepository.persistOrUpdate(category);
        return categoryMapper.toDto(category);
    }

    /**
     * Delete the Category by id.
     *
     * @param id the id of the entity.
     */
    @Override
    @Transactional
    public void delete(Long id) {
        log.debug("Request to delete Category : {}", id);
        categoryRepository.findByIdOptional(id).ifPresent(category -> {
            categoryRepository.delete(category);
        });
    }

    /**
     * Get one category by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    public Optional<CategoryDTO> findOne(Long id) {
        log.debug("Request to get Category : {}", id);
        return categoryRepository.findByIdOptional(id)
            .map(category -> categoryMapper.toDto((Category) category)); 
    }

    /**
     * Get all the categories.
     * @return the list of entities.
     */
    @Override
    public  List<CategoryDTO> findAll() {
        log.debug("Request to get all Categories");
        List<Category> categories = categoryRepository.findAll().list();
        return categoryMapper.toDto(categories);
    }



}

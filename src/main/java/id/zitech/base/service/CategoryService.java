package id.zitech.base.service;

import id.zitech.base.service.dto.CategoryDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link id.zitech.base.domain.Category}.
 */
public interface CategoryService {

    /**
     * Save a category.
     *
     * @param categoryDTO the entity to save.
     * @return the persisted entity.
     */
    CategoryDTO persistOrUpdate(CategoryDTO categoryDTO);

    /**
     * Delete the "id" categoryDTO.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Get all the categories.
     * @return the list of entities.
     */
    public  List<CategoryDTO> findAll();

    /**
     * Get the "id" categoryDTO.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CategoryDTO> findOne(Long id);



}

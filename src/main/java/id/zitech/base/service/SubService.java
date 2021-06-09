package id.zitech.base.service;

import id.zitech.base.service.dto.SubDTO;
import io.quarkus.panache.common.Page;

import java.util.Optional;

/**
 * Service Interface for managing {@link id.zitech.base.domain.Sub}.
 */
public interface SubService {

    /**
     * Save a sub.
     *
     * @param subDTO the entity to save.
     * @return the persisted entity.
     */
    SubDTO persistOrUpdate(SubDTO subDTO);

    /**
     * Delete the "id" subDTO.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Get all the subs.
     * @param page the pagination information.
     * @return the list of entities.
     */
    public Paged<SubDTO> findAll(Page page);

    /**
     * Get the "id" subDTO.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SubDTO> findOne(Long id);



}

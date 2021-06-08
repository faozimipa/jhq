package id.zitech.base.service;

import id.zitech.base.service.dto.BookDTO;
import io.quarkus.panache.common.Page;

import java.util.Optional;

/**
 * Service Interface for managing {@link id.zitech.base.domain.Book}.
 */
public interface BookService {

    /**
     * Save a book.
     *
     * @param bookDTO the entity to save.
     * @return the persisted entity.
     */
    BookDTO persistOrUpdate(BookDTO bookDTO);

    /**
     * Delete the "id" bookDTO.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Get all the books.
     * @param page the pagination information.
     * @return the list of entities.
     */
    public Paged<BookDTO> findAll(Page page);

    /**
     * Get the "id" bookDTO.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<BookDTO> findOne(Long id);



}

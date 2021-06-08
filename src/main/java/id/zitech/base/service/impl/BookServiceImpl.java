package id.zitech.base.service.impl;

import id.zitech.base.service.BookService;
import io.quarkus.panache.common.Page;
import id.zitech.base.service.Paged;
import id.zitech.base.domain.Book;
import id.zitech.base.repository.BookRepository;
import id.zitech.base.service.dto.BookDTO;
import id.zitech.base.service.mapper.BookMapper;
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
public class BookServiceImpl implements BookService {

    private final Logger log = LoggerFactory.getLogger(BookServiceImpl.class);

    @Inject
    BookRepository bookRepository;

    @Inject
    BookMapper bookMapper;

    @Override
    @Transactional
    public BookDTO persistOrUpdate(BookDTO bookDTO) {
        log.debug("Request to save Book : {}", bookDTO);
        var book = bookMapper.toEntity(bookDTO);
        book = bookRepository.persistOrUpdate(book);
        return bookMapper.toDto(book);
    }

    /**
     * Delete the Book by id.
     *
     * @param id the id of the entity.
     */
    @Override
    @Transactional
    public void delete(Long id) {
        log.debug("Request to delete Book : {}", id);
        bookRepository.findByIdOptional(id).ifPresent(book -> {
            bookRepository.delete(book);
        });
    }

    /**
     * Get one book by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    public Optional<BookDTO> findOne(Long id) {
        log.debug("Request to get Book : {}", id);
        return bookRepository.findByIdOptional(id)
            .map(book -> bookMapper.toDto((Book) book)); 
    }

    /**
     * Get all the books.
     * @param page the pagination information.
     * @return the list of entities.
     */
    @Override
    public Paged<BookDTO> findAll(Page page) {
        log.debug("Request to get all Books");
        return new Paged<>(bookRepository.findAll().page(page))
            .map(book -> bookMapper.toDto((Book) book));
    }



}

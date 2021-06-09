package id.zitech.base.service.impl;

import id.zitech.base.service.SubService;
import io.quarkus.panache.common.Page;
import id.zitech.base.service.Paged;
import id.zitech.base.domain.Sub;
import id.zitech.base.repository.SubRepository;
import id.zitech.base.service.dto.SubDTO;
import id.zitech.base.service.mapper.SubMapper;
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
public class SubServiceImpl implements SubService {

    private final Logger log = LoggerFactory.getLogger(SubServiceImpl.class);

    @Inject
    SubRepository subRepository;

    @Inject
    SubMapper subMapper;

    @Override
    @Transactional
    public SubDTO persistOrUpdate(SubDTO subDTO) {
        log.debug("Request to save Sub : {}", subDTO);
        var sub = subMapper.toEntity(subDTO);
        sub = subRepository.persistOrUpdate(sub);
        return subMapper.toDto(sub);
    }

    /**
     * Delete the Sub by id.
     *
     * @param id the id of the entity.
     */
    @Override
    @Transactional
    public void delete(Long id) {
        log.debug("Request to delete Sub : {}", id);
        subRepository.findByIdOptional(id).ifPresent(sub -> {
            subRepository.delete(sub);
        });
    }

    /**
     * Get one sub by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    public Optional<SubDTO> findOne(Long id) {
        log.debug("Request to get Sub : {}", id);
        return subRepository.findByIdOptional(id)
            .map(sub -> subMapper.toDto((Sub) sub)); 
    }

    /**
     * Get all the subs.
     * @param page the pagination information.
     * @return the list of entities.
     */
    @Override
    public Paged<SubDTO> findAll(Page page) {
        log.debug("Request to get all Subs");
        return new Paged<>(subRepository.findAll().page(page))
            .map(sub -> subMapper.toDto((Sub) sub));
    }



}

package id.zitech.base.service.mapper;

import id.zitech.base.domain.*;
import id.zitech.base.service.dto.SubDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Sub} and its DTO {@link SubDTO}.
 */
@Mapper(componentModel = "cdi", uses = { CategoryMapper.class })
public interface SubMapper extends EntityMapper<SubDTO, Sub> {

    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(source = "category.categoryName", target = "categoryName")
    SubDTO toDto(Sub sub);

    @Mapping(source = "categoryId", target = "category")
    Sub toEntity(SubDTO subDTO);

    default Sub fromId(Long id) {
        if (id == null) {
            return null;
        }
        Sub sub = new Sub();
        sub.id = id;
        return sub;
    }
}

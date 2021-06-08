package id.zitech.base.service.mapper;


import id.zitech.base.domain.*;
import id.zitech.base.service.dto.BookDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Book} and its DTO {@link BookDTO}.
 */
@Mapper(componentModel = "cdi", uses = {})
public interface BookMapper extends EntityMapper<BookDTO, Book> {



    default Book fromId(Long id) {
        if (id == null) {
            return null;
        }
        Book book = new Book();
        book.id = id;
        return book;
    }
}

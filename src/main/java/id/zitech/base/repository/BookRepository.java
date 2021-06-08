package id.zitech.base.repository;

import id.zitech.base.domain.Book;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import javax.enterprise.context.ApplicationScoped;

/**
 * Hibernate Panache repository for the Book entity.
 */
@SuppressWarnings("unused")
@ApplicationScoped
public class BookRepository implements PanacheRepository<Book> {

    public Book update(Book book) {
        if (book == null) {
            throw new IllegalArgumentException("book can't be null");
        }
        var entity = Book.<Book>findById(book.id);
        if (entity != null) {
            entity.title = book.title;
            entity.isbn = book.isbn;
            entity.author = book.author;
            entity.publisher = book.publisher;
            entity.city = book.city;
        }
        return entity;
    }

    public Book persistOrUpdate(Book book) {
        if (book == null) {
            throw new IllegalArgumentException("book can't be null");
        }
        if (book.id == null) {
            persist(book);
            return book;
        } else {
            return update(book);
        }
    }


}

package id.zitech.base.web.rest;

import static javax.ws.rs.core.UriBuilder.fromPath;

import id.zitech.base.service.BookService;
import id.zitech.base.repository.BookRepository;
import id.zitech.base.web.rest.errors.BadRequestAlertException;
import id.zitech.base.web.util.HeaderUtil;
import id.zitech.base.web.util.ResponseUtil;
import id.zitech.base.service.dto.BookDTO;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import id.zitech.base.service.Paged;
import id.zitech.base.web.rest.vm.PageRequestVM;
import id.zitech.base.web.rest.vm.SortRequestVM;
import id.zitech.base.web.util.PaginationUtil;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link id.zitech.base.domain.Book}.
 */
@Path("/api/books")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class BookResource {

    private final Logger log = LoggerFactory.getLogger(BookResource.class);

    private static final String ENTITY_NAME = "book";

    @ConfigProperty(name = "application.name")
    String applicationName;


    @Inject
    BookService bookService;
    /**
     * {@code POST  /books} : Create a new book.
     *
     * @param bookDTO the bookDTO to create.
     * @return the {@link Response} with status {@code 201 (Created)} and with body the new bookDTO, or with status {@code 400 (Bad Request)} if the book has already an ID.
     */
    @POST
    public Response createBook(@Valid BookDTO bookDTO, @Context UriInfo uriInfo) {
        log.debug("REST request to save Book : {}", bookDTO);
        if (bookDTO.id != null) {
            throw new BadRequestAlertException("A new book cannot already have an ID", ENTITY_NAME, "idexists");
        }
        var result = bookService.persistOrUpdate(bookDTO);
        var response = Response.created(fromPath(uriInfo.getPath()).path(result.id.toString()).build()).entity(result);
        HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code PUT  /books} : Updates an existing book.
     *
     * @param bookDTO the bookDTO to update.
     * @return the {@link Response} with status {@code 200 (OK)} and with body the updated bookDTO,
     * or with status {@code 400 (Bad Request)} if the bookDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the bookDTO couldn't be updated.
     */
    @PUT
    public Response updateBook(@Valid BookDTO bookDTO) {
        log.debug("REST request to update Book : {}", bookDTO);
        if (bookDTO.id == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        var result = bookService.persistOrUpdate(bookDTO);
        var response = Response.ok().entity(result);
        HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, bookDTO.id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code DELETE  /books/:id} : delete the "id" book.
     *
     * @param id the id of the bookDTO to delete.
     * @return the {@link Response} with status {@code 204 (NO_CONTENT)}.
     */
    @DELETE
    @Path("/{id}")
    public Response deleteBook(@PathParam("id") Long id) {
        log.debug("REST request to delete Book : {}", id);
        bookService.delete(id);
        var response = Response.noContent();
        HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code GET  /books} : get all the books.
     *
     * @param pageRequest the pagination information.
     * @return the {@link Response} with status {@code 200 (OK)} and the list of books in body.
     */
    @GET
    public Response getAllBooks(@BeanParam PageRequestVM pageRequest, @BeanParam SortRequestVM sortRequest, @Context UriInfo uriInfo) {
        log.debug("REST request to get a page of Books");
        var page = pageRequest.toPage();
        var sort = sortRequest.toSort();
        Paged<BookDTO> result = bookService.findAll(page);
        var response = Response.ok().entity(result.content);
        response = PaginationUtil.withPaginationInfo(response, uriInfo, result);
        return response.build();
    }


    /**
     * {@code GET  /books/:id} : get the "id" book.
     *
     * @param id the id of the bookDTO to retrieve.
     * @return the {@link Response} with status {@code 200 (OK)} and with body the bookDTO, or with status {@code 404 (Not Found)}.
     */
    @GET
    @Path("/{id}")

    public Response getBook(@PathParam("id") Long id) {
        log.debug("REST request to get Book : {}", id);
        Optional<BookDTO> bookDTO = bookService.findOne(id);
        return ResponseUtil.wrapOrNotFound(bookDTO);
    }
}

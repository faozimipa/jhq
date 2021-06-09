package id.zitech.base.web.rest;

import static javax.ws.rs.core.UriBuilder.fromPath;

import id.zitech.base.service.SubService;
import id.zitech.base.repository.SubRepository;
import id.zitech.base.web.rest.errors.BadRequestAlertException;
import id.zitech.base.web.util.HeaderUtil;
import id.zitech.base.web.util.ResponseUtil;
import id.zitech.base.service.dto.SubDTO;

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
 * REST controller for managing {@link id.zitech.base.domain.Sub}.
 */
@Path("/api/subs")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class SubResource {

    private final Logger log = LoggerFactory.getLogger(SubResource.class);

    private static final String ENTITY_NAME = "sub";

    @ConfigProperty(name = "application.name")
    String applicationName;


    @Inject
    SubService subService;
    /**
     * {@code POST  /subs} : Create a new sub.
     *
     * @param subDTO the subDTO to create.
     * @return the {@link Response} with status {@code 201 (Created)} and with body the new subDTO, or with status {@code 400 (Bad Request)} if the sub has already an ID.
     */
    @POST
    public Response createSub(@Valid SubDTO subDTO, @Context UriInfo uriInfo) {
        log.debug("REST request to save Sub : {}", subDTO);
        if (subDTO.id != null) {
            throw new BadRequestAlertException("A new sub cannot already have an ID", ENTITY_NAME, "idexists");
        }
        var result = subService.persistOrUpdate(subDTO);
        var response = Response.created(fromPath(uriInfo.getPath()).path(result.id.toString()).build()).entity(result);
        HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code PUT  /subs} : Updates an existing sub.
     *
     * @param subDTO the subDTO to update.
     * @return the {@link Response} with status {@code 200 (OK)} and with body the updated subDTO,
     * or with status {@code 400 (Bad Request)} if the subDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the subDTO couldn't be updated.
     */
    @PUT
    public Response updateSub(@Valid SubDTO subDTO) {
        log.debug("REST request to update Sub : {}", subDTO);
        if (subDTO.id == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        var result = subService.persistOrUpdate(subDTO);
        var response = Response.ok().entity(result);
        HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, subDTO.id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code DELETE  /subs/:id} : delete the "id" sub.
     *
     * @param id the id of the subDTO to delete.
     * @return the {@link Response} with status {@code 204 (NO_CONTENT)}.
     */
    @DELETE
    @Path("/{id}")
    public Response deleteSub(@PathParam("id") Long id) {
        log.debug("REST request to delete Sub : {}", id);
        subService.delete(id);
        var response = Response.noContent();
        HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code GET  /subs} : get all the subs.
     *
     * @param pageRequest the pagination information.
     * @return the {@link Response} with status {@code 200 (OK)} and the list of subs in body.
     */
    @GET
    public Response getAllSubs(@BeanParam PageRequestVM pageRequest, @BeanParam SortRequestVM sortRequest, @Context UriInfo uriInfo) {
        log.debug("REST request to get a page of Subs");
        var page = pageRequest.toPage();
        var sort = sortRequest.toSort();
        Paged<SubDTO> result = subService.findAll(page);
        var response = Response.ok().entity(result.content);
        response = PaginationUtil.withPaginationInfo(response, uriInfo, result);
        return response.build();
    }


    /**
     * {@code GET  /subs/:id} : get the "id" sub.
     *
     * @param id the id of the subDTO to retrieve.
     * @return the {@link Response} with status {@code 200 (OK)} and with body the subDTO, or with status {@code 404 (Not Found)}.
     */
    @GET
    @Path("/{id}")

    public Response getSub(@PathParam("id") Long id) {
        log.debug("REST request to get Sub : {}", id);
        Optional<SubDTO> subDTO = subService.findOne(id);
        return ResponseUtil.wrapOrNotFound(subDTO);
    }
}

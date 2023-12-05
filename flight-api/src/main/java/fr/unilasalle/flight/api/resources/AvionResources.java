package fr.unilasalle.flight.api.resources;

import fr.unilasalle.flight.api.beans.Avion;
import fr.unilasalle.flight.api.repositories.AvionRepo;
import jakarta.inject.Inject;
import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Set;

@Path("/avion")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AvionResources extends GenericResources {

    @Inject
    AvionRepo repository;

    @Inject
    Validator validator;

    @GET
    public Response getPlanes(@QueryParam("operator") String operator) {
        List<Avion> planes;
        if (StringUtils.isBlank(operator)) {
            planes = repository.listAll();
        } else {
            planes = repository.findByOperator(operator);
        }
        return getOr404(planes);
    }

    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") Long id) {
        Avion Avion = repository.findByIdOptional(id).orElse(null);
        return getOr404(Avion);
    }

    @POST
    @Transactional
    public Response createPlane(Avion Avion) {
        Set<ConstraintViolation<Avion>> violations = validator.validate(Avion);
        if (!violations.isEmpty()) {
            return Response.status(400).entity(new ErrorWrapper(violations)).build();
        }

        try {
            repository.persistAndFlush(Avion);
            return Response.ok(Avion).status(201).build();
        } catch (PersistenceException ex) {
            return Response.serverError().entity(new ErrorWrapper(ex.getMessage())).build();
        }
    }
}

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

    // Injecting AvionRepo and Validator
    @Inject
    AvionRepo repository;

    @Inject
    Validator validator;

    // Retrieve the list of planes based on the operator (all planes if the operator is not specified)
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

    // Retrieve a plane based on its identifier
    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") Long id) {
        Avion avion = repository.findByIdOptional(id).orElse(null);
        return getOr404(avion);
    }

    // Create a new plane
    @POST
    @Transactional
    public Response createPlane(Avion avion) {
        Set<ConstraintViolation<Avion>> violations = validator.validate(avion);

        // If there are validation violations, return a 400 Bad Request response with the validation errors
        if (!violations.isEmpty()) {
            return Response.status(400).entity(new ErrorWrapper(violations)).build();
        }

        try {
            // Persist and flush the new plane
            repository.persistAndFlush(avion);
            // Return a 201 Created response with the created plane
            return Response.ok(avion).status(201).build();
        } catch (PersistenceException ex) {
            // If there is a persistence exception, return a 500 Internal Server Error response with the exception message
            return Response.serverError().entity(new ErrorWrapper(ex.getMessage())).build();
        }
    }
}

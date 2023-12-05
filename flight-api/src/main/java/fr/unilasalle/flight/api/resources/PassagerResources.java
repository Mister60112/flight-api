package fr.unilasalle.flight.api.resources;

import fr.unilasalle.flight.api.beans.Passager;
import fr.unilasalle.flight.api.repositories.PassagerRepo;
import jakarta.inject.Inject;
import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Set;

@Path("/passager")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PassagerResources extends GenericResources {

    @Inject
    PassagerRepo repository;

    @Inject
    Validator validator;

    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") Long id) {
        Passager passager = repository.findByIdOptional(id).orElse(null);
        return getOr404(passager);
    }

    @PUT
    @Transactional
    @Path("/{id}")
    public Response updatePassenger(@PathParam("id") Long id, Passager passager) {
        Passager existingPassenger = repository.findByIdOptional(id).orElse(null);
        if (existingPassenger == null) {
            return Response.status(404).build();
        }

        Set<ConstraintViolation<Passager>> violations = validator.validate(passager);
        if (!violations.isEmpty()) {
            return Response.status(400).entity(new ErrorWrapper(violations)).build();
        }

        try {
            repository.update("surname = ?1, firstname = ?2, emailAddress = ?3 where id = ?4",
                    passager.getSurname(), passager.getFirstname(), passager.getEmailAddress(), id);
            return Response.ok(passager).status(200).build();
        } catch (PersistenceException ex) {
            return Response.serverError().entity(new ErrorWrapper(ex.getMessage())).build();
        }

    }
}

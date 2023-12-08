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

    // Injecting PassagerRepo and Validator
    @Inject
    PassagerRepo repository;

    @Inject
    Validator validator;

    // Retrieve a passenger by ID
    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") Long id) {
        Passager passager = repository.findByIdOptional(id).orElse(null);
        return getOr404(passager);
    }

    // Update a passenger by ID
    @PUT
    @Transactional
    @Path("/{id}")
    public Response updatePassenger(@PathParam("id") Long id, Passager passager) {
        // Retrieve the existing passenger by ID
        Passager existingPassenger = repository.findByIdOptional(id).orElse(null);

        // If the passenger does not exist, return a 404 Not Found response
        if (existingPassenger == null) {
            return Response.status(404).build();
        }

        // Validate the updated passenger
        Set<ConstraintViolation<Passager>> violations = validator.validate(passager);
        if (!violations.isEmpty()) {
            // If there are validation violations, return a 400 Bad Request response with the validation errors
            return Response.status(400).entity(new ErrorWrapper(violations)).build();
        }

        try {
            // Update the existing passenger with the provided information
            repository.update("surname = ?1, firstname = ?2, emailAddress = ?3 where id = ?4",
                    passager.getSurname(), passager.getFirstname(), passager.getEmailAddress(), id);
            // Return a 200 OK response with the updated passenger
            return Response.ok(passager).status(200).build();
        } catch (PersistenceException ex) {
            // If there is a persistence exception, return a 500 Internal Server Error response with the exception message
            return Response.serverError().entity(new ErrorWrapper(ex.getMessage())).build();
        }
    }
}

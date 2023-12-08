package fr.unilasalle.flight.api.resources;

import fr.unilasalle.flight.api.beans.Vol;
import fr.unilasalle.flight.api.beans.Avion;
import fr.unilasalle.flight.api.beans.Reservation;
import fr.unilasalle.flight.api.repositories.VolRepo;
import fr.unilasalle.flight.api.repositories.ReservationRepo;
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

@Path("/vol")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class VolResources extends GenericResources {

    // Injecting repositories and Validator
    @Inject
    VolRepo flightRepository;

    @Inject
    AvionRepo planeRepository;

    @Inject
    ReservationRepo reservationRepository;

    @Inject
    Validator validator;

    // Retrieve flights based on destination or return all flights
    @GET
    public Response getFlights(@QueryParam("destination") String destination) {
        List<Vol> flights;
        if (StringUtils.isBlank(destination)) {
            flights = flightRepository.listAll();
        } else {
            flights = flightRepository.findByDestination(destination);
        }
        return getOr404(flights);
    }

    // Retrieve a flight by ID
    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") Long id) {
        Vol flight = flightRepository.findByIdOptional(id).orElse(null);
        return getOr404(flight);
    }

    // Create a new flight
    @POST
    @Transactional
    public Response createFlight(Vol flight) {
        // Validate the flight
        Set<ConstraintViolation<Vol>> violations = validator.validate(flight);

        if (!violations.isEmpty()) {
            return Response.status(400).entity(new ErrorWrapper(violations)).build();
        }

        Avion plane;
        if (StringUtils.isNotBlank(flight.getAvion().getRegistration())) {
            plane = planeRepository.findByRegistration(flight.getAvion().getRegistration());

            // If the plane does not exist, return a 400 Bad Request response
            if (plane == null) {
                return Response.status(400).entity(new ErrorWrapper("Provided Plane does not exist")).build();
            }
            flight.setAvion(plane);
        }

        try {
            // Persist and flush the flight
            flightRepository.persistAndFlush(flight);
            // Return a 201 Created response with the created flight
            return Response.ok(flight).status(201).build();
        } catch (PersistenceException ex) {
            // If there is a persistence exception, return a 500 Internal Server Error response with the exception message
            return Response.serverError().entity(new ErrorWrapper(ex.getMessage())).build();
        }
    }

    // Delete a flight by ID, along with its associated reservations
    @DELETE
    @Transactional
    @Path("/{id}")
    public Response deleteFlight(@PathParam("id") Long id) {
        Vol flight = flightRepository.findByIdOptional(id).orElse(null);

        // If the flight does not exist, return a 404 Not Found response
        if (flight == null) {
            return Response.status(404).build();
        }

        // Find reservations associated with the flight
        List<Reservation> reservations = reservationRepository.findByFlightId(id);

        // Delete each associated reservation
        for (Reservation reservation : reservations) {
            try {
                reservationRepository.deleteById(reservation.getId());
            } catch (PersistenceException ex) {
                // If there is a persistence exception, return a 500 Internal Server Error response with the exception message
                return Response.serverError().entity(new ErrorWrapper(ex.getMessage())).build();
            }
        }

        try {
            // Delete the flight by ID
            flightRepository.deleteById(id);
            // Return a 200 OK response
            return Response.ok().status(200).build();
        } catch (PersistenceException ex) {
            // If there is a persistence exception, return a 500 Internal Server Error response with the exception message
            return Response.serverError().entity(new ErrorWrapper(ex.getMessage())).build();
        }
    }
}

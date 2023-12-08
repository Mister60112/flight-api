package fr.unilasalle.flight.api.resources;

import fr.unilasalle.flight.api.beans.Vol;
import fr.unilasalle.flight.api.beans.Passager;
import fr.unilasalle.flight.api.beans.Reservation;
import fr.unilasalle.flight.api.repositories.VolRepo;
import fr.unilasalle.flight.api.repositories.PassagerRepo;
import fr.unilasalle.flight.api.repositories.ReservationRepo;
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

@Path("/reservation")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ReservationResources extends GenericResources {

    // Injecting repositories and Validator
    @Inject
    ReservationRepo reservationRepository;

    @Inject
    VolRepo flightRepository;

    @Inject
    PassagerRepo passengerRepository;

    @Inject
    Validator validator;

    // Retrieve reservations based on flight number or return all reservations
    @GET
    public Response getReservations(@QueryParam("flightNumber") String flightNumber) {
        List<Reservation> reservations;

        if (StringUtils.isNotBlank(flightNumber)) {
            reservations = reservationRepository.findByFlightNumber(flightNumber);
        } else {
            reservations = reservationRepository.listAll();
        }

        return Response.ok(reservations).status(200).build();
    }

    // Create a new reservation
    @POST
    @Transactional
    public Response createReservation(Reservation reservation) {
        // Validate the reservation
        Set<ConstraintViolation<Reservation>> reservationViolation = validator.validate(reservation);
        if (!reservationViolation.isEmpty()) {
            return Response.status(400).entity(new ErrorWrapper(reservationViolation)).build();
        }

        // Validate the passenger
        Set<ConstraintViolation<Passager>> passengerViolations = validator.validate(reservation.getPassager());
        if (!passengerViolations.isEmpty()) {
            return Response.status(400).entity(new ErrorWrapper(passengerViolations)).build();
        }

        Vol vol;
        if (StringUtils.isNotBlank(reservation.getVol().getNumber())) {
            vol = flightRepository.findByNumber(reservation.getVol().getNumber());

            // If the flight does not exist, return a 400 Bad Request response
            if (vol == null) {
                return Response.status(400).entity(new ErrorWrapper("Provided Flight does not exist")).build();
            }

            // If the flight is full, return a 400 Bad Request response
            if (reservationRepository.countByFlightNumber(reservation.getVol().getNumber()).equals(vol.getAvion().getCapacity())) {
                return Response.status(400).entity(new ErrorWrapper("Provided Flight is complete")).build();
            }

            reservation.setVol(vol);
        }

        // Find or create the passenger
        Passager passager = passengerRepository.findByEmailAddress(reservation.getPassager().getEmailAddress());
        if (passager == null) {
            try {
                passengerRepository.persistAndFlush(reservation.getPassager());
                passager = passengerRepository.findByEmailAddress(reservation.getPassager().getEmailAddress());
            } catch (PersistenceException ex) {
                return Response.serverError().entity(new ErrorWrapper(ex.getMessage())).build();
            }
        }
        reservation.setPassager(passager);

        try {
            // Persist and flush the reservation
            reservationRepository.persistAndFlush(reservation);
            // Return a 201 Created response with the created reservation
            return Response.ok(reservation).status(201).build();
        } catch (PersistenceException ex) {
            // If there is a persistence exception, return a 500 Internal Server Error response with the exception message
            return Response.serverError().entity(new ErrorWrapper(ex.getMessage())).build();
        }
    }

    // Delete a reservation by ID
    @DELETE
    @Transactional
    @Path("/{id}")
    public Response deleteReservation(@PathParam("id") Long id) {
        Reservation reservation = reservationRepository.findByIdOptional(id).orElse(null);

        // If the reservation does not exist, return a 404 Not Found response
        if (reservation == null) {
            return Response.status(404).build();
        }

        try {
            // Delete the reservation by ID
            reservationRepository.deleteById(id);
            // Return a 200 OK response
            return Response.ok().status(200).build();
        } catch (PersistenceException ex) {
            // If there is a persistence exception, return a 500 Internal Server Error response with the exception message
            return Response.serverError().entity(new ErrorWrapper(ex.getMessage())).build();
        }
    }
}

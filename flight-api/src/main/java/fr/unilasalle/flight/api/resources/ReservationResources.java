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

    @Inject
    ReservationRepo reservationRepository;

    @Inject
    VolRepo flightRepository;

    @Inject
    PassagerRepo passengerRepository;

    @Inject
    Validator validator;

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

    @POST
    @Transactional
    public Response createReservation(Reservation reservation) {
        Set<ConstraintViolation<Reservation>> reservationViolation = validator.validate(reservation);
        if (!reservationViolation.isEmpty()) {
            return Response.status(400).entity(new ErrorWrapper(reservationViolation)).build();
        }

        Set<ConstraintViolation<Passager>> passengerViolations = validator.validate(reservation.getPassager());
        if (!passengerViolations.isEmpty()) {
            return Response.status(400).entity(new ErrorWrapper(passengerViolations)).build();
        }

        Vol vol;
        if (StringUtils.isNotBlank(reservation.getVol().getNumber())) {
            vol = flightRepository.findByNumber(reservation.getVol().getNumber());
            if (vol == null) {
                return Response.status(400).entity(new ErrorWrapper("Provided Flight does not exist")).build();
            }
            if(reservationRepository.countByFlightNumber(reservation.getVol().getNumber()).equals(vol.getAvion().getCapacity())){
                return Response.status(400).entity(new ErrorWrapper("Provided Flight is complete")).build();
            }
            reservation.setVol(vol);
        }

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
            reservationRepository.persistAndFlush(reservation);
            return Response.ok(reservation).status(201).build();
        } catch (PersistenceException ex) {
            return Response.serverError().entity(new ErrorWrapper(ex.getMessage())).build();
        }
    }


    @DELETE
    @Transactional
    @Path("/{id}")
    public Response deleteReservation(@PathParam("id") Long id) {
        Reservation reservation = reservationRepository.findByIdOptional(id).orElse(null);
        if (reservation == null) {
            return Response.status(404).build();
        }

        try {
            reservationRepository.deleteById(id);
            return Response.ok().status(200).build();
        } catch (PersistenceException ex) {
            return Response.serverError().entity(new ErrorWrapper(ex.getMessage())).build();
        }
    }
}

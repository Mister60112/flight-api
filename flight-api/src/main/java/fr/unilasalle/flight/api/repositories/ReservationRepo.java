package fr.unilasalle.flight.api.repositories;

import fr.unilasalle.flight.api.beans.Reservation;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.inject.Model;

import java.util.List;

@Model
public class ReservationRepo implements PanacheRepositoryBase<Reservation, Long> {

    public List<Reservation> findByFlightId(Long id) {
        return find("flight.id", id).stream().toList();
    }

    public List<Reservation> findByFlightNumber(String number) {
        return find("flight.number", number).stream().toList();
    }

    public Long countByFlightNumber(String number) {
        return count("flight.number", number);
    }

}

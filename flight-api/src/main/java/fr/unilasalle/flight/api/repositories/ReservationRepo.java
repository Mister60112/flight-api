package fr.unilasalle.flight.api.repositories;

import fr.unilasalle.flight.api.beans.Reservation;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.inject.Model;

import java.util.List;

@Model
public class ReservationRepo implements PanacheRepositoryBase<Reservation, Long> {

    // Recherche et renvoie la liste des réservations en fonction de l'ID du vol spécifié
    public List<Reservation> findByFlightId(Long id) {
        return find("flight.id", id).stream().toList();
    }

    // Recherche et renvoie la liste des réservations en fonction du numéro de vol spécifié
    public List<Reservation> findByFlightNumber(String number) {
        return find("flight.number", number).stream().toList();
    }

    // Compte le nombre de réservations en fonction du numéro de vol spécifié
    public Long countByFlightNumber(String number) {
        return count("flight.number", number);
    }
}

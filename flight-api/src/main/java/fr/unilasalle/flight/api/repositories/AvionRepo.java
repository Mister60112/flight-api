package fr.unilasalle.flight.api.repositories;

import fr.unilasalle.flight.api.beans.Avion;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.inject.Model;

import java.util.List;

@Model
public class AvionRepo implements PanacheRepositoryBase<Avion, Long> {

    // Recherche et renvoie une liste d'avions en fonction de l'opérateur spécifié
    public List<Avion> findByOperator(String operator) {
        return find("operator", operator).stream().toList();
    }

    // Recherche et renvoie un avion en fonction du numéro d'immatriculation spécifié
    public Avion findByRegistration(String registration) {
        return find("registration", registration).firstResultOptional().orElse(null);
    }
}

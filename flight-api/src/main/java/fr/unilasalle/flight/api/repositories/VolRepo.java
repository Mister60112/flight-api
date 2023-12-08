package fr.unilasalle.flight.api.repositories;

import fr.unilasalle.flight.api.beans.Vol;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.inject.Model;

import java.util.List;

@Model
public class VolRepo implements PanacheRepositoryBase<Vol, Long> {

    // Recherche et renvoie la liste des vols en fonction de la destination spécifiée
    public List<Vol> findByDestination(String destination) {
        return find("destination", destination).stream().toList();
    }

    // Recherche et renvoie un vol en fonction du numéro de vol spécifié
    public Vol findByNumber(String number) {
        return find("number", number).firstResultOptional().orElse(null);
    }
}

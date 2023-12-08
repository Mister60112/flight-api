package fr.unilasalle.flight.api.repositories;

import fr.unilasalle.flight.api.beans.Passager;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.inject.Model;

@Model
public class PassagerRepo implements PanacheRepositoryBase<Passager, Long> {

    // Recherche et renvoie un passager en fonction de l'adresse e-mail spécifiée
    public Passager findByEmailAddress(String emailAddress) {
        return find("emailAddress", emailAddress).firstResultOptional().orElse(null);
    }
}

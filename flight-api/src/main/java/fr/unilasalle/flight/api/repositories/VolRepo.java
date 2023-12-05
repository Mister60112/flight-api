package fr.unilasalle.flight.api.repositories;

import fr.unilasalle.flight.api.beans.Vol;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.inject.Model;

import java.util.List;

@Model
public class VolRepo implements PanacheRepositoryBase<Vol, Long> {

    public List<Vol> findByDestination(String destination) {
        return find("destination", destination).stream().toList();
    }

    public Vol findByNumber(String number) {
        return find("number", number).firstResultOptional().orElse(null);
    }
}

package fr.unilasalle.flight.api.repositories;

import fr.unilasalle.flight.api.beans.Avion;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.inject.Model;

import java.util.List;

@Model
public class AvionRepo implements PanacheRepositoryBase<Avion, Long> {

    public List<Avion> findByOperator(String operator) {
        return find("operator", operator).stream().toList();
    }

    public Avion findByRegistration(String registration) {
        return find("registration", registration).firstResultOptional().orElse(null);
    }
}

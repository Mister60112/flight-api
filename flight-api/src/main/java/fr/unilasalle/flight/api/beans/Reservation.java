package fr.unilasalle.flight.api.beans;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.enterprise.inject.Model;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Model
@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "reservations", uniqueConstraints = {@UniqueConstraint(name = "unique_reservation", columnNames = {"flightId", "passengerId"})})
public class Reservation extends PanacheEntityBase {

    // Identifiant unique généré automatiquement pour chaque réservation
    @Id
    @SequenceGenerator(name = "reservations_sequence", sequenceName = "reservations_sequence", allocationSize = 1, initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "reservations_sequence")
    private Long id;

    // Vol associé à la réservation, ne doit pas être vide
    @NotNull(message = "le vol ne doit pas être vide")
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "flightId", referencedColumnName = "id", nullable = false)
    private Vol vol;

    // Passager associé à la réservation, ne doit pas être vide
    @NotNull(message = "le passager ne doit pas être vide")
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "passengerId", referencedColumnName = "id", nullable = false)
    private Passager passager;
}

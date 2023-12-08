package fr.unilasalle.flight.api.beans;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fr.unilasalle.flight.api.constants.regex;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.enterprise.inject.Model;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Model
@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "flights")
public class Vol extends PanacheEntityBase {

    // Identifiant unique généré automatiquement pour chaque vol
    @Id
    @SequenceGenerator(name = "flights_sequence", sequenceName = "flights_sequence", allocationSize = 1, initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "flights_sequence")
    private Long id;

    // Numéro de vol, doit avoir une longueur de 6 caractères, être unique et suivre le modèle défini
    @NotBlank(message = "le numéro de vol ne doit pas être vide")
    @Size(min = 6, max = 6, message = "le numéro de vol doit être de 6 caractères")
    @Pattern(regexp = regex.UPPERCASE_ALPHA_REGEXP, message = "le numéro de vol n'est pas valide")
    @Column(unique = true, nullable = false)
    private String number;

    // Origine du vol, ne doit pas être vide
    @NotBlank(message = "l'origine du vol ne doit pas être vide")
    @Column(nullable = false)
    private String origin;

    // Destination du vol, ne doit pas être vide
    @NotBlank(message = "la destination du vol ne doit pas être vide")
    @Column(nullable = false)
    private String destination;

    // Date de départ du vol, ne doit pas être vide
    @NotNull(message = "l'heure départ du vol ne doit pas être vide")
    @Column(nullable = false)
    private LocalDate departureDate;

    // Heure de départ du vol, ne doit pas être vide
    @NotNull(message = "la date départ du vol ne doit pas être vide")
    @Column(nullable = false)
    private LocalTime departureTime;

    // Date d'arrivée du vol, ne doit pas être vide
    @NotNull(message = "l'heure d'arrivée du vol ne doit pas être vide")
    @Column(nullable = false)
    private LocalDate arrivalDate;

    // Heure d'arrivée du vol, ne doit pas être vide
    @NotNull(message = "la date d'arrivée du vol ne doit pas être vide")
    @Column(nullable = false)
    private LocalTime arrivalTime;

    // Avion associé au vol, ne doit pas être vide
    @NotNull(message = "le vol ne doit pas être vide")
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "planeId", referencedColumnName = "id", nullable = false)
    private Avion avion;

    // Liste des réservations associées au vol, ignorée lors de la sérialisation JSON
    @JsonIgnore
    @OneToMany(targetEntity = Reservation.class, mappedBy = "vol")
    private List<Reservation> reservation = new ArrayList<>();

}

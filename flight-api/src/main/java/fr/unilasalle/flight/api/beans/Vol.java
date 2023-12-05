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

    @Id
    @SequenceGenerator(name = "flights_sequence", sequenceName = "flights_sequence", allocationSize = 1, initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "flights_sequence")
    private Long id;

    @NotBlank(message = "Flight Number must not be empty")
    @Size(min = 6, max = 6, message = "Flight Number must be 6 characters long")
    @Pattern(regexp = regex.UPPERCASE_ALPHA_REGEXP, message = "Flight Number is invalid")
    @Column(unique = true, nullable = false)
    private String number;

    @NotBlank(message = "Flight Origin must not be empty")
    @Column(nullable = false)
    private String origin;

    @NotBlank(message = "Flight Destination must not be empty")
    @Column(nullable = false)
    private String destination;

    @NotNull(message = "Flight Departure Date must be provided")
    @Column(nullable = false)
    private LocalDate departureDate;

    @NotNull(message = "Flight Departure Time must be provided")
    @Column(nullable = false)
    private LocalTime departureTime;

    @NotNull(message = "Flight Arrival Date must be provided")
    @Column(nullable = false)
    private LocalDate arrivalDate;

    @NotNull(message = "Flight Arrival Time must be provided")
    @Column(nullable = false)
    private LocalTime arrivalTime;

    @NotNull(message = "Flight Plane must be provided")
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "planeId", referencedColumnName = "id", nullable = false)
    private Avion avion;

    @JsonIgnore
    @OneToMany(targetEntity = Reservation.class, mappedBy = "vol")
    private List<Reservation> reservation = new ArrayList<>();

}

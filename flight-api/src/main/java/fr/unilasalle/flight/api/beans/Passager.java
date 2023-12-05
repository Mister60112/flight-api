package fr.unilasalle.flight.api.beans;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fr.unilasalle.flight.api.constants.regex;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.enterprise.inject.Model;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Model
@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "passengers")
public class Passager extends PanacheEntityBase {

    @Id
    @SequenceGenerator(name = "passengers_sequence", sequenceName = "passengers_sequence", allocationSize = 1, initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "passengers_sequence")
    private Long id;

    @NotBlank(message = "Passenger Surname must not be empty")
    @Pattern(regexp = regex.ALPHA_REGEXP, message = "Passenger Surname is invalid")
    @Column(nullable = false)
    private String surname;

    @NotBlank(message = "Passenger Firstname must not be empty")
    @Pattern(regexp = regex.ALPHA_REGEXP, message = "Passenger Firstname is invalid")
    @Column(nullable = false)
    private String firstname;

    @NotBlank(message = "Passenger Email Address must not be empty")
    @Pattern(regexp = regex.EMAIL_REGEXP, message = "Passenger Email Address is not a valid email")
    @Size(max = 150, message = "Passenger Email must be least than 150 characters long")
    @Column(unique = true, nullable = false)
    private String emailAddress;

    @JsonIgnore
    @OneToMany(targetEntity = Reservation.class, mappedBy = "passager")
    private List<Reservation> reservations = new ArrayList<>();
}

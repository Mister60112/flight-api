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

    @NotBlank(message = "le nom du passager ne doit pas être vide")
    @Pattern(regexp = regex.ALPHA_REGEXP, message = "le nom du passager n'est pas valide")
    @Column(nullable = false)
    private String surname;

    @NotBlank(message = "le prénom du passager ne doit pas être vide")
    @Pattern(regexp = regex.ALPHA_REGEXP, message = "le prénom du passager n'est pas valide")
    @Column(nullable = false)
    private String firstname;

    @NotBlank(message = "le numéro de téléphone du passager ne doit pas être vide")
    @Pattern(regexp = regex.EMAIL_REGEXP, message = "le numéro de téléphone du passager n'est pas valide")
    @Size(max = 150, message = "le numéro de téléphone du passager ne doit pas dépasser 150 caractères")
    @Column(unique = true, nullable = false)
    private String emailAddress;

    @JsonIgnore
    @OneToMany(targetEntity = Reservation.class, mappedBy = "passager")
    private List<Reservation> reservations = new ArrayList<>();
}

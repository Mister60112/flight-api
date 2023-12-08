package fr.unilasalle.flight.api.beans;

import fr.unilasalle.flight.api.constants.regex;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.enterprise.inject.Model;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Model
@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "planes")
public class Avion extends PanacheEntityBase {

    // Identifiant unique généré automatiquement pour chaque avion
    @Id
    @SequenceGenerator(name = "planes_sequence", sequenceName = "planes_sequence", allocationSize = 1, initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "planes_sequence")
    private Long id;

    // Opérateur de l'avion, ne doit pas être vide et doit suivre le modèle alphabétique défini
    @NotBlank(message = "l'opérateur avion ne doit pas être vide")
    @Pattern(regexp = regex.ALPHA_REGEXP, message = "l'opérateur avion n'est pas valide")
    @Column(nullable = false)
    private String operator;

    // Modèle de l'avion, ne doit pas être vide et doit suivre le modèle alphabétique défini
    @NotBlank(message = "le modèle avion ne doit pas être vide")
    @Pattern(regexp = regex.ALPHA_REGEXP, message = "le modèle avion n'est pas valide")
    @Column(nullable = false)
    private String model;

    // Numéro d'immatriculation de l'avion, doit avoir une longueur de 6 caractères, être unique et suivre le modèle défini
    @NotBlank(message = "le numéro d'immatriculation avion ne doit pas être vide")
    @Size(min = 6, max = 6, message = "le numéro d'immatriculation avion doit être de 6 caractères")
    @Pattern(regexp = regex.UPPERCASE_ALPHA_REGEXP, message = "le numéro d'immatriculation avion n'est pas valide")
    @Column(unique = true, nullable = false)
    private String registration;

    // Capacité de sièges de l'avion, ne doit pas être vide et doit être supérieure à 2
    @NotNull(message = "le nombre de sièges avion ne doit pas être vide")
    @Min(value = 2, message = "le nombre de sièges avion doit être supérieur à 2")
    @Column(nullable = false)
    private Long capacity;

}

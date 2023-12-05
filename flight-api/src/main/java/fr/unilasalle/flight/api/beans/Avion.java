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

    @Id
    @SequenceGenerator(name = "planes_sequence", sequenceName = "planes_sequence", allocationSize = 1, initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "planes_sequence")
    private Long id;

    @NotBlank(message = "Plane Operator must not be empty")
    @Pattern(regexp = regex.ALPHA_REGEXP, message = "Plane Operator is invalid")
    @Column(nullable = false)
    private String operator;

    @NotBlank(message = "Plane Model must not be empty")
    @Pattern(regexp = regex.ALPHA_REGEXP, message = "Plane Operator is invalid")
    @Column(nullable = false)
    private String model;

    @NotBlank(message = "Plane Registration must not be empty")
    @Size(min = 6, max = 6, message = "Plane Registration must be 6 characters long")
    @Pattern(regexp = regex.UPPERCASE_ALPHA_REGEXP, message = "Plane Registration is invalid")
    @Column(unique = true, nullable = false)
    private String registration;

    @NotNull(message = "Plane Capacity must be provided")
    @Min(value = 2, message = "Plane Capacity must be at least 2...")
    @Column(nullable = false)
    private Long capacity;

}

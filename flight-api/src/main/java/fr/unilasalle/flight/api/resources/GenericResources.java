package fr.unilasalle.flight.api.resources;

import jakarta.validation.ConstraintViolation;
import jakarta.ws.rs.core.Response;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class GenericResources {

    // Gère la réponse HTTP en fonction de l'objet spécifié
    protected Response getOr404(Object object) {

        // Si la liste est vide, renvoie une réponse 404 sans contenu
        if (object instanceof List<?> && ((List<?>) object).isEmpty()) {
            return Response.noContent().status(404).build();
        }

        // Si l'objet n'est pas nul, renvoie une réponse 200 OK avec l'objet
        if (object != null) {
            return Response.ok(object).status(200).build();
        } else {
            // Sinon, renvoie une réponse 404 sans contenu
            return Response.noContent().status(404).build();
        }
    }

    @Getter
    protected static class ErrorWrapper {

        // Message d'erreur global
        private final String message;

        // Liste des messages d'erreur détaillés
        private List<String> errors = new ArrayList<>();

        // Constructeur pour les messages d'erreur généraux
        ErrorWrapper(String message) {
            this.message = message;
        }

        // Constructeur pour les messages d'erreur de validation
        ErrorWrapper(Set<? extends ConstraintViolation<?>> violations) {
            this.message = "";
            this.errors = violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .toList();
        }
    }
}

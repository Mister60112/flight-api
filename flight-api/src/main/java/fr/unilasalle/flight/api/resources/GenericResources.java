package fr.unilasalle.flight.api.resources;

import jakarta.validation.ConstraintViolation;
import jakarta.ws.rs.core.Response;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class GenericResources {

    protected Response getOr404(Object object) {

        if (object instanceof List<?> && ((List<?>) object).isEmpty()) {
            return Response.noContent().status(404).build();
        }

        if (object != null) {
            return Response.ok(object).status(200).build();
        } else {
            return Response.noContent().status(404).build();
        }
    }

    @Getter
    protected static class ErrorWrapper {

        private final String message;

        private List<String> errors = new ArrayList<>();

        ErrorWrapper(String message) {
            this.message = message;
        }

        ErrorWrapper(Set<? extends ConstraintViolation<?>> violations) {
            this.message = "";
            this.errors = violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .toList();
        }


    }
}

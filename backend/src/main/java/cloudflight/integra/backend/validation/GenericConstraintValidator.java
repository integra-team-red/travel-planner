package cloudflight.integra.backend.validation;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.util.Set;
import org.hibernate.validator.constraints.Length;
import org.springframework.stereotype.Component;

@Component
public class GenericConstraintValidator<T> implements GenericValidator<T> {
    private final Validator validator;

    public GenericConstraintValidator(Validator validator) {
        this.validator = validator;
    }

    public void validate(T validationObject) throws ConstraintViolationException {
        Set<ConstraintViolation<T>> totalViolations = validator.validate(validationObject);

        if (!totalViolations.isEmpty()) {
            throw new ConstraintViolationException(formatExceptionMessage(totalViolations), totalViolations);
        }
    }

    // NOTE(MC): I wanted to extract this into a static class and break down the loop into functions for each
    // constraint,
    // but then I realized that I don't know how I'd have one function just for *any other constraints* and make it look
    // pretty
    // so here we are
    private String formatExceptionMessage(Set<ConstraintViolation<T>> violations) {
        var exceptionMessage = new StringBuilder();
        var lengthMismatchErrors = new StringBuilder();
        var absentFieldErrors = new StringBuilder();
        var patternMismatchErrors = new StringBuilder();
        for (var violation : violations) {
            switch (violation.getConstraintDescriptor().getAnnotation()) {
                case Length _ -> {
                    lengthMismatchErrors.append(violation.getMessage()).append('\n');
                }
                case NotNull _ -> {
                    absentFieldErrors.append(violation.getPropertyPath()).append('\n');
                }
                case Pattern _ -> {
                    patternMismatchErrors.append(violation.getMessage()).append('\n');
                }
                default -> {
                    exceptionMessage
                            .append("Anonymous violation found on field ")
                            .append(violation.getPropertyPath())
                            .append("\n");
                }
            }
        }
        if (!lengthMismatchErrors.isEmpty()) {
            exceptionMessage.append("Field length violations:\n").append(lengthMismatchErrors);
        }
        if (!absentFieldErrors.isEmpty()) {
            exceptionMessage.append("Missing fields:\n").append(absentFieldErrors);
        }
        if (!patternMismatchErrors.isEmpty()) {
            exceptionMessage.append("Field pattern violations:\n").append(patternMismatchErrors);
        }
        return exceptionMessage.toString();
    }
}

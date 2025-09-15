package cloudflight.integra.backend.validation;

public class ValidationError extends RuntimeException {
    public ValidationError(String message) {
        super(message);
    }
}

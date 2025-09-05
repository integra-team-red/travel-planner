package cloudflight.integra.backend.model.validation;

public class ProposalValidationError extends RuntimeException {
    public ProposalValidationError(String message) {
        super(message);
    }
}

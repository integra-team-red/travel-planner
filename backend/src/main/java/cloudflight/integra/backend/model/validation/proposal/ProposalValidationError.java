package cloudflight.integra.backend.model.validation.proposal;

import cloudflight.integra.backend.model.validation.ValidationError;

public class ProposalValidationError extends ValidationError {
    public ProposalValidationError(String message) {
        super(message);
    }
}

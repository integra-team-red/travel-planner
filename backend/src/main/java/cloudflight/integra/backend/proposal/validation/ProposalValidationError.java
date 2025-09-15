package cloudflight.integra.backend.proposal.validation;

import cloudflight.integra.backend.validation.ValidationError;

public class ProposalValidationError extends ValidationError {
    public ProposalValidationError(String message) {
        super(message);
    }
}

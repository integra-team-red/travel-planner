package cloudflight.integra.backend.model.validation;
import cloudflight.integra.backend.model.Proposal;

public interface ProposalValidation {
    void validate(Proposal proposal) throws ProposalValidationError;
}

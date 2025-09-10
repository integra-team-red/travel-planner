package cloudflight.integra.backend.model.validation.proposal;

import cloudflight.integra.backend.model.Proposal;
import cloudflight.integra.backend.model.validation.GenericValidator;
import org.springframework.stereotype.Component;

@Component
public class ProposalValidator implements GenericValidator<Proposal> {
    @Override
    public void validate(Proposal proposal) {
        if (proposal == null) {
            throw new ProposalValidationError("it cannot be null!!!");
        }
        if (proposal.getName().length()<3) {
            throw new ProposalValidationError("the length of proposal is too short");
        }
    }
}

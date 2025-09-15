package cloudflight.integra.backend.proposal.validation;

import cloudflight.integra.backend.proposal.Proposal;
import cloudflight.integra.backend.validation.GenericValidator;
import org.springframework.stereotype.Component;

@Component
public class ProposalValidator implements GenericValidator<Proposal> {
    @Override
    public void validate(Proposal proposal) {
        if (proposal == null) {
            throw new ProposalValidationError("it cannot be null!!!");
        }
        if (proposal.getName()
                .length() < 3) {
            throw new ProposalValidationError("the length of proposal is too short");
        }
    }
}

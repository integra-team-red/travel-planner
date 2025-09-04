package cloudflight.integra.backend.model.validation;

import cloudflight.integra.backend.model.Proposal;
import cloudflight.integra.backend.model.Type;
import org.springframework.stereotype.Component;

@Component
public class ProposalValidator implements ProposalValidation {
    @Override
    public void validate(Proposal proposal) {
        if (proposal == null) {
            throw new ProposalValidationError("it cannot be null!!!");
        }
        if (proposal.getType()!= Type.RESTAURANT || proposal.getType()!=Type.POINT_OF_INTEREST) {
            throw new ProposalValidationError("proposal type is invalid");
        }
        if (proposal.getName().length()<3) {
            throw new ProposalValidationError("the length of proposal is too short");
        }
    }
}

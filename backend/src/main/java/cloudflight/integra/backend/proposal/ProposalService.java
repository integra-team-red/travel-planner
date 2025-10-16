package cloudflight.integra.backend.proposal;

import java.util.List;

public interface ProposalService {
    Proposal addProposal(Proposal proposal);

    Proposal updateProposal(Long id, Proposal proposal);

    void deleteProposal(Long id);

    Proposal getProposal(Long id);

    List<Proposal> getAllProposals();
}

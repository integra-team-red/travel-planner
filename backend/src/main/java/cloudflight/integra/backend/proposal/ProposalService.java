package cloudflight.integra.backend.proposal;

import java.util.List;
import java.util.Optional;

public interface ProposalService {
    Proposal save(Proposal proposal);

    List<Proposal> findAll();

    Optional<Proposal> findById(Long id);

    Proposal deleteById(Long id);

    Proposal update(Proposal proposal);
}

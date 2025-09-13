package cloudflight.integra.backend.service;

import cloudflight.integra.backend.model.Proposal;
import java.util.List;
import java.util.Optional;

public interface ServiceProposal {
    Proposal save(Proposal proposal);

    List<Proposal> findAll();

    Optional<Proposal> findById(Long id);

    Proposal deleteById(Long id);

    Proposal update(Proposal proposal);
}

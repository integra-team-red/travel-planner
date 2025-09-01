package cloudflight.integra.backend.repository;

import cloudflight.integra.backend.model.Proposal;

import java.util.List;
import java.util.Optional;

public interface RepositoryProposalInterface {
    Proposal save(Proposal proposal);
    Optional<Proposal> findById(Long id);
    Optional<Proposal> findByName(String name);
    List<Proposal> findAll();
    void deleteById(Long id);
    Proposal update(Proposal proposal);
}

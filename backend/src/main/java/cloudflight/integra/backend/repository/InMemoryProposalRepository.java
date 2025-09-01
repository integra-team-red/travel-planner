package cloudflight.integra.backend.repository;

import cloudflight.integra.backend.model.Proposal;
import cloudflight.integra.backend.model.Type;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class InMemoryProposalRepository implements RepositoryProposalInterface{
    private final List<Proposal> entities = new ArrayList<>();
    public InMemoryProposalRepository(){
        entities.add(new Proposal(1L, "DaPino", Type.RESTAURANT));
    }
    @Override
    public Proposal save(Proposal proposal) {
        entities.add(proposal);
        return proposal;
    }
    @Override
    public Optional<Proposal> findById(Long id) {
        return entities.stream().filter(proposal -> proposal.getId().equals(id)).findFirst();
    }
    @Override
    public Optional<Proposal> findByName(String name) {
        return entities.stream() .filter(proposal -> proposal.getName().equalsIgnoreCase(name)) .findFirst();
    }
    @Override
    public List<Proposal> findAll() {
        return entities;
    }
    @Override
    public void deleteById(Long id) {
        entities.removeIf(p -> p.getId().equals(id));
    }

    @Override
    public Proposal update(Proposal proposal) {
        deleteById(proposal.getId());
        entities.add(proposal);
        return proposal;
    }
}

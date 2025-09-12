package cloudflight.integra.backend.service;

import cloudflight.integra.backend.model.Proposal;
import cloudflight.integra.backend.model.Status;
import cloudflight.integra.backend.model.validation.proposal.ProposalValidator;
import cloudflight.integra.backend.repository.ProposalRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProposalServiceImpl implements ServiceProposal {

    private final ProposalRepository proposalRepository;
    private final ProposalValidator proposalValidator;

    @Autowired
    public ProposalServiceImpl(ProposalRepository proposalRepository, ProposalValidator proposalValidator) {
        this.proposalRepository = proposalRepository;
        this.proposalValidator = proposalValidator;
    }

    @Override
    public Proposal update(Proposal proposal) {
        proposalValidator.validate(proposal);
        Proposal p = proposalRepository.findById(proposal.getId())
                .orElseThrow(() -> new RuntimeException("Proposal not found: " + proposal.getId()));
        p.setName(proposal.getName());
        p.setStatus(proposal.getStatus());
        p.setType(proposal.getType());
        return proposalRepository.save(p);
    }

    @Override
    public Proposal deleteById(Long id) {
        Proposal p = proposalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proposal not found: " + id));
        proposalRepository.delete(p);
        return p;
    }

    @Override
    public Optional<Proposal> findById(Long id) {
        return proposalRepository.findById(id);
    }

    @Override
    public List<Proposal> findAll() {
        return proposalRepository.findAll();
    }

    @Override
    public Proposal save(Proposal proposal) {
        proposal.setStatus(Status.PENDING);
        proposalValidator.validate(proposal);
        return proposalRepository.save(proposal);
    }
}

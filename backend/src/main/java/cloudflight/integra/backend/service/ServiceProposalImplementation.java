package cloudflight.integra.backend.service;

import cloudflight.integra.backend.model.Proposal;
import cloudflight.integra.backend.model.validation.ProposalValidator;
import cloudflight.integra.backend.repository.RepositoryProposalInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ServiceProposalImplementation implements ServiceProposal {
    private RepositoryProposalInterface repositoryProposal;
    private ProposalValidator proposalValidator;

    @Autowired
    public ServiceProposalImplementation(RepositoryProposalInterface repositoryProposal, ProposalValidator proposalValidator){
        this.repositoryProposal = repositoryProposal;
        this.proposalValidator = proposalValidator;
    }
    @Override
    public Proposal save(Proposal proposal) {
        proposalValidator.validate(proposal);
        return repositoryProposal.save(proposal);
    }
    @Override
    public Optional<Proposal> findByName(String name){
        return repositoryProposal.findByName(name);
    }
    @Override
    public List<Proposal> findAll(){
        return repositoryProposal.findAll();
    }
    @Override
    public void deleteById(Long id) {
        repositoryProposal.deleteById(id);
    }
    @Override
    public Optional<Proposal> findById(Long id) {
        return repositoryProposal.findById(id);
    }
    @Override
    public Proposal update(Proposal proposal) {
        proposalValidator.validate(proposal);
        return repositoryProposal.update(proposal);
    }
}

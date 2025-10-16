package cloudflight.integra.backend.proposal;

import cloudflight.integra.backend.validation.GenericConstraintValidator;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProposalServiceImpl implements ProposalService {

    private final DBProposalRepository repository;
    private final GenericConstraintValidator<Proposal> validator;

    @Autowired
    public ProposalServiceImpl(DBProposalRepository repository, GenericConstraintValidator<Proposal> validator) {
        this.repository = repository;
        this.validator = validator;
    }

    @Override
    public Proposal addProposal(Proposal proposal) {
        proposal.setStatus(Status.PENDING);
        validator.validate(proposal);
        return repository.save(proposal);
    }

    @Override
    public Proposal updateProposal(Long id, Proposal proposal) {
        validator.validate(proposal);
        var dbProposal = getProposal(id);

        dbProposal.setName(proposal.getName());
        dbProposal.setType(proposal.getType());
        dbProposal.setCity(proposal.getCity());
        dbProposal.setCuisineType(proposal.getCuisineType());
        dbProposal.setDescription(proposal.getDescription());
        dbProposal.setAveragePrice(proposal.getAveragePrice());
        dbProposal.setPoiType(proposal.getPoiType());
        dbProposal.setPrice(proposal.getPrice());

        return repository.save(dbProposal);
    }

    @Override
    public void deleteProposal(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Proposal getProposal(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("No proposal id provided");
        }
        var proposalWrapper = repository.findById(id);
        if (proposalWrapper.isEmpty()) {
            throw new EntityNotFoundException("Proposal with the provided id not found");
        }
        return proposalWrapper.get();
    }

    @Override
    public List<Proposal> getAllProposals() {
        return repository.findAll();
    }
}

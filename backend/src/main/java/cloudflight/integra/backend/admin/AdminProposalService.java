package cloudflight.integra.backend.admin;

import cloudflight.integra.backend.poi.DBPOIRepository;
import cloudflight.integra.backend.poi.POI;
import cloudflight.integra.backend.proposal.*;
import cloudflight.integra.backend.restaurant.DBRestaurantRepository;
import cloudflight.integra.backend.restaurant.Restaurant;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminProposalService {
    private final DBProposalRepository proposalRepository;
    private final DBPOIRepository poiRepository;
    private final DBRestaurantRepository restaurantRepository;

    @Autowired
    public AdminProposalService(
            DBProposalRepository proposalRepository,
            DBPOIRepository poiRepository,
            DBRestaurantRepository restaurantRepository) {
        this.proposalRepository = proposalRepository;
        this.poiRepository = poiRepository;
        this.restaurantRepository = restaurantRepository;
    }

    public Proposal approveProposal(Long proposalId) {
        Proposal proposal = proposalRepository
                .findById(proposalId)
                .orElseThrow(() -> new EntityNotFoundException("Proposal not found"));
        if (proposal.getStatus() != Status.PENDING) {
            throw new IllegalStateException("Only pending proposals can be approved");
        }

        proposal.setStatus(Status.APPROVED);
        Proposal updatedProposal = proposalRepository.save(proposal);

        if (proposal.getType() == ProposalType.RESTAURANT) {
            Restaurant restaurant = new Restaurant()
                    .setName(proposal.getName())
                    .setCity(proposal.getCity())
                    .setAveragePrice(proposal.getAveragePrice())
                    .setCuisineType(proposal.getCuisineType());

            restaurantRepository.save(restaurant);

        } else if (proposal.getType() == ProposalType.POINT_OF_INTEREST) {
            POI poi = new POI()
                    .setName(proposal.getName())
                    .setDescription(proposal.getDescription())
                    .setCity(proposal.getCity())
                    .setPrice(proposal.getPrice())
                    .setType(proposal.getPoiType());

            poiRepository.save(poi);
        }

        return updatedProposal;
    }

    public Proposal rejectProposal(Long proposalId) {
        Proposal proposal = proposalRepository
                .findById(proposalId)
                .orElseThrow(() -> new EntityNotFoundException("Proposal not found"));
        if (proposal.getStatus() != Status.PENDING) {
            throw new IllegalStateException("Only pending proposals can be rejected");
        }
        proposal.setStatus(Status.REJECTED);
        return proposalRepository.save(proposal);
    }

    public List<Proposal> getPendingProposals() {
        return proposalRepository.findByStatus(Status.PENDING);
    }
}

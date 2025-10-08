package cloudflight.integra.backend.admin;

import cloudflight.integra.backend.poi.DBPOIRepository;
import cloudflight.integra.backend.poi.PointOfInterest;
import cloudflight.integra.backend.proposal.*;
import cloudflight.integra.backend.restaurant.DBRestaurantRepository;
import cloudflight.integra.backend.restaurant.Restaurant;
import cloudflight.integra.backend.user.*;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminProposalService {
    private final DBUserRepository userRepository;
    private final ProposalRepository proposalRepository;
    private final DBPOIRepository poiRepository;
    private final DBRestaurantRepository restaurantRepository;

    @Autowired
    public AdminProposalService(
            DBUserRepository userRepository,
            ProposalRepository proposalRepository,
            DBPOIRepository poiRepository,
            DBRestaurantRepository restaurantRepository) {
        this.userRepository = userRepository;
        this.proposalRepository = proposalRepository;
        this.poiRepository = poiRepository;
        this.restaurantRepository = restaurantRepository;
    }

    public List<Proposal> getAllProposals() {
        return proposalRepository.findAll();
    }

    public Proposal approveProposal(Long proposalId, Long userId) {
        User user = userRepository
                .findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("user hasn't been found"));
        if (user.getRole() != Role.ADMIN) {
            throw new SecurityException("user must be an admin!!");
        }
        Proposal proposal = proposalRepository
                .findById(proposalId)
                .orElseThrow(() -> new EntityNotFoundException("Proposal not found"));
        if (proposal.getStatus() != Status.PENDING)
            throw new RuntimeException("Only pending proposals can be approved");

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
            PointOfInterest poi = new PointOfInterest()
                    .setName(proposal.getName())
                    .setDescription(proposal.getDescription())
                    .setCity(proposal.getCity())
                    .setPrice(proposal.getPrice())
                    .setType(proposal.getPoiType());

            poiRepository.save(poi);
        }

        return updatedProposal;
    }

    public Proposal rejectProposal(Long proposalId, Long userId) {
        User user = userRepository
                .findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("user hasn't been found"));
        Proposal proposal = proposalRepository
                .findById(proposalId)
                .orElseThrow(() -> new EntityNotFoundException("Proposal not found"));
        if (proposal.getStatus() != Status.PENDING)
            throw new RuntimeException("Only pending proposals can be approved");
        proposal.setStatus(Status.REJECTED);
        return proposalRepository.save(proposal);
    }

    public List<Proposal> getPendingApprovals() {
        return proposalRepository.findByStatus(Status.PENDING);
    }
}

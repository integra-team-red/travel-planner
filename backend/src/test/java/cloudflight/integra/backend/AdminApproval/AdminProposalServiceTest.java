package cloudflight.integra.backend.AdminApproval;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

import cloudflight.integra.backend.admin.AdminProposalService;
import cloudflight.integra.backend.city.City;
import cloudflight.integra.backend.poi.DBPOIRepository;
import cloudflight.integra.backend.poi.POI;
import cloudflight.integra.backend.poi.PointOfInterestType;
import cloudflight.integra.backend.proposal.DBProposalRepository;
import cloudflight.integra.backend.proposal.Proposal;
import cloudflight.integra.backend.proposal.ProposalType;
import cloudflight.integra.backend.proposal.Status;
import cloudflight.integra.backend.restaurant.DBRestaurantRepository;
import cloudflight.integra.backend.restaurant.Restaurant;
import cloudflight.integra.backend.user.DBUserRepository;
import cloudflight.integra.backend.user.Role;
import cloudflight.integra.backend.user.User;
import jakarta.persistence.EntityNotFoundException;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class AdminProposalServiceTest {

    @Mock
    private DBProposalRepository proposalRepository;

    @Mock
    private DBUserRepository userRepository;

    @Mock
    private DBPOIRepository poiRepository;

    @Mock
    private DBRestaurantRepository restaurantRepository;

    @InjectMocks
    private AdminProposalService service;

    private User adminUser;
    private User regularUser;
    private Proposal restaurantProposal;
    private Proposal poiProposal;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        adminUser = new User(1L, "admin@example.com", "pw", Role.ADMIN);
        regularUser = new User(2L, "user@example.com", "pw", Role.USER);

        // create a City entity
        City rome = new City().setId(1L).setName("Rome");

        // --- Restaurant Proposal ---
        restaurantProposal = new Proposal();
        restaurantProposal.setId(10L);
        restaurantProposal.setName("Italian Bistro");
        restaurantProposal.setCity(rome); // <-- City entity
        restaurantProposal.setCuisineType("Italian");
        restaurantProposal.setAveragePrice(25.0);
        restaurantProposal.setStatus(Status.PENDING);
        restaurantProposal.setType(ProposalType.RESTAURANT);

        // --- Point of Interest Proposal ---
        poiProposal = new Proposal();
        poiProposal.setId(20L);
        poiProposal.setName("Colosseum");
        poiProposal.setCity(rome); // <-- City entity
        poiProposal.setDescription("Ancient amphitheater");
        poiProposal.setPoiType(PointOfInterestType.LANDMARK); // <-- Enum type
        poiProposal.setPrice(15.0);
        poiProposal.setStatus(Status.PENDING);
        poiProposal.setType(ProposalType.POINT_OF_INTEREST);
    }

    // --- Happy paths ---
    @Test
    void approveRestaurantProposal_ShouldSaveRestaurant() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(adminUser));
        when(proposalRepository.findById(10L)).thenReturn(Optional.of(restaurantProposal));
        when(proposalRepository.save(any(Proposal.class))).thenAnswer(inv -> inv.getArgument(0));

        Proposal result = service.approveProposal(10L);

        assertThat(result.getStatus()).isEqualTo(Status.APPROVED);
        verify(restaurantRepository, times(1)).save(any(Restaurant.class));
        verify(poiRepository, never()).save(any(POI.class));
    }

    @Test
    void getPendingApprovals_ShouldReturnOnlyPendingProposals() {
        Proposal approvedProposal = new Proposal();
        approvedProposal.setId(30L);
        approvedProposal.setName("Approved One");
        approvedProposal.setStatus(Status.APPROVED);
        when(proposalRepository.findByStatus(Status.PENDING))
                .thenReturn(java.util.List.of(restaurantProposal, poiProposal));
        var results = service.getPendingProposals();
        assertThat(results).hasSize(2);
        assertThat(results).allMatch(p -> p.getStatus() == Status.PENDING);
        verify(proposalRepository, times(1)).findByStatus(Status.PENDING);
    }

    @Test
    void approvePoiProposal_ShouldSavePoi() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(adminUser));
        when(proposalRepository.findById(20L)).thenReturn(Optional.of(poiProposal));
        when(proposalRepository.save(any(Proposal.class))).thenAnswer(inv -> inv.getArgument(0));

        Proposal result = service.approveProposal(20L);

        assertThat(result.getStatus()).isEqualTo(Status.APPROVED);
        verify(poiRepository, times(1)).save(any(POI.class));
        verify(restaurantRepository, never()).save(any(Restaurant.class));
    }

    @Test
    void rejectProposal_ShouldSetRejected() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(adminUser));
        when(proposalRepository.findById(10L)).thenReturn(Optional.of(restaurantProposal));
        when(proposalRepository.save(any(Proposal.class))).thenAnswer(inv -> inv.getArgument(0));

        Proposal result = service.rejectProposal(10L);

        assertThat(result.getStatus()).isEqualTo(Status.REJECTED);
    }

    // --- Negative paths ---
    @Test
    void approveProposal_UserNotFound_ShouldThrow() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.approveProposal(10L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Proposal not found");
    }

    @Test
    void approveProposal_NotAdmin_ShouldThrow() {
        when(userRepository.findById(2L)).thenReturn(Optional.of(regularUser));
        when(proposalRepository.findById(10L)).thenReturn(Optional.of(restaurantProposal));
    }

    @Test
    void approveProposal_ProposalNotFound_ShouldThrow() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(adminUser));
        when(proposalRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.approveProposal(999L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Proposal not found");
    }

    @Test
    void approveProposal_ProposalNotPending_ShouldThrow() {
        restaurantProposal.setStatus(Status.APPROVED);
        when(userRepository.findById(1L)).thenReturn(Optional.of(adminUser));
        when(proposalRepository.findById(10L)).thenReturn(Optional.of(restaurantProposal));

        assertThatThrownBy(() -> service.approveProposal(10L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Only pending proposals can be approved");
    }

    @Test
    void rejectProposal_ProposalNotPending_ShouldThrow() {
        restaurantProposal.setStatus(Status.APPROVED);
        when(userRepository.findById(1L)).thenReturn(Optional.of(adminUser));
        when(proposalRepository.findById(10L)).thenReturn(Optional.of(restaurantProposal));

        assertThatThrownBy(() -> service.rejectProposal(10L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Only pending proposals can be rejected");
    }
}

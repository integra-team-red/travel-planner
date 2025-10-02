package cloudflight.integra.backend.admin;

import cloudflight.integra.backend.proposal.*;
import cloudflight.integra.backend.user.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/proposals")
@SecurityRequirement(name = "bearerAuth")
public class AdminProposalController {
    private final AdminProposalService adminProposalService;
    private final DBUserRepository userRepository;

    @Autowired
    public AdminProposalController(AdminProposalService adminProposalService, DBUserRepository userRepository) {
        this.adminProposalService = adminProposalService;
        this.userRepository = userRepository;
    }

    @Operation(
            summary = "Get all pending proposals awaiting admin approval",
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Pending proposals returned successfully",
                        content =
                                @Content(
                                        mediaType = "application/json",
                                        array = @ArraySchema(schema = @Schema(implementation = ProposalDTO.class)))),
                @ApiResponse(responseCode = "403", description = "User is not an admin", content = @Content)
            })
    @GetMapping("/pending")
    public ResponseEntity<List<ProposalDTO>> getPendingApprovals(Authentication authentification) {
        List<ProposalDTO> pendingProposals = adminProposalService.getPendingApprovals().stream()
                .map(ProposalMapper::ProposalToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(pendingProposals);
    }

    @Operation(
            summary = "Approve a pending proposal",
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Proposal approved successfully",
                        content =
                                @Content(
                                        mediaType = "application/json",
                                        schema = @Schema(implementation = ProposalDTO.class))),
                @ApiResponse(responseCode = "403", description = "User is not an admin", content = @Content),
                @ApiResponse(responseCode = "404", description = "Proposal not found", content = @Content)
            })
    @PostMapping("/{id}/approve")
    public ResponseEntity<ProposalDTO> approveProposal(@PathVariable Long id, Authentication authentication) {
        User admin = userRepository.findUserByEmail(authentication.getName());
        Proposal approved = adminProposalService.approveProposal(id, admin.getId());
        return ResponseEntity.ok(ProposalMapper.ProposalToDTO(approved));
    }

    @Operation(
            summary = "Reject a pending proposal",
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Proposal rejected successfully",
                        content =
                                @Content(
                                        mediaType = "application/json",
                                        schema = @Schema(implementation = ProposalDTO.class))),
                @ApiResponse(responseCode = "403", description = "User is not an admin", content = @Content),
                @ApiResponse(responseCode = "404", description = "Proposal not found", content = @Content)
            })
    @PostMapping("/{id}/reject")
    public ResponseEntity<ProposalDTO> rejectProposal(@PathVariable Long id, Authentication authentication) {
        User admin = userRepository.findUserByEmail(authentication.getName());
        Proposal rejected = adminProposalService.rejectProposal(id, admin.getId());
        return ResponseEntity.ok(ProposalMapper.ProposalToDTO(rejected));
    }
}

package cloudflight.integra.backend.admin;

import cloudflight.integra.backend.proposal.*;
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
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/proposals")
@SecurityRequirement(name = "bearerAuth")
public class AdminProposalController {
    private final AdminProposalService adminProposalService;

    @Autowired
    public AdminProposalController(AdminProposalService adminProposalService) {
        this.adminProposalService = adminProposalService;
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
            })
    @GetMapping("/pending")
    public ResponseEntity<List<ProposalDTO>> getPendingProposals() {
        List<ProposalDTO> pendingProposals = adminProposalService.getPendingProposals().stream()
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
                @ApiResponse(responseCode = "404", description = "Proposal not found", content = @Content),
                @ApiResponse(
                        responseCode = "409",
                        description = "Only pending proposals can be approved",
                        content = @Content)
            })
    @PostMapping("/{id}/approve")
    public ResponseEntity<ProposalDTO> approveProposal(@PathVariable Long id) {
        Proposal approved = adminProposalService.approveProposal(id);
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
                @ApiResponse(responseCode = "404", description = "Proposal not found", content = @Content),
                @ApiResponse(
                        responseCode = "409",
                        description = "Only pending proposals can be rejected",
                        content = @Content)
            })
    @PostMapping("/{id}/reject")
    public ResponseEntity<ProposalDTO> rejectProposal(@PathVariable Long id) {
        Proposal rejected = adminProposalService.rejectProposal(id);
        return ResponseEntity.ok(ProposalMapper.ProposalToDTO(rejected));
    }
}

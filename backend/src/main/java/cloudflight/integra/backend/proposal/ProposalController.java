package cloudflight.integra.backend.proposal;

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
@RequestMapping("/proposal")
@SecurityRequirement(name = "bearerAuth")
public class ProposalController {
    private ProposalServiceImpl proposalService;

    @Autowired
    private void setProposalService(ProposalServiceImpl serviceProposal) {
        this.proposalService = serviceProposal;
    }

    @Operation(
            summary = "Add a proposal to the repository",
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Proposal added successfully; returns the added proposal",
                        content =
                                @Content(
                                        mediaType = "application/json",
                                        schema = @Schema(implementation = ProposalDTO.class))),
                @ApiResponse(responseCode = "5xx", description = "Invalid proposal supplied", content = @Content)
            })
    @PostMapping
    public ResponseEntity<ProposalDTO> createProposal(@RequestBody ProposalDTO proposalDTO) {
        Proposal savedProposal = proposalService.save(ProposalMapper.ProposalToEntity(proposalDTO));
        return ResponseEntity.ok(ProposalMapper.ProposalToDTO(savedProposal));
    }

    @Operation(
            summary = "Get a list of all proposals from the repository",
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "All proposals returned successfully",
                        content =
                                @Content(
                                        mediaType = "application/json",
                                        array = @ArraySchema(schema = @Schema(implementation = ProposalDTO.class)))),
            })
    @GetMapping
    public ResponseEntity<List<ProposalDTO>> getAllProposals() {
        List<ProposalDTO> proposals = proposalService.findAll().stream()
                .map(ProposalMapper::ProposalToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(proposals);
    }

    @Operation(
            summary = "Get a proposal from the repository",
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Proposal returned successfully",
                        content =
                                @Content(
                                        mediaType = "application/json",
                                        schema = @Schema(implementation = ProposalDTO.class))),
                @ApiResponse(responseCode = "5xx", description = "Proposal not found", content = @Content)
            })
    @GetMapping("/{id}")
    public ResponseEntity<ProposalDTO> getProposalById(@PathVariable("id") Long id) {
        Proposal proposal =
                proposalService.findById(id).orElseThrow(() -> new RuntimeException("Proposal not found: " + id));

        return ResponseEntity.ok(ProposalMapper.ProposalToDTO(proposal));
    }

    @Operation(
            summary = "Update a proposal's data with the provided proposal",
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Proposal updated successfully; returns the updated proposal",
                        content =
                                @Content(
                                        mediaType = "application/json",
                                        schema = @Schema(implementation = ProposalDTO.class))),
                @ApiResponse(
                        responseCode = "5xx",
                        description = "Invalid proposal supplied / Proposal not found",
                        content = @Content),
            })
    @PutMapping("/{id}")
    public ResponseEntity<ProposalDTO> updateProposal(
            @PathVariable("id") Long id, @RequestBody ProposalDTO newProposalDTO) {
        Proposal proposalToUpdate = ProposalMapper.ProposalToEntity(newProposalDTO);
        proposalToUpdate.setId(id);
        Proposal updatedProposal = proposalService.update(proposalToUpdate);
        return ResponseEntity.ok(ProposalMapper.ProposalToDTO(updatedProposal));
    }

    @Operation(
            summary = "Delete a proposal from the repository",
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Proposal deleted successfully; returns the deleted proposal",
                        content =
                                @Content(
                                        mediaType = "application/json",
                                        schema = @Schema(implementation = ProposalDTO.class)))
            })
    @DeleteMapping("/{id}")
    public ResponseEntity<ProposalDTO> deleteProposal(@PathVariable("id") Long id) {
        Proposal deletedProposal = proposalService.deleteById(id);
        return ResponseEntity.ok(ProposalMapper.ProposalToDTO(deletedProposal));
    }
}

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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/proposal")
@SecurityRequirement(name = "bearerAuth")
public class ProposalController {
    private final ProposalService service;

    @Autowired
    public ProposalController(ProposalService service) {
        this.service = service;
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
                @ApiResponse(responseCode = "422", description = "Invalid proposal supplied", content = @Content)
            })
    @PostMapping
    public ResponseEntity<ProposalDTO> addProposal(@RequestBody ProposalDTO proposalDTO) {
        Proposal savedProposal = service.addProposal(ProposalMapper.ProposalToEntity(proposalDTO));
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
    public ResponseEntity<List<ProposalDTO>> getProposals() {
        List<ProposalDTO> proposals = service.getAllProposals().stream()
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
                @ApiResponse(responseCode = "404", description = "Proposal ID not found", content = @Content)
            })
    @GetMapping("/{id}")
    public ResponseEntity<ProposalDTO> getProposalById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(ProposalMapper.ProposalToDTO(service.getProposal(id)));
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
                @ApiResponse(responseCode = "422", description = "Invalid proposal supplied", content = @Content),
                @ApiResponse(responseCode = "404", description = "Proposal ID not found", content = @Content)
            })
    @PutMapping("/{id}")
    public ResponseEntity<ProposalDTO> updateProposal(@PathVariable Long id, @RequestBody ProposalDTO newProposalDTO) {
        Proposal updatedProposal = service.updateProposal(id, ProposalMapper.ProposalToEntity(newProposalDTO));
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
    public ResponseEntity<ProposalDTO> deleteProposal(@PathVariable Long id) {
        service.deleteProposal(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}

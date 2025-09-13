package cloudflight.integra.backend.controller;

import cloudflight.integra.backend.DTO.ProposalDTO;
import cloudflight.integra.backend.mapper.ProposalMapper;
import cloudflight.integra.backend.model.Proposal;
import cloudflight.integra.backend.service.ProposalServiceImpl;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/proposal")
public class ProposalController {
    private ProposalServiceImpl proposalService;

    @Autowired
    private void setProposalService(ProposalServiceImpl serviceProposal) { this.proposalService = serviceProposal; }

    @PostMapping
    public ResponseEntity<ProposalDTO> createProposal(@RequestBody ProposalDTO proposalDTO) {
        Proposal savedProposal = proposalService.save(ProposalMapper.ProposalToEntity(proposalDTO));
        return ResponseEntity.ok(ProposalMapper.ProposalToDTO(savedProposal));
    }

    @GetMapping
    public ResponseEntity<List<ProposalDTO>> getAllProposals() {
        List<ProposalDTO> proposals = proposalService.findAll()
                .stream()
                .map(ProposalMapper::ProposalToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(proposals);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProposalDTO> getProposalById(@PathVariable("id") Long id) {
        Proposal proposal = proposalService.findById(id)
                .orElseThrow(() -> new RuntimeException("Proposal not found: " + id));

        return ResponseEntity.ok(ProposalMapper.ProposalToDTO(proposal));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProposalDTO> updateProposal(@PathVariable("id") Long id,
                                                      @RequestBody ProposalDTO newProposalDTO) {
        Proposal proposalToUpdate = ProposalMapper.ProposalToEntity(newProposalDTO);
        proposalToUpdate.setId(id);
        Proposal updatedProposal = proposalService.update(proposalToUpdate);
        return ResponseEntity.ok(ProposalMapper.ProposalToDTO(updatedProposal));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ProposalDTO> deleteProposal(@PathVariable("id") Long id) {
        Proposal deletedProposal = proposalService.deleteById(id);
        return ResponseEntity.ok(ProposalMapper.ProposalToDTO(deletedProposal));
    }
}

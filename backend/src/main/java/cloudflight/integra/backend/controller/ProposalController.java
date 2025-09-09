package cloudflight.integra.backend.controller;

import cloudflight.integra.backend.DTO.ProposalDTO;
import cloudflight.integra.backend.mapper.ProposalMapper;
import cloudflight.integra.backend.model.Proposal;
import cloudflight.integra.backend.service.ServiceProposal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/proposal")
public class ProposalController {
    private ServiceProposal serviceProposal;

    @Autowired
    private void setProposalService(ServiceProposal serviceProposal) {
        this.serviceProposal = serviceProposal;
    }

    @PostMapping
    public ResponseEntity<ProposalDTO> createProposal(@RequestBody ProposalDTO proposalDTO) {
        Proposal savedProposal = serviceProposal.save(ProposalMapper.ProposalToEntity(proposalDTO));
        return ResponseEntity.ok(ProposalMapper.ProposalToDTO(savedProposal));
    }

    @GetMapping
    public ResponseEntity<List<ProposalDTO>> getAllProposals() {
        List<ProposalDTO> proposals = serviceProposal.findAll()
                .stream()
                .map(ProposalMapper::ProposalToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(proposals);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProposalDTO> getProposalById(@PathVariable Long id) {
        Proposal proposal = serviceProposal.findById(id)
                .orElseThrow(() -> new RuntimeException("Proposal not found: " + id));

        return ResponseEntity.ok(ProposalMapper.ProposalToDTO(proposal));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProposalDTO> updateProposal(@PathVariable Long id, @RequestBody ProposalDTO newProposalDTO) {
        Proposal updatedProposal = serviceProposal.update(ProposalMapper.ProposalToEntity(newProposalDTO));
        return ResponseEntity.ok(ProposalMapper.ProposalToDTO(updatedProposal));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ProposalDTO> deleteProposal(@PathVariable Long id) {
        Proposal deletedProposal = serviceProposal.deleteById(id);
        return ResponseEntity.ok(ProposalMapper.ProposalToDTO(deletedProposal));
    }
}

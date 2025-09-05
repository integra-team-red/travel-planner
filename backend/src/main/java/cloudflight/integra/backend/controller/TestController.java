package cloudflight.integra.backend.controller;

import cloudflight.integra.backend.model.Proposal;
import cloudflight.integra.backend.service.ServiceProposal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
public class TestController {
    private ServiceProposal serviceProposal;

    @Autowired
    private void setProposalService(ServiceProposal serviceProposal) {
        this.serviceProposal = serviceProposal;
    }
    @PostMapping
    public Proposal createProposal(@RequestBody Proposal proposal) {
        return serviceProposal.save(proposal);
    }
    @GetMapping
    public List<Proposal> getAllProposals() {
        return serviceProposal.findAll();
    }
    @GetMapping("/{id}")
    public Proposal getProposalById(@PathVariable Long id) {
        return serviceProposal.findById(id)
                .orElseThrow(() -> new RuntimeException("Proposal not found: " + id));
    }
    @PutMapping("/{id}")
    public Proposal updateProposal(@PathVariable Long id, @RequestBody Proposal proposal) {
        proposal.setId(id);
        return serviceProposal.update(proposal);
    }
    @DeleteMapping("/{id}")
    public void deleteProposal(@PathVariable Long id) {
        serviceProposal.deleteById(id);
    }
}

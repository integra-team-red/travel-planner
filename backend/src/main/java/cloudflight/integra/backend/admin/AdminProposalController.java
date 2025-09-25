package cloudflight.integra.backend.admin;

import cloudflight.integra.backend.proposal.*;
import cloudflight.integra.backend.user.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/proposals")
public class AdminProposalController {
    private AdminProposalService adminProposalService;
    private DBUserRepository userRepository;

    @Autowired
    public AdminProposalController(AdminProposalService adminProposalService, DBUserRepository userRepository) {
        this.adminProposalService = adminProposalService;
        this.userRepository = userRepository;
    }

    @PostMapping("/{id}/approve")
    public ResponseEntity<ProposalDTO> approveProposal(@PathVariable Long id, Authentication authentication) {
        User admin = userRepository.findUserByEmail(authentication.getName());
        Proposal approved = adminProposalService.approveProposal(id, admin.getId());
        return ResponseEntity.ok(ProposalMapper.ProposalToDTO(approved));
    }

    @PostMapping("/{id}/reject")
    public ResponseEntity<ProposalDTO> rejectProposal(@PathVariable Long id, Authentication authentication) {
        User admin = userRepository.findUserByEmail(authentication.getName());
        Proposal rejected = adminProposalService.rejectProposal(id, admin.getId());
        return ResponseEntity.ok(ProposalMapper.ProposalToDTO(rejected));
    }
}

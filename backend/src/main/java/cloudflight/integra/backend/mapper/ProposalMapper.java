package cloudflight.integra.backend.mapper;

import cloudflight.integra.backend.DTO.ProposalDTO;
import cloudflight.integra.backend.model.Proposal;

public class ProposalMapper {
    public static ProposalDTO ProposalToDTO(Proposal proposal) {
        return new ProposalDTO(proposal.getId(),
                proposal.getName(),
                proposal.getType(),
                proposal.getStatus());
    }

    public static Proposal ProposalToEntity(ProposalDTO proposal_dto) {
        Proposal proposal = new Proposal();
        proposal.setId(proposal_dto.id());
        proposal.setName(proposal_dto.name());
        proposal.setType(proposal_dto.type());
        proposal.setStatus(proposal_dto.status());
        return proposal;
    }

}

package cloudflight.integra.backend.DTO;

import cloudflight.integra.backend.model.Status;
import cloudflight.integra.backend.model.Type;

public record ProposalDTO(Long id, String name, Type type, Status status) {
}

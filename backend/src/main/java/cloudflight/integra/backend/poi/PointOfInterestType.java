package cloudflight.integra.backend.poi;

import cloudflight.integra.backend.proposal.ProposalType;

public enum PointOfInterestType {
    MUSEUM(ProposalType.POINT_OF_INTEREST),
    LANDMARK(ProposalType.POINT_OF_INTEREST),
    PARK(ProposalType.POINT_OF_INTEREST),
    MONUMENT(ProposalType.POINT_OF_INTEREST),
    OTHER(ProposalType.POINT_OF_INTEREST);

    private final ProposalType parent;

    PointOfInterestType(ProposalType parent) {
        this.parent = parent;
    }

    public ProposalType getParent() {
        return parent;
    }
}

package cloudflight.integra.backend.proposal;

import cloudflight.integra.backend.poi.PointOfInterestType;

public record ProposalDTO(
        Long id,
        String name,
        ProposalType type,
        Status status,
        Long cityId,
        String description,
        Double price,
        Double averagePrice,
        String cuisineType,
        PointOfInterestType poiType) {}

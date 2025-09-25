package cloudflight.integra.backend.proposal;

import cloudflight.integra.backend.city.City;

public class ProposalMapper {
    public static ProposalDTO ProposalToDTO(Proposal proposal) {
        return new ProposalDTO(
                proposal.getId(),
                proposal.getName(),
                proposal.getType(),
                proposal.getStatus(),
                proposal.getCity() != null ? proposal.getCity().getId() : null,
                proposal.getDescription(),
                proposal.getPrice(),
                proposal.getAveragePrice(),
                proposal.getCuisineType(),
                proposal.getPoiType());
    }

    public static Proposal ProposalToEntity(ProposalDTO dto) {
        Proposal proposal = new Proposal();
        proposal.setId(dto.id());
        proposal.setName(dto.name());
        proposal.setType(dto.type());
        proposal.setStatus(dto.status());
        proposal.setDescription(dto.description());
        proposal.setPrice(dto.price());
        proposal.setAveragePrice(dto.averagePrice());
        proposal.setCuisineType(dto.cuisineType());
        proposal.setPoiType(dto.poiType());

        if (dto.cityId() != null) {
            City city = new City();
            city.setId(dto.cityId());
            proposal.setCity(city);
        }

        return proposal;
    }
}

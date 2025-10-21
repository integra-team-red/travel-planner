package cloudflight.integra.backend.coffeeShop;

import cloudflight.integra.backend.city.City;
import java.util.List;

public class CoffeeShopMapper {
    public static CoffeeShopDTO EntityToDTO(CoffeeShop coffeeShop) {
        return new CoffeeShopDTO(
                coffeeShop.getId(),
                coffeeShop.getName(),
                coffeeShop.getCityId(),
                coffeeShop.getAddress(),
                coffeeShop.getOpeningHours(),
                coffeeShop.getDescription(),
                coffeeShop.getAveragePrice(),
                coffeeShop.getRating(),
                coffeeShop.getImage());
    }

    public static CoffeeShop DTOtoEntity(CoffeeShopDTO coffee_dto, City city) {
        CoffeeShop coffeeShop = new CoffeeShop();
        coffeeShop.setId(null);
        coffeeShop.setName(coffee_dto.name());
        coffeeShop.setCity(city);
        coffeeShop.setAddress(coffee_dto.address());
        coffeeShop.setOpeningHours(coffee_dto.openingHours());
        coffeeShop.setDescription(coffee_dto.description());
        coffeeShop.setAveragePrice(coffee_dto.averagePrice());
        coffeeShop.setRating(coffee_dto.rating());
        coffeeShop.setImage(coffee_dto.image());

        return coffeeShop;
    }

    public static List<CoffeeShopDTO> EntityListToDTOList(List<CoffeeShop> entityList) {
        return entityList.stream().map(CoffeeShopMapper::EntityToDTO).toList();
    }
}

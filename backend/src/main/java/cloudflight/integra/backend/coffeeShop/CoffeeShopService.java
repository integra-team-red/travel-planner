package cloudflight.integra.backend.coffeeShop;

import java.util.List;

public interface CoffeeShopService {
    CoffeeShop addCoffeeShop(CoffeeShop coffeeShop);

    List<CoffeeShop> addCoffeeShops(List<CoffeeShop> coffeeShops);

    CoffeeShop updateCoffeeShop(Long id, CoffeeShop coffeeShop);

    void deleteCoffeeShop(Long id);

    List<CoffeeShop> getAllCoffeeShops();

    CoffeeShop getCoffeeShop(Long id);

    List<CoffeeShop> getAllCoffeeShopsSortedByName(int pageNumber, int pageSize, boolean isDescending);

    List<CoffeeShop> getAllCoffeeShopsSortedByAveragePrice(int pageNumber, int pageSize, boolean isDescending);

    List<CoffeeShop> getAllCoffeeShopsSortedByRating(int pageNumber, int pageSize, boolean isDescending);
}

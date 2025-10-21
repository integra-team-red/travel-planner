package cloudflight.integra.backend.coffeeShop;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CoffeeShopRepository extends JpaRepository<CoffeeShop, Long> {
    List<CoffeeShop> findByCity_Id(Long cityId);

    List<CoffeeShop> findByCity_Name(String cityName);
}

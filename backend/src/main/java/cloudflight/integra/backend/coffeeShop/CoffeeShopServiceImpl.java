package cloudflight.integra.backend.coffeeShop;

import cloudflight.integra.backend.validation.GenericConstraintValidator;
import cloudflight.integra.backend.validation.PageRequestValidator;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class CoffeeShopServiceImpl implements CoffeeShopService {

    private final CoffeeShopRepository repository;
    private final GenericConstraintValidator<CoffeeShop> validator;

    @Autowired
    public CoffeeShopServiceImpl(CoffeeShopRepository coffeeRepo, GenericConstraintValidator<CoffeeShop> validator) {
        this.repository = coffeeRepo;
        this.validator = validator;
    }

    @Override
    public CoffeeShop addCoffeeShop(CoffeeShop coffeeShop) {
        validator.validate(coffeeShop);
        return repository.save(coffeeShop);
    }

    @Override
    public List<CoffeeShop> addCoffeeShops(List<CoffeeShop> coffeeShops) {
        try {
            coffeeShops.forEach(validator::validate);
        } catch (ConstraintViolationException err) {
            throw new ConstraintViolationException(
                    "One or more CoffeeShops were invalid:\n" + err.getMessage(), err.getConstraintViolations());
        }
        return repository.saveAll(coffeeShops);
    }

    @Override
    public CoffeeShop updateCoffeeShop(Long id, CoffeeShop newCoffeeShop) {
        validator.validate(newCoffeeShop);
        var coffeeShop = getCoffeeShop(id);

        coffeeShop.setName(newCoffeeShop.getName());
        coffeeShop.setCity(newCoffeeShop.getCity());
        coffeeShop.setAddress(newCoffeeShop.getAddress());
        coffeeShop.setOpeningHours(newCoffeeShop.getOpeningHours());
        coffeeShop.setDescription(newCoffeeShop.getDescription());
        coffeeShop.setAveragePrice(newCoffeeShop.getAveragePrice());
        coffeeShop.setRating(newCoffeeShop.getRating());
        coffeeShop.setImage(newCoffeeShop.getImage());

        repository.save(coffeeShop);
        return coffeeShop;
    }

    @Override
    public void deleteCoffeeShop(Long id) {
        repository.deleteById(id);
    }

    @Override
    public List<CoffeeShop> getAllCoffeeShops() {
        return repository.findAll();
    }

    @Override
    public CoffeeShop getCoffeeShop(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("No coffee shop id provided");
        }
        var coffeeShopWrapper = repository.findById(id);
        if (coffeeShopWrapper.isEmpty()) {
            throw new EntityNotFoundException("Coffee shop with the provided id not found");
        }
        return coffeeShopWrapper.get();
    }

    @Override
    public List<CoffeeShop> getAllCoffeeShopsSortedByName(int pageNumber, int pageSize, boolean isDescending) {
        PageRequestValidator.validate(pageNumber, pageSize);
        var sortingDirection = isDescending ? Sort.Direction.DESC : Sort.Direction.ASC;
        return repository
                .findAll(PageRequest.of(pageNumber, pageSize, Sort.by(sortingDirection, "name")))
                .toList();
    }

    @Override
    public List<CoffeeShop> getAllCoffeeShopsSortedByAveragePrice(int pageNumber, int pageSize, boolean isDescending) {
        PageRequestValidator.validate(pageNumber, pageSize);
        var sortingDirection = isDescending ? Sort.Direction.DESC : Sort.Direction.ASC;
        return repository
                .findAll(PageRequest.of(pageNumber, pageSize, Sort.by(sortingDirection, "average_price")))
                .toList();
    }

    @Override
    public List<CoffeeShop> getAllCoffeeShopsSortedByRating(int pageNumber, int pageSize, boolean isDescending) {
        PageRequestValidator.validate(pageNumber, pageSize);
        var sortingDirection = isDescending ? Sort.Direction.DESC : Sort.Direction.ASC;
        return repository
                .findAll(PageRequest.of(pageNumber, pageSize, Sort.by(sortingDirection, "rating")))
                .toList();
    }
}

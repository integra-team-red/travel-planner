package cloudflight.integra.backend.spa;

import cloudflight.integra.backend.validation.GenericValidator;
import cloudflight.integra.backend.validation.ValidationError;
import org.springframework.stereotype.Component;

@Component
public class SpaValidator implements GenericValidator<Spa> {
    public void validate(Spa spa) throws ValidationError {
        if (spa.getPriceLowerBound() > spa.getPriceUpperBound()) {
            throw new ValidationError("Spa's price lower bound cannot exceed its upper bound");
        }
    }
}

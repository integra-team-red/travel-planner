package cloudflight.integra.backend.nightlife;

import cloudflight.integra.backend.validation.GenericValidator;
import cloudflight.integra.backend.validation.ValidationError;
import org.springframework.stereotype.Component;

@Component
public class NightlifeValidator implements GenericValidator<Nightlife> {
    public void validate(Nightlife nightlife) throws ValidationError {
        if (nightlife.getPriceLowerBound() > nightlife.getPriceUpperBound()) {
            throw new ValidationError("Nightlife's price lower bound cannot exceed its upper bound");
        }
    }
}

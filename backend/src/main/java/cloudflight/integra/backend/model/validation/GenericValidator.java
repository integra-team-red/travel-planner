package cloudflight.integra.backend.model.validation;

public interface GenericValidator<T> {
    void validate(T object) throws ValidationError;
}

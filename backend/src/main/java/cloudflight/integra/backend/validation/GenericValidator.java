package cloudflight.integra.backend.validation;

public interface GenericValidator<T> {
    void validate(T object) throws ValidationError;
}

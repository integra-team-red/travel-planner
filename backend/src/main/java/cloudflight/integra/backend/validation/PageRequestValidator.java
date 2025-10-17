package cloudflight.integra.backend.validation;

public class PageRequestValidator {
    public static void validate(int pageNumber, int pageSize) throws IllegalArgumentException {
        if (pageSize <= 0 || pageNumber < 0) {
            throw new IllegalArgumentException("Invalid page request");
        }
    }
}

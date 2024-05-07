package simonemanca.vetrineCapstone.exceptions;

import org.springframework.validation.ObjectError;
import java.util.List;

public class BadRequestException extends RuntimeException {
    private List<ObjectError> errorsList;

    public BadRequestException(String message) {
        super(message);
    }

    public BadRequestException(String message, List<ObjectError> errorsList) {
        super(message);
        this.errorsList = errorsList;
    }

    public List<ObjectError> getErrorsList() {
        return errorsList;
    }
}


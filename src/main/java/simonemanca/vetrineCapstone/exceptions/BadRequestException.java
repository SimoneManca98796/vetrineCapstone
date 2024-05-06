package simonemanca.vetrineCapstone.exceptions;

import java.util.List;
import org.springframework.validation.ObjectError;

public class BadRequestException extends RuntimeException {
    private List<ObjectError> errorsList;

    public BadRequestException(String message) {
        super(message);
    }

    public BadRequestException(List<ObjectError> errorsList) {
        super("Ci sono stati errori di validazione nel payload.");
        this.errorsList = errorsList;
    }

    public List<ObjectError> getErrorsList() {
        return errorsList;
    }
}

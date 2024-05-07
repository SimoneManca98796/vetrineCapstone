package simonemanca.vetrineCapstone.payloads;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

/**
 * Data Transfer Object for new user registration. Uses Java Record for immutability and simplicity.
 */
public record NewUserDTO(
        @NotEmpty(message = "Il nome è obbligatorio")
        @Size(min = 3, max = 30, message = "Il nome deve essere compreso tra i 3 e i 30 caratteri")
        String name,

        @NotEmpty(message = "Il cognome è obbligatorio")
        @Size(min = 3, max = 30, message = "Il cognome deve essere compreso tra i 3 e i 30 caratteri")
        String surname,

        @NotEmpty(message = "L'email è obbligatoria")
        @Email(message = "L'email inserita non è valida")
        String email,

        @NotEmpty(message = "La password è obbligatoria")
        @Size(min = 8, message = "La password deve avere come minimo 8 caratteri")
        String password
) {}

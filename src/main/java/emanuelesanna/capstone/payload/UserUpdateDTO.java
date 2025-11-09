package emanuelesanna.capstone.payload;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserUpdateDTO(
        @NotBlank(message = "Il nome è obbligatorio.")
        @Size(min = 2, max = 50, message = "Il nome deve avere tra 2 e 50 caratteri.")
        String name,
        @NotBlank(message = "Il cognome è obbligatorio.")
        @Size(min = 2, max = 50, message = "Il cognome deve avere tra 2 e 50 caratteri.")
        String surname,
        @NotBlank(message = "L'email è obbligatoria.")
        @Email(message = "Il formato dell'email non è valido.")
        @Size(max = 100, message = "L'email non può superare i 100 caratteri.")
        String email) {
}

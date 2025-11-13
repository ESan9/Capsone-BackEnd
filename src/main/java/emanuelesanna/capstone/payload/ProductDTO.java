package emanuelesanna.capstone.payload;

import emanuelesanna.capstone.enums.Availability;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductDTO(
        @NotBlank(message = "Il nome è obbligatorio.")
        @Size(min = 2, max = 50, message = "Il nome deve avere tra 2 e 50 caratteri.")
        String name,
        @NotBlank(message = "La descrizione è obbligatoria")
        @Size(min = 10, max = 255, message = "La descrizione deve avere tra 10 e 255 caratteri.")
        String description,
        @NotNull(message = "Il prezzo è obbligatorio")
        @Positive(message = "Il prezzo deve essere positivo")
        BigDecimal price,
        @NotNull(message = "La disponibilità è obbligatoria")
        Availability availability,
        boolean highlighted,
        @NotBlank
        @Size(max = 255)
        String materials,
        @NotBlank
        @Size(max = 255)
        String dimension,
        @NotNull(message = "L'ID della categoria è obbligatorio")
        UUID categoryId) {
}

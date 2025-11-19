package emanuelesanna.capstone.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record NewCategoryDTO(
        @NotBlank(message = "Il nome è obbligatorio")
        @Size(min = 2, max = 50, message = "Il nome deve avere tra 2 e 50 caratteri.")
        String name,
        @NotBlank(message = "La descrizione è obbligatoria")
        @Size(min = 10, max = 255, message = "La descrizione deve avere tra 10 e 255 caratteri.")
        String description,
        String coverImageUrl
) {
}

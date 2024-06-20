package example.task.model.command;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "Command object used to create a new product.")
public class CreateProductCommand {

    @Schema(description = "Name of product")
    @NotBlank(message = "Name may not be blank")
    private String name;
    @Schema(description = "Price of product")
    @NotNull(message = "Price may not be null")
    @PositiveOrZero(message = "Price may not be negative")
    private Double price;
}

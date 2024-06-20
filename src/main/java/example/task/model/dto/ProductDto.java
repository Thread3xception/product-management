package example.task.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Schema(description = "Data Transfer Object representing a product.")
public class ProductDto {

    @Schema(description = "Name of product")
    private String name;
    @Schema(description = "Price of product")
    private Double price;
}

package br.edu.unifip.ecommerceapi.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

//@Data
//public class ProductDto {
//    @NotBlank
//    private String name;
//    @NotBlank
//    private String description;
//    @NotNull
//    private BigDecimal price;
//    @NotNull
//    private UUID category;
//
//    public UUID getCategory() {
//        return category;
//    }
//}

// Os records já geram automaticamente alguns métodos (como equals(), hashCode(), toString()) por isso não precisamos
// do @Data do Lombok
public record ProductDto(
        String name,
        String description,
        BigDecimal price,
        UUID category
) {
}
// os métodos "getter" para os campos são gerados automaticamente, e você não precisa escrevê-los explicitamente.
// Para acessar os campos de um record, você pode usar diretamente o nome do campo, sem a necessidade de criar métodos
// "getter". O compilador do Java gera esses métodos automaticamente.

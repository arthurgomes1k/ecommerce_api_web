package br.edu.unifip.ecommerceapi.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;


public record EnderecoDto(
        @NotNull
        UUID id,
        @NotBlank
        String rua,
        @NotBlank
        String cidade) {
}
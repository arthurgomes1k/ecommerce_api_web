package br.edu.unifip.ecommerceapi.dtos;

import br.edu.unifip.ecommerceapi.constants.UserRole;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RoleDto {
    @NotNull
    @Enumerated(EnumType.STRING)
    private UserRole name;

}

package com.mariajulia.auth_service.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank
        @Size(min = 2, max = 100)
        String name,

        @NotBlank
        @Email(message = "O email deve ser válido.")
        String email,

        @NotBlank
        @Size(min = 8, max = 255, message = "A senha deve conter pelo menos 8 caracteres.")
        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&]).{8,}$",
                message = "A senha deve conter pelo menos uma letra maiúscula, uma letra minúscula, um dígito e um caractere especial (@$!%*?&)."
        )
        String password
) {
}

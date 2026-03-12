package com.logitrack.logitrack.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import com.logitrack.logitrack.model.Usuario;

public record UsuarioRequestDTO(
        @NotBlank(message = "El nombre no puede estar vacío")
        String nombre,

        @NotBlank(message = "El email no puede estar vacío")
        @Email(message = "El email no es válido")
        String email,

        @NotBlank(message = "La contraseña no puede estar vacía")
        String password,

        @NotNull(message = "El rol es obligatorio")
        Usuario.Rol rol
) {
}

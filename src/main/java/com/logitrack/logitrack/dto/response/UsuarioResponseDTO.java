package com.logitrack.logitrack.dto.response;

import com.logitrack.logitrack.model.Usuario;

public record UsuarioResponseDTO(
        Long id,
        String nombre,
        String email,
        Usuario.Rol rol
) {}
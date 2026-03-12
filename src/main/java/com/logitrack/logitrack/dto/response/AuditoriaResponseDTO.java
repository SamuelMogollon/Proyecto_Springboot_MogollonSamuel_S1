package com.logitrack.logitrack.dto.response;

import com.logitrack.logitrack.model.Auditoria;
import java.time.LocalDateTime;

public record AuditoriaResponseDTO(
        Long id,
        Auditoria.TipoOperacion tipoOperacion,
        LocalDateTime fechaHora,
        String entidadAfectada,
        String valoresAnteriores,
        String valoresNuevos,
        UsuarioResponseDTO usuario
) {}

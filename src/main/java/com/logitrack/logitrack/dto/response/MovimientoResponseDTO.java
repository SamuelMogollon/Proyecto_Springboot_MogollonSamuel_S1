package com.logitrack.logitrack.dto.response;

import com.logitrack.logitrack.model.Movimiento;
import java.time.LocalDateTime;
import java.util.List;

public record MovimientoResponseDTO(
        Long id,
        LocalDateTime fecha,
        Movimiento.TipoMovimiento tipo,
        UsuarioResponseDTO usuario,
        BodegaResponseDTO bodegaOrigen,
        BodegaResponseDTO bodegaDestino,
        List<DetalleMovimientoResponseDTO> detalles
) {}

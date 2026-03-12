package com.logitrack.logitrack.dto.request;

import com.logitrack.logitrack.model.Movimiento;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record MovimientoRequestDTO(

        @NotNull(message = "El tipo de movimiento es obligatorio")
        Movimiento.TipoMovimiento tipo,

        @NotNull(message = "El usuario es obligatorio")
        Long idUsuario,

        Long idBodegaOrigen,

        Long idBodegaDestino,

        @NotNull(message = "Los detalles son obligatorios")
        List<DetalleMovimientoRequestDTO> detalles
) {}

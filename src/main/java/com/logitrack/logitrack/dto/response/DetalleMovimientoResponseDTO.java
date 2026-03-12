package com.logitrack.logitrack.dto.response;

public record DetalleMovimientoResponseDTO(
        Long id,
        ProductoResponseDTO producto,
        Integer cantidad
) {}

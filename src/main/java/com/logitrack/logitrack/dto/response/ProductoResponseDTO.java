package com.logitrack.logitrack.dto.response;

import java.math.BigDecimal;

public record ProductoResponseDTO(
        Long id,
        String nombre,
        String categoria,
        BigDecimal precio
) {}

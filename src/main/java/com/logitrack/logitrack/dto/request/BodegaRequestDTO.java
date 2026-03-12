package com.logitrack.logitrack.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record BodegaRequestDTO(

        @NotBlank(message = "El nombre no puede estar vacío")
        String nombre,

        @NotBlank(message = "La ubicación no puede estar vacía")
        String ubicacion,

        @NotNull(message = "La capacidad es obligatoria")
        @Min(value = 1, message = "La capacidad debe ser mayor que 0")
        Integer capacidad,

        @NotNull(message = "El id del encargado es obligatorio")
        Long idEncargado
) {}
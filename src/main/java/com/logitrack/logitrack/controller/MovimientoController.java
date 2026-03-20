package com.logitrack.logitrack.controller;


import com.logitrack.logitrack.dto.request.MovimientoRequestDTO;
import com.logitrack.logitrack.dto.response.MovimientoResponseDTO;
import com.logitrack.logitrack.service.impl.MovimientoServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Tag(name = "Movimientos", description = "Gestión de movimientos de inventario (entradas, salidas y transferencias)")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/movimientos")
@RequiredArgsConstructor
public class MovimientoController {

    private final MovimientoServiceImpl movimientoServiceImpl;

    @Operation(summary = "Crear movimiento", description = "Registra un nuevo movimiento de inventario. Tipos: ENTRADA, SALIDA, TRANSFERENCIA")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Movimiento creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o stock insuficiente",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"error\": \"Stock insuficiente\"}"))),
            @ApiResponse(responseCode = "403", description = "No autorizado")
    })
    @PostMapping
    public ResponseEntity<MovimientoResponseDTO> guardar(@Valid @RequestBody MovimientoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(movimientoServiceImpl.guardarMovimiento(dto));
    }

    @Operation(summary = "Listar movimientos", description = "Retorna todos los movimientos registrados en el sistema")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de movimientos obtenida exitosamente"),
            @ApiResponse(responseCode = "403", description = "No autorizado")
    })
    @GetMapping
    public ResponseEntity<List<MovimientoResponseDTO>> listarTodos() {
        return ResponseEntity.ok(movimientoServiceImpl.listarTodos());
    }

    @Operation(summary = "Buscar movimiento por ID", description = "Retorna un movimiento específico por su ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Movimiento encontrado"),
            @ApiResponse(responseCode = "404", description = "Movimiento no encontrado",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"error\": \"Movimiento no encontrado\"}"))),
            @ApiResponse(responseCode = "403", description = "No autorizado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<MovimientoResponseDTO> buscarPorId(@Parameter(description = "ID del movimiento", example = "1")@PathVariable Long id) {
        return ResponseEntity.ok(movimientoServiceImpl.buscarPorId(id));
    }

    @Operation(summary = "Buscar movimientos por rango de fechas", description = "Retorna movimientos entre dos fechas. Formato: 2024-01-01T00:00:00")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de movimientos obtenida exitosamente"),
            @ApiResponse(responseCode = "400", description = "Formato de fecha inválido"),
            @ApiResponse(responseCode = "403", description = "No autorizado")
    })


    @GetMapping("/rango")
    public ResponseEntity<List<MovimientoResponseDTO>> buscarPorRango(
            @Parameter(description = "Fecha inicio. Ejemplo: 2024-01-01T00:00:00")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @Parameter(description = "Fecha fin. Ejemplo: 2024-12-31T23:59:59")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {
        return ResponseEntity.ok(movimientoServiceImpl.buscarPorRangoDeFechas(inicio, fin));
    }

    @GetMapping("/movimientos/recientes")
    public ResponseEntity<List<MovimientoResponseDTO>> listarRecientes(){
        return ResponseEntity.ok(movimientoServiceImpl.listarUltimos());
    }




}

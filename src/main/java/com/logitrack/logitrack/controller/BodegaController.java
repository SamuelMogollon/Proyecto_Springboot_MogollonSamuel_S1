package com.logitrack.logitrack.controller;


import com.logitrack.logitrack.dto.request.BodegaRequestDTO;
import com.logitrack.logitrack.dto.response.BodegaResponseDTO;
import com.logitrack.logitrack.service.impl.BodegaServiceImpl;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Bodegas", description = "Gestión de bodegas del sistema")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/bodegas")
@RequiredArgsConstructor
public class BodegaController {

    private final BodegaServiceImpl bodegaServiceImpl;

    @Operation(summary = "Crear bodega", description = "Registra una nueva bodega en el sistema")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Bodega creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"error\": \"Datos inválidos\"}"))),
            @ApiResponse(responseCode = "403", description = "No autorizado")
    })
    @PostMapping
    public ResponseEntity<BodegaResponseDTO> guardar(@Valid @RequestBody BodegaRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bodegaServiceImpl.guardarBodega(dto));
    }

    @Operation(summary = "Listar bodegas", description = "Retorna todas las bodegas registradas en el sistema")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de bodegas obtenida exitosamente"),
            @ApiResponse(responseCode = "403", description = "No autorizado")
    })
    @GetMapping
    public ResponseEntity<List<BodegaResponseDTO>> listarTodos() {
        return ResponseEntity.ok(bodegaServiceImpl.listarTodos());
    }

    @Operation(summary = "Buscar bodega por ID", description = "Retorna una bodega específica por su ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Bodega encontrada"),
            @ApiResponse(responseCode = "404", description = "Bodega no encontrada",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"error\": \"Bodega no encontrada\"}"))),
            @ApiResponse(responseCode = "403", description = "No autorizado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<BodegaResponseDTO> buscarPorId(@Parameter(description = "ID de la bodega", example = "1")@PathVariable Long id) {
        return ResponseEntity.ok(bodegaServiceImpl.buscarPorId(id));
    }

    @Operation(summary = "Actualizar bodega", description = "Actualiza los datos de una bodega existente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Bodega actualizada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Bodega no encontrada",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"error\": \"Bodega no encontrada\"}"))),
            @ApiResponse(responseCode = "403", description = "No autorizado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<BodegaResponseDTO> actualizar(@Valid @RequestBody BodegaRequestDTO dto,
                                                        @Parameter(description = "ID de la bodega", example = "1")
                                                        @PathVariable Long id) {
        return ResponseEntity.ok(bodegaServiceImpl.actualizarBodega(dto, id));
    }

    @Operation(summary = "Eliminar bodega", description = "Elimina una bodega del sistema por su ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Bodega eliminada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Bodega no encontrada",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"error\": \"Bodega no encontrada\"}"))),
            @ApiResponse(responseCode = "403", description = "No autorizado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@Parameter(description = "ID de la bodega", example = "1")@PathVariable Long id) {
        bodegaServiceImpl.eliminarBodega(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

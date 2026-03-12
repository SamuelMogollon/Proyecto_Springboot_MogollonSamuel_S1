package com.logitrack.logitrack.controller;


import com.logitrack.logitrack.dto.response.AuditoriaResponseDTO;
import com.logitrack.logitrack.model.Auditoria;
import com.logitrack.logitrack.service.impl.AuditoriaServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Auditoría", description = "Consulta del registro de auditoría del sistema")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/auditorias")
@RequiredArgsConstructor
public class AuditoriaController {

    private final AuditoriaServiceImpl auditoriaServiceImpl;

    @Operation(summary = "Listar auditorías", description = "Retorna todos los registros de auditoría del sistema")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de auditorías obtenida exitosamente"),
            @ApiResponse(responseCode = "403", description = "No autorizado")
    })    @GetMapping
    public ResponseEntity<List<AuditoriaResponseDTO>> listarTodos() {
        return ResponseEntity.ok(auditoriaServiceImpl.listarTodos());
    }

    @Operation(summary = "Buscar auditorías por usuario", description = "Retorna todos los registros de auditoría generados por un usuario específico")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de auditorías obtenida exitosamente"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"error\": \"Usuario no encontrado\"}"))),
            @ApiResponse(responseCode = "403", description = "No autorizado")
    })
    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<AuditoriaResponseDTO>> buscarPorUsuario(@Parameter(description = "ID del usuario", example = "1")@PathVariable Long idUsuario) {
        return ResponseEntity.ok(auditoriaServiceImpl.buscarPorUsuario(idUsuario));
    }

    @Operation(summary = "Buscar auditorías por tipo", description = "Retorna todos los registros de auditoría filtrados por tipo. Valores: INSERT, UPDATE, DELETE")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de auditorías obtenida exitosamente"),
            @ApiResponse(responseCode = "400", description = "Tipo de operación inválido",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"error\": \"Tipo de operación inválido\"}"))),
            @ApiResponse(responseCode = "403", description = "No autorizado")
    })
    @GetMapping("/tipo/{tipoOperacion}")
    public ResponseEntity<List<AuditoriaResponseDTO>> buscarPorTipo(
            @Parameter(description = "Tipo de operación. Valores: INSERT, UPDATE, DELETE", example = "INSERT")
            @PathVariable Auditoria.TipoOperacion tipoOperacion) {
        return ResponseEntity.ok(auditoriaServiceImpl.buscarPorTipoOperacion(tipoOperacion));
    }
}

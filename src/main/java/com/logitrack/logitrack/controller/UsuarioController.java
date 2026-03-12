package com.logitrack.logitrack.controller;


import com.logitrack.logitrack.dto.request.UsuarioRequestDTO;
import com.logitrack.logitrack.dto.response.UsuarioResponseDTO;
import com.logitrack.logitrack.service.impl.UsuarioServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Tag(name = "Usuarios", description = "Gestión de usuarios del sistema")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

        private final UsuarioServiceImpl usuarioServiceImpl;

        @Operation(summary = "Crear usuario", description = "Registra un nuevo usuario en el sistema")
        @ApiResponses({
                @ApiResponse(responseCode = "201", description = "Usuario creado exitosamente"),
                @ApiResponse(responseCode = "400", description = "Datos inválidos",
                        content = @Content(mediaType = "application/json",
                                examples = @ExampleObject(value = "{\"error\": \"El email ya está registrado\"}"))),
                @ApiResponse(responseCode = "403", description = "No autorizado")
        })
        @PostMapping
        public ResponseEntity<UsuarioResponseDTO> guardar(@RequestBody UsuarioRequestDTO dto) {
            return ResponseEntity.status(HttpStatus.CREATED).body(usuarioServiceImpl.guardarUsuario(dto));
        }

        @Operation(summary = "Listar usuarios", description = "Retorna todos los usuarios registrados en el sistema")
        @ApiResponses({
                @ApiResponse(responseCode = "200", description = "Lista de usuarios obtenida exitosamente"),
                @ApiResponse(responseCode = "403", description = "No autorizado")
        })
        @GetMapping
        public ResponseEntity<List<UsuarioResponseDTO>> listarTodos() {
            return ResponseEntity.ok(usuarioServiceImpl.listarTodos());
        }

        @Operation(summary = "Buscar usuario por ID", description = "Retorna un usuario específico por su ID")
        @ApiResponses({
                @ApiResponse(responseCode = "200", description = "Usuario encontrado"),
                @ApiResponse(responseCode = "404", description = "Usuario no encontrado",
                        content = @Content(mediaType = "application/json",
                                examples = @ExampleObject(value = "{\"error\": \"Usuario no encontrado\"}"))),
                @ApiResponse(responseCode = "403", description = "No autorizado")
        })
        @GetMapping("/{id}")
        public ResponseEntity<UsuarioResponseDTO> buscarPorId(@PathVariable Long id) {
            return ResponseEntity.ok(usuarioServiceImpl.buscarPorId(id));
        }

        @Operation(summary = "Actualizar usuario", description = "Actualiza los datos de un usuario existente")
        @ApiResponses({
                @ApiResponse(responseCode = "200", description = "Usuario actualizado exitosamente"),
                @ApiResponse(responseCode = "404", description = "Usuario no encontrado",
                        content = @Content(mediaType = "application/json",
                                examples = @ExampleObject(value = "{\"error\": \"Usuario no encontrado\"}"))),
                @ApiResponse(responseCode = "403", description = "No autorizado")
        })
        @PutMapping("/{id}")
        public ResponseEntity<UsuarioResponseDTO> actualizar(@RequestBody UsuarioRequestDTO dto,
                                                             @PathVariable Long id) {
            return ResponseEntity.ok(usuarioServiceImpl.actualizarUsuario(dto, id));
        }

        @Operation(summary = "Eliminar usuario", description = "Elimina un usuario del sistema por su ID")
        @ApiResponses({
                @ApiResponse(responseCode = "204", description = "Usuario eliminado exitosamente"),
                @ApiResponse(responseCode = "404", description = "Usuario no encontrado",
                        content = @Content(mediaType = "application/json",
                                examples = @ExampleObject(value = "{\"error\": \"Usuario no encontrado\"}"))),
                @ApiResponse(responseCode = "403", description = "No autorizado")
        })
        @DeleteMapping("/{id}")
        public ResponseEntity<Void> eliminar(@PathVariable Long id) {
            usuarioServiceImpl.eliminarUsuario(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
}

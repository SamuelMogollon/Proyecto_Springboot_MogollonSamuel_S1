package com.logitrack.logitrack.auth;


import com.logitrack.logitrack.config.JwtService;
import com.logitrack.logitrack.model.Usuario;
import com.logitrack.logitrack.repository.UsuarioRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Tag(name = "Autenticación", description = "Endpoints para registro e inicio de sesión de usuarios")
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final JwtService jwtService;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Operation(summary = "Iniciar sesión", description = "Autentica al usuario y retorna un token JWT válido por 1 hora")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login exitoso, retorna token JWT",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"token\": \"eyJhbGciOiJIUzI1NiJ9...\"}"))),
            @ApiResponse(responseCode = "401", description = "Credenciales incorrectas",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"error\": \"Credenciales incorrectas\"}")))
    })
    @PostMapping("/login")
    public Map<String, String> login(@RequestBody LoginRequest request) {

        Usuario usuario = usuarioRepository.findByEmail(request.email())
                .orElseThrow(() -> new RuntimeException("Credenciales inválidas"));

        if (!passwordEncoder.matches(request.password(), usuario.getPassword())) {
            throw new RuntimeException("Credenciales inválidas");
        }

        String token = jwtService.generateToken(usuario.getEmail(), usuario.getRol().name());
        return Map.of("token", token);
    }

    @Operation(summary = "Registrar usuario", description = "Crea un nuevo usuario en el sistema con rol ADMIN o EMPLEADO")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuario registrado correctamente",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"mensaje\": \"Usuario registrado correctamente\"}"))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o email ya registrado",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"error\": \"El email ya está registrado\"}")))
    })
    @PostMapping("/register")
    public Map<String, String> register(@RequestBody RegisterRequest request) {

        if (usuarioRepository.existsByEmail(request.email())) {
            throw new RuntimeException("El email ya está registrado");
        }

        Usuario u = new Usuario();
        u.setNombre(request.nombre());
        u.setEmail(request.email());
        u.setPassword(passwordEncoder.encode(request.password()));
        u.setRol(request.rol());

        usuarioRepository.save(u);

        return Map.of("mensaje", "Usuario registrado correctamente");
    }
}

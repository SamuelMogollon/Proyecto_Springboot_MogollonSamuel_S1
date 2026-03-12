package com.logitrack.logitrack.mapper;

import com.logitrack.logitrack.dto.request.UsuarioRequestDTO;
import com.logitrack.logitrack.dto.response.UsuarioResponseDTO;
import com.logitrack.logitrack.model.Usuario;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {
    public static Usuario DTOAEntidad(UsuarioRequestDTO dto) {
        Usuario u = new Usuario();
        u.setNombre(dto.nombre());
        u.setEmail(dto.email());
        u.setPassword(dto.password());
        u.setRol(dto.rol());
        return u;
    }

    public UsuarioResponseDTO entidadADTO(Usuario u) {
        return new UsuarioResponseDTO(
                u.getId(),
                u.getNombre(),
                u.getEmail(),
                u.getRol()
        );
    }

    public void actualizarEntidadDesdeDTO(Usuario u, UsuarioRequestDTO dto) {
        u.setNombre(dto.nombre());
        u.setEmail(dto.email());
        u.setPassword(dto.password());
        u.setRol(dto.rol());
    }
}

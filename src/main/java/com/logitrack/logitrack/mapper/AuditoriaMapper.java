package com.logitrack.logitrack.mapper;

import com.logitrack.logitrack.dto.response.AuditoriaResponseDTO;
import com.logitrack.logitrack.model.Auditoria;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuditoriaMapper {
    private final UsuarioMapper usuarioMapper;

    public AuditoriaResponseDTO entidadADTO(Auditoria a) {
        return new AuditoriaResponseDTO(
                a.getId(),
                a.getTipoOperacion(),
                a.getFechaHora(),
                a.getEntidadAfectada(),
                a.getValoresAnteriores(),
                a.getValoresNuevos(),
                a.getUsuario() != null ? usuarioMapper.entidadADTO(a.getUsuario()) : null
        );
    }
}

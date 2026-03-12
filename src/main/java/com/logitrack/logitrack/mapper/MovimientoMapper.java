package com.logitrack.logitrack.mapper;


import com.logitrack.logitrack.dto.request.MovimientoRequestDTO;
import com.logitrack.logitrack.dto.response.DetalleMovimientoResponseDTO;
import com.logitrack.logitrack.dto.response.MovimientoResponseDTO;
import com.logitrack.logitrack.model.Bodega;
import com.logitrack.logitrack.model.Movimiento;
import com.logitrack.logitrack.model.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MovimientoMapper {
    private final UsuarioMapper usuarioMapper;
    private final BodegaMapper bodegaMapper;
    private final ProductoMapper productoMapper;

    public Movimiento DTOAEntidad(MovimientoRequestDTO dto,
                                  Usuario usuario,
                                  Bodega bodegaOrigen,
                                  Bodega bodegaDestino) {
        Movimiento m = new Movimiento();
        m.setFecha(LocalDateTime.now());
        m.setTipo(dto.tipo());
        m.setUsuario(usuario);
        m.setBodegaOrigen(bodegaOrigen);
        m.setBodegaDestino(bodegaDestino);
        return m;
    }

    public MovimientoResponseDTO entidadADTO(Movimiento m,
                                             List<DetalleMovimientoResponseDTO> detalles) {
        return new MovimientoResponseDTO(
                m.getId(),
                m.getFecha(),
                m.getTipo(),
                usuarioMapper.entidadADTO(m.getUsuario()),
                m.getBodegaOrigen() != null ? bodegaMapper.entidadADTO(m.getBodegaOrigen()) : null,
                m.getBodegaDestino() != null ? bodegaMapper.entidadADTO(m.getBodegaDestino()) : null,
                detalles
        );
    }
}

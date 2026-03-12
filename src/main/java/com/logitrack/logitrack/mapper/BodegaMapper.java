package com.logitrack.logitrack.mapper;

import com.logitrack.logitrack.dto.request.BodegaRequestDTO;
import com.logitrack.logitrack.dto.response.BodegaResponseDTO;
import com.logitrack.logitrack.model.Bodega;
import com.logitrack.logitrack.model.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BodegaMapper {
    private final UsuarioMapper usuarioMapper;

    public Bodega DTOAEntidad(BodegaRequestDTO dto, Usuario encargado) {
        Bodega b = new Bodega();
        b.setNombre(dto.nombre());
        b.setUbicacion(dto.ubicacion());
        b.setCapacidad(dto.capacidad());
        b.setEncargado(encargado);
        return b;
    }

    public BodegaResponseDTO entidadADTO(Bodega b) {
        return new BodegaResponseDTO(
                b.getId(),
                b.getNombre(),
                b.getUbicacion(),
                b.getCapacidad(),
                b.getEncargado() != null ? usuarioMapper.entidadADTO(b.getEncargado()) : null
        );
    }

    public void actualizarEntidadDesdeDTO(Bodega b, BodegaRequestDTO dto, Usuario encargado) {
        b.setNombre(dto.nombre());
        b.setUbicacion(dto.ubicacion());
        b.setCapacidad(dto.capacidad());
        b.setEncargado(encargado);
    }
}

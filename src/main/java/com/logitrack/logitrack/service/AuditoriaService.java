package com.logitrack.logitrack.service;

import com.logitrack.logitrack.dto.response.AuditoriaResponseDTO;
import com.logitrack.logitrack.model.Auditoria;
import java.util.List;

public interface AuditoriaService {
    List<AuditoriaResponseDTO> listarTodos();
    List<AuditoriaResponseDTO> buscarPorUsuario(Long idUsuario);
    List<AuditoriaResponseDTO> buscarPorTipoOperacion(Auditoria.TipoOperacion tipoOperacion);
    void registrar(String entidad, String valoresAnteriores, String valoresNuevos,
                   Auditoria.TipoOperacion tipoOperacion, Long idUsuario);
}

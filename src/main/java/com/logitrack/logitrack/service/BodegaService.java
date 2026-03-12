package com.logitrack.logitrack.service;

import com.logitrack.logitrack.dto.request.BodegaRequestDTO;
import com.logitrack.logitrack.dto.response.BodegaResponseDTO;
import java.util.List;

public interface BodegaService {
    BodegaResponseDTO guardarBodega(BodegaRequestDTO dto);
    BodegaResponseDTO actualizarBodega(BodegaRequestDTO dto, Long id);
    void eliminarBodega(Long id);
    List<BodegaResponseDTO> listarTodos();
    BodegaResponseDTO buscarPorId(Long id);
}

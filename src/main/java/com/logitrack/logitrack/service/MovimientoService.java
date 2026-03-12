package com.logitrack.logitrack.service;

import com.logitrack.logitrack.dto.request.MovimientoRequestDTO;
import com.logitrack.logitrack.dto.response.MovimientoResponseDTO;
import java.time.LocalDateTime;
import java.util.List;

public interface MovimientoService {
    MovimientoResponseDTO guardarMovimiento(MovimientoRequestDTO dto);
    List<MovimientoResponseDTO> listarTodos();
    MovimientoResponseDTO buscarPorId(Long id);
    List<MovimientoResponseDTO> buscarPorRangoDeFechas(LocalDateTime inicio, LocalDateTime fin);
}

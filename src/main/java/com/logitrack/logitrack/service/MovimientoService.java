package com.logitrack.logitrack.service;

import com.logitrack.logitrack.dto.request.MovimientoRequestDTO;
import com.logitrack.logitrack.dto.response.MovimientoResponseDTO;
import com.logitrack.logitrack.model.Movimiento;

import java.time.LocalDateTime;
import java.util.List;

public interface MovimientoService {
    MovimientoResponseDTO guardarMovimiento(MovimientoRequestDTO dto);
    List<MovimientoResponseDTO> listarTodos();
    MovimientoResponseDTO buscarPorId(Long id);
    List<MovimientoResponseDTO> buscarPorRangoDeFechas(LocalDateTime inicio, LocalDateTime fin);
    List<MovimientoResponseDTO> listarUltimos();
    long countByMovimientoId();
    long countByMovimientoTipoMovimiento();
}

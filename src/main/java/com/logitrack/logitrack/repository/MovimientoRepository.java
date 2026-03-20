package com.logitrack.logitrack.repository;

import com.logitrack.logitrack.model.Movimiento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.awt.print.Pageable;
import java.time.LocalDateTime;
import java.util.List;

public interface MovimientoRepository extends JpaRepository<Movimiento, Long> {
    List<Movimiento> findByFechaBetween(LocalDateTime inicio, LocalDateTime fin);
    List<Movimiento> findTop10ByOrderByFechaDesc();
    List<Movimiento>findByMovimientoName(String Movimiento, Pageable pageRequest);
    long countByMovimientoId();
    long countByMovimientoTipoMovimiento();
}
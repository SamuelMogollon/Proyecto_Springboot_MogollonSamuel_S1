package com.logitrack.logitrack.repository;

import com.logitrack.logitrack.model.DetalleMovimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface DetalleMovimientoRepository extends JpaRepository<DetalleMovimiento, Long> {

    @Query("SELECT dm.producto.nombre, SUM(dm.cantidad) as total FROM DetalleMovimiento dm GROUP BY dm.producto ORDER BY total DESC")
    List<Object[]> productosMasMovidos();
}
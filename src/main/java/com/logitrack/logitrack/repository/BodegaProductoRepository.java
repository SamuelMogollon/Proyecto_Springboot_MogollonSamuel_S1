package com.logitrack.logitrack.repository;

import com.logitrack.logitrack.model.BodegaProducto;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface BodegaProductoRepository extends JpaRepository<BodegaProducto, Long> {
    Optional<BodegaProducto> findByBodegaIdAndProductoId(Long idBodega, Long idProducto);
}

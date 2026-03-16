package com.logitrack.logitrack.repository;

import com.logitrack.logitrack.model.BodegaProducto;
import com.logitrack.logitrack.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface BodegaProductoRepository extends JpaRepository<BodegaProducto, Long> {
    Optional<BodegaProducto> findByBodegaIdAndProductoId(Long idBodega, Long idProducto);

    @Query("SELECT COALESCE(SUM(bp.stock), 0) FROM BodegaProducto bp WHERE bp.producto.id = :productoId")
    Integer sumStockByProductoId(@Param("productoId") Long productoId);

    @Query("SELECT bp.producto FROM BodegaProducto bp GROUP BY bp.producto HAVING SUM(bp.stock) < :limite")
    List<Producto> findProductosConStockBajo(@Param("limite") Integer limite);

    @Query("SELECT bp.bodega.nombre, SUM(bp.stock) FROM BodegaProducto bp GROUP BY bp.bodega")
    List<Object[]> stockTotalPorBodega();
}

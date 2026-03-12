package com.logitrack.logitrack.mapper;


import com.logitrack.logitrack.dto.response.DetalleMovimientoResponseDTO;
import com.logitrack.logitrack.model.DetalleMovimiento;
import com.logitrack.logitrack.model.Movimiento;
import com.logitrack.logitrack.model.Producto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DetalleMovimientoMapper {
    private final ProductoMapper productoMapper;

    public DetalleMovimiento crearEntidad(Movimiento movimiento,
                                          Producto producto,
                                          Integer cantidad) {
        DetalleMovimiento d = new DetalleMovimiento();
        d.setMovimiento(movimiento);
        d.setProducto(producto);
        d.setCantidad(cantidad);
        return d;
    }

    public DetalleMovimientoResponseDTO entidadADTO(DetalleMovimiento d) {
        return new DetalleMovimientoResponseDTO(
                d.getId(),
                productoMapper.entidadADTO(d.getProducto()),
                d.getCantidad()
        );
    }
}

package com.logitrack.logitrack.mapper;

import com.logitrack.logitrack.dto.request.ProductoRequestDTO;
import com.logitrack.logitrack.dto.response.ProductoResponseDTO;
import com.logitrack.logitrack.model.Producto;
import org.springframework.stereotype.Component;

@Component
public class ProductoMapper {
    public static Producto DTOAEntidad(ProductoRequestDTO dto) {
        Producto p = new Producto();
        p.setNombre(dto.nombre());
        p.setCategoria(dto.categoria());
        p.setPrecio(dto.precio());
        return p;
    }

    public ProductoResponseDTO entidadADTO(Producto p) {
        return new ProductoResponseDTO(
                p.getId(),
                p.getNombre(),
                p.getCategoria(),
                p.getPrecio()
        );
    }

    public void actualizarEntidadDesdeDTO(Producto p, ProductoRequestDTO dto) {
        p.setNombre(dto.nombre());
        p.setCategoria(dto.categoria());
        p.setPrecio(dto.precio());
    }
}

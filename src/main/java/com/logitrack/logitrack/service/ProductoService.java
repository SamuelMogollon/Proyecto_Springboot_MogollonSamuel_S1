package com.logitrack.logitrack.service;

import com.logitrack.logitrack.dto.request.ProductoRequestDTO;
import com.logitrack.logitrack.dto.response.ProductoResponseDTO;
import java.util.List;

public interface ProductoService {
    ProductoResponseDTO guardarProducto(ProductoRequestDTO dto);
    ProductoResponseDTO actualizarProducto(ProductoRequestDTO dto, Long id);
    void eliminarProducto(Long id);
    List<ProductoResponseDTO> listarTodos();
    ProductoResponseDTO buscarPorId(Long id);
    List<ProductoResponseDTO> listarProductosConStockBajo();
}

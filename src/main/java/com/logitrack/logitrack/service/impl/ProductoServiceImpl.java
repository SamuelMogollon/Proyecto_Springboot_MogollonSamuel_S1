package com.logitrack.logitrack.service.impl;

import com.logitrack.logitrack.dto.request.ProductoRequestDTO;
import com.logitrack.logitrack.dto.response.ProductoResponseDTO;
import com.logitrack.logitrack.mapper.ProductoMapper;
import com.logitrack.logitrack.model.Producto;
import com.logitrack.logitrack.repository.ProductoRepository;
import com.logitrack.logitrack.service.ProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductoServiceImpl implements ProductoService {
    private final ProductoRepository productoRepository;
    private final ProductoMapper productoMapper;


    @Override
    public ProductoResponseDTO guardarProducto(ProductoRequestDTO dto) {
        Producto p = ProductoMapper.DTOAEntidad(dto);
        Producto guardado = productoRepository.save(p);
        return productoMapper.entidadADTO(guardado);
    }

    @Override
    public ProductoResponseDTO actualizarProducto(ProductoRequestDTO dto, Long id) {
        Producto p = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No existe el producto"));
        productoMapper.actualizarEntidadDesdeDTO(p, dto);
        Producto actualizado = productoRepository.save(p);
        return productoMapper.entidadADTO(actualizado);
    }

    @Override
    public void eliminarProducto(Long id) {
        productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No existe el producto"));
        productoRepository.deleteById(id);
    }

    @Override
    public List<ProductoResponseDTO> listarTodos() {
        return productoRepository.findAll()
                .stream()
                .map(productoMapper::entidadADTO)
                .toList();
    }

    @Override
    public ProductoResponseDTO buscarPorId(Long id) {
        Producto p = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No existe el producto"));
        return productoMapper.entidadADTO(p);
    }
}

package com.logitrack.logitrack.service.impl;

import com.logitrack.logitrack.dto.request.MovimientoRequestDTO;
import com.logitrack.logitrack.dto.response.DetalleMovimientoResponseDTO;
import com.logitrack.logitrack.dto.response.MovimientoResponseDTO;
import com.logitrack.logitrack.exception.BusinessRuleException;
import com.logitrack.logitrack.mapper.DetalleMovimientoMapper;
import com.logitrack.logitrack.mapper.MovimientoMapper;
import com.logitrack.logitrack.model.*;
import com.logitrack.logitrack.repository.*;
import com.logitrack.logitrack.service.AuditoriaService;
import com.logitrack.logitrack.service.MovimientoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MovimientoServiceImpl implements MovimientoService {

    private final MovimientoRepository movimientoRepository;
    private final MovimientoMapper movimientoMapper;
    private final DetalleMovimientoMapper detalleMovimientoMapper;
    private final DetalleMovimientoRepository detalleMovimientoRepository;
    private final UsuarioRepository usuarioRepository;
    private final BodegaRepository bodegaRepository;
    private final ProductoRepository productoRepository;
    private final BodegaProductoRepository bodegaProductoRepository;
    private final AuditoriaService auditoriaService;

    @Override
    public MovimientoResponseDTO guardarMovimiento(MovimientoRequestDTO dto) {

        Usuario usuario = usuarioRepository.findById(dto.idUsuario())
                .orElseThrow(() -> new BusinessRuleException("No existe el usuario con id: " + dto.idUsuario()));

        Bodega bodegaOrigen = dto.idBodegaOrigen() != null
                ? bodegaRepository.findById(dto.idBodegaOrigen())
                .orElseThrow(() -> new BusinessRuleException("No existe la bodega origen con id: " + dto.idBodegaOrigen()))
                : null;

        Bodega bodegaDestino = dto.idBodegaDestino() != null
                ? bodegaRepository.findById(dto.idBodegaDestino())
                .orElseThrow(() -> new BusinessRuleException("No existe la bodega destino con id: " + dto.idBodegaDestino()))
                : null;

        Movimiento movimiento = movimientoMapper.DTOAEntidad(dto, usuario, bodegaOrigen, bodegaDestino);
        Movimiento guardado = movimientoRepository.save(movimiento);

        List<DetalleMovimientoResponseDTO> detalles = dto.detalles().stream().map(detalleDTO -> {

            Producto producto = productoRepository.findById(detalleDTO.idProducto())
                    .orElseThrow(() -> new BusinessRuleException("No existe el producto con id: " + detalleDTO.idProducto()));

            // Actualizar stock según tipo de movimiento
            actualizarStock(dto.tipo(), bodegaOrigen, bodegaDestino, producto, detalleDTO.cantidad());

            DetalleMovimiento detalle = detalleMovimientoMapper.crearEntidad(guardado, producto, detalleDTO.cantidad());
            DetalleMovimiento detalleGuardado = detalleMovimientoRepository.save(detalle);

            return detalleMovimientoMapper.entidadADTO(detalleGuardado);

        }).toList();

        // Registrar auditoría
        auditoriaService.registrar(
                "Movimiento",
                null,
                "Tipo: " + dto.tipo(),
                Auditoria.TipoOperacion.INSERT,
                dto.idUsuario()
        );

        return movimientoMapper.entidadADTO(guardado, detalles);
    }

    private void actualizarStock(Movimiento.TipoMovimiento tipo,
                                 Bodega origen, Bodega destino,
                                 Producto producto, Integer cantidad) {
        if (tipo == Movimiento.TipoMovimiento.ENTRADA) {
            agregarStock(destino, producto, cantidad);

        } else if (tipo == Movimiento.TipoMovimiento.SALIDA) {
            reducirStock(origen, producto, cantidad);

        } else if (tipo == Movimiento.TipoMovimiento.TRANSFERENCIA) {
            reducirStock(origen, producto, cantidad);
            agregarStock(destino, producto, cantidad);
        }
    }

    private void agregarStock(Bodega bodega, Producto producto, Integer cantidad) {
        BodegaProducto bp = bodegaProductoRepository
                .findByBodegaIdAndProductoId(bodega.getId(), producto.getId())
                .orElseGet(() -> {
                    BodegaProducto nuevo = new BodegaProducto();
                    nuevo.setBodega(bodega);
                    nuevo.setProducto(producto);
                    nuevo.setStock(0);
                    return nuevo;
                });
        bp.setStock(bp.getStock() + cantidad);
        bodegaProductoRepository.save(bp);
    }

    private void reducirStock(Bodega bodega, Producto producto, Integer cantidad) {
        BodegaProducto bp = bodegaProductoRepository
                .findByBodegaIdAndProductoId(bodega.getId(), producto.getId())
                .orElseThrow(() -> new BusinessRuleException("Stock insuficiente para el producto con id: " + producto.getId()));

        if (bp.getStock() < cantidad) {
            throw new BusinessRuleException("No hay stock del producto con id: " + producto.getId() + " en la bodega con id: " + bodega.getId());
        }

        bp.setStock(bp.getStock() - cantidad);
        bodegaProductoRepository.save(bp);
    }

    @Override
    public List<MovimientoResponseDTO> listarTodos() {
        return movimientoRepository.findAll()
                .stream()
                .map(m -> movimientoMapper.entidadADTO(m,
                        m.getDetalles().stream()
                                .map(detalleMovimientoMapper::entidadADTO)
                                .toList()))
                .toList();
    }

    @Override
    public MovimientoResponseDTO buscarPorId(Long id) {
        Movimiento m = movimientoRepository.findById(id)
                .orElseThrow(() -> new BusinessRuleException("No existe el movimiento con id: " + id));
        List<DetalleMovimientoResponseDTO> detalles = m.getDetalles()
                .stream()
                .map(detalleMovimientoMapper::entidadADTO)
                .toList();
        return movimientoMapper.entidadADTO(m, detalles);
    }

    @Override
    public List<MovimientoResponseDTO> buscarPorRangoDeFechas(LocalDateTime inicio, LocalDateTime fin) {
        return movimientoRepository.findByFechaBetween(inicio, fin)
                .stream()
                .map(m -> movimientoMapper.entidadADTO(m,
                        m.getDetalles().stream()
                                .map(detalleMovimientoMapper::entidadADTO)
                                .toList()))
                .toList();
    }
}

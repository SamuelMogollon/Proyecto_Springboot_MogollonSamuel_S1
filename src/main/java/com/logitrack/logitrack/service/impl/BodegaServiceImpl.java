package com.logitrack.logitrack.service.impl;

import com.logitrack.logitrack.dto.request.BodegaRequestDTO;
import com.logitrack.logitrack.dto.response.BodegaResponseDTO;
import com.logitrack.logitrack.exception.BusinessRuleException;
import com.logitrack.logitrack.mapper.BodegaMapper;
import com.logitrack.logitrack.model.Bodega;
import com.logitrack.logitrack.model.Usuario;
import com.logitrack.logitrack.repository.BodegaRepository;
import com.logitrack.logitrack.repository.UsuarioRepository;
import com.logitrack.logitrack.service.BodegaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BodegaServiceImpl implements BodegaService {

    private final BodegaRepository bodegaRepository;
    private final BodegaMapper bodegaMapper;
    private final UsuarioRepository usuarioRepository;

    @Override
    public BodegaResponseDTO guardarBodega(BodegaRequestDTO dto) {
        Usuario encargado = usuarioRepository.findById(dto.idEncargado())
                .orElseThrow(() -> new BusinessRuleException("No existe el encargado con id: " + dto.idEncargado()));
        Bodega b = bodegaMapper.DTOAEntidad(dto, encargado);
        Bodega guardada = bodegaRepository.save(b);
        return bodegaMapper.entidadADTO(guardada);
    }

    @Override
    public BodegaResponseDTO actualizarBodega(BodegaRequestDTO dto, Long id) {
        Bodega b = bodegaRepository.findById(id)
                .orElseThrow(() -> new BusinessRuleException("No existe la bodega con id: " + id));
        Usuario encargado = usuarioRepository.findById(dto.idEncargado())
                .orElseThrow(() -> new BusinessRuleException("No existe el encargado con id: " + dto.idEncargado()));
        bodegaMapper.actualizarEntidadDesdeDTO(b, dto, encargado);
        Bodega actualizada = bodegaRepository.save(b);
        return bodegaMapper.entidadADTO(actualizada);
    }

    @Override
    public void eliminarBodega(Long id) {
        bodegaRepository.findById(id)
                .orElseThrow(() -> new BusinessRuleException("No existe la bodega con id: " + id));
        bodegaRepository.deleteById(id);
    }

    @Override
    public List<BodegaResponseDTO> listarTodos() {
        return bodegaRepository.findAll()
                .stream()
                .map(bodegaMapper::entidadADTO)
                .toList();
    }

    @Override
    public BodegaResponseDTO buscarPorId(Long id) {
        Bodega b = bodegaRepository.findById(id)
                .orElseThrow(() -> new BusinessRuleException("No existe la bodega con id: " + id));
        return bodegaMapper.entidadADTO(b);
    }
}

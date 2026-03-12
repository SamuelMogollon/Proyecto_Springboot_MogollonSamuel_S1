package com.logitrack.logitrack.service.impl;

import com.logitrack.logitrack.dto.request.UsuarioRequestDTO;
import com.logitrack.logitrack.dto.response.UsuarioResponseDTO;
import com.logitrack.logitrack.mapper.UsuarioMapper;
import com.logitrack.logitrack.model.Usuario;
import com.logitrack.logitrack.repository.UsuarioRepository;
import com.logitrack.logitrack.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;


    @Override
    public UsuarioResponseDTO guardarUsuario(UsuarioRequestDTO dto) {
        Usuario u = UsuarioMapper.DTOAEntidad(dto);
        Usuario guardado = usuarioRepository.save(u);
        return usuarioMapper.entidadADTO(guardado);
    }

    @Override
    public UsuarioResponseDTO actualizarUsuario(UsuarioRequestDTO dto, Long id) {
        Usuario u = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No existe el usuario"));
        usuarioMapper.actualizarEntidadDesdeDTO(u, dto);
        Usuario actualizado = usuarioRepository.save(u);
        return usuarioMapper.entidadADTO(actualizado);
    }

    @Override
    public void eliminarUsuario(Long id) {
        usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No existe el usuario"));
        usuarioRepository.deleteById(id);
    }

    @Override
    public List<UsuarioResponseDTO> listarTodos() {
        return usuarioRepository.findAll()
                .stream()
                .map(usuarioMapper::entidadADTO)
                .toList();
    }

    @Override
    public UsuarioResponseDTO buscarPorId(Long id) {
        Usuario u = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No existe el usuario"));
        return usuarioMapper.entidadADTO(u);
    }
}

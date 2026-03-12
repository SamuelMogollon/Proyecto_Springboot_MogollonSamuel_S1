package com.logitrack.logitrack.service;

import com.logitrack.logitrack.dto.request.UsuarioRequestDTO;
import com.logitrack.logitrack.dto.response.UsuarioResponseDTO;
import java.util.List;

public interface UsuarioService {
    UsuarioResponseDTO guardarUsuario(UsuarioRequestDTO dto);
    UsuarioResponseDTO actualizarUsuario(UsuarioRequestDTO dto, Long id);
    void eliminarUsuario(Long id);
    List<UsuarioResponseDTO> listarTodos();
    UsuarioResponseDTO buscarPorId(Long id);
}

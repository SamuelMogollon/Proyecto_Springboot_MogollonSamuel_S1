package com.logitrack.logitrack.service.impl;

import com.logitrack.logitrack.dto.response.AuditoriaResponseDTO;
import com.logitrack.logitrack.mapper.AuditoriaMapper;
import com.logitrack.logitrack.model.Auditoria;
import com.logitrack.logitrack.model.Usuario;
import com.logitrack.logitrack.repository.AuditoriaRepository;
import com.logitrack.logitrack.repository.UsuarioRepository;
import com.logitrack.logitrack.service.AuditoriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuditoriaServiceImpl implements AuditoriaService {

    private final AuditoriaRepository auditoriaRepository;
    private final AuditoriaMapper auditoriaMapper;
    private final UsuarioRepository usuarioRepository;

    @Override
    public List<AuditoriaResponseDTO> listarTodos() {
        return auditoriaRepository.findAll()
                .stream()
                .map(auditoriaMapper::entidadADTO)
                .toList();
    }

    @Override
    public List<AuditoriaResponseDTO> buscarPorUsuario(Long idUsuario) {
        return auditoriaRepository.findByUsuarioId(idUsuario)
                .stream()
                .map(auditoriaMapper::entidadADTO)
                .toList();
    }

    @Override
    public List<AuditoriaResponseDTO> buscarPorTipoOperacion(Auditoria.TipoOperacion tipoOperacion) {
        return auditoriaRepository.findByTipoOperacion(tipoOperacion)
                .stream()
                .map(auditoriaMapper::entidadADTO)
                .toList();
    }

    @Override
    public void registrar(String entidad,
                          String valoresAnteriores,
                          String valoresNuevos,
                          Auditoria.TipoOperacion tipoOperacion,
                          Long idUsuario) {

        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElse(null);

        Auditoria a = new Auditoria();
        a.setEntidadAfectada(entidad);
        a.setValoresAnteriores(valoresAnteriores);
        a.setValoresNuevos(valoresNuevos);
        a.setTipoOperacion(tipoOperacion);
        a.setFechaHora(LocalDateTime.now());
        a.setUsuario(usuario);

        auditoriaRepository.save(a);
    }
}

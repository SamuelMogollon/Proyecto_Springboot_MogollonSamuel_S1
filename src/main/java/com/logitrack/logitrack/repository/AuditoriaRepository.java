package com.logitrack.logitrack.repository;

import com.logitrack.logitrack.model.Auditoria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuditoriaRepository extends JpaRepository<Auditoria, Long> {
    List<Auditoria> findByUsuarioId(Long idUsuario);
    List<Auditoria> findByTipoOperacion(Auditoria.TipoOperacion tipoOperacion);
}
package com.logitrack.logitrack.controller;


import com.logitrack.logitrack.dto.response.AuditoriaResponseDTO;
import com.logitrack.logitrack.model.Auditoria;
import com.logitrack.logitrack.service.impl.AuditoriaServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/auditorias")
@RequiredArgsConstructor
public class AuditoriaController {

    private final AuditoriaServiceImpl auditoriaServiceImpl;

    @GetMapping
    public ResponseEntity<List<AuditoriaResponseDTO>> listarTodos() {
        return ResponseEntity.ok(auditoriaServiceImpl.listarTodos());
    }

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<AuditoriaResponseDTO>> buscarPorUsuario(@PathVariable Long idUsuario) {
        return ResponseEntity.ok(auditoriaServiceImpl.buscarPorUsuario(idUsuario));
    }

    @GetMapping("/tipo/{tipoOperacion}")
    public ResponseEntity<List<AuditoriaResponseDTO>> buscarPorTipo(
            @PathVariable Auditoria.TipoOperacion tipoOperacion) {
        return ResponseEntity.ok(auditoriaServiceImpl.buscarPorTipoOperacion(tipoOperacion));
    }
}

package com.logitrack.logitrack.controller;


import com.logitrack.logitrack.dto.request.BodegaRequestDTO;
import com.logitrack.logitrack.dto.response.BodegaResponseDTO;
import com.logitrack.logitrack.service.impl.BodegaServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bodegas")
@RequiredArgsConstructor
public class BodegaController {

    private final BodegaServiceImpl bodegaServiceImpl;

    @PostMapping
    public ResponseEntity<BodegaResponseDTO> guardar(@RequestBody BodegaRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bodegaServiceImpl.guardarBodega(dto));
    }

    @GetMapping
    public ResponseEntity<List<BodegaResponseDTO>> listarTodos() {
        return ResponseEntity.ok(bodegaServiceImpl.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BodegaResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(bodegaServiceImpl.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BodegaResponseDTO> actualizar(@RequestBody BodegaRequestDTO dto,
                                                        @PathVariable Long id) {
        return ResponseEntity.ok(bodegaServiceImpl.actualizarBodega(dto, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        bodegaServiceImpl.eliminarBodega(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

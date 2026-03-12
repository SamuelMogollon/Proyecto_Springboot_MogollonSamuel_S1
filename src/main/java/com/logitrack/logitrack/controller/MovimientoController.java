package com.logitrack.logitrack.controller;


import com.logitrack.logitrack.dto.request.MovimientoRequestDTO;
import com.logitrack.logitrack.dto.response.MovimientoResponseDTO;
import com.logitrack.logitrack.service.impl.MovimientoServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/movimientos")
@RequiredArgsConstructor
public class MovimientoController {

    private final MovimientoServiceImpl movimientoServiceImpl;

    @PostMapping
    public ResponseEntity<MovimientoResponseDTO> guardar(@RequestBody MovimientoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(movimientoServiceImpl.guardarMovimiento(dto));
    }

    @GetMapping
    public ResponseEntity<List<MovimientoResponseDTO>> listarTodos() {
        return ResponseEntity.ok(movimientoServiceImpl.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovimientoResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(movimientoServiceImpl.buscarPorId(id));
    }

    @GetMapping("/rango")
    public ResponseEntity<List<MovimientoResponseDTO>> buscarPorRango(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {
        return ResponseEntity.ok(movimientoServiceImpl.buscarPorRangoDeFechas(inicio, fin));
    }
}

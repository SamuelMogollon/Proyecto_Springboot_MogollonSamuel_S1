package com.logitrack.logitrack.controller;


import com.logitrack.logitrack.repository.BodegaProductoRepository;
import com.logitrack.logitrack.repository.DetalleMovimientoRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reportes")
@RequiredArgsConstructor
@Tag(name = "Reportes", description = "Reportes y resúmenes generales del sistema")
@SecurityRequirement(name = "bearerAuth")
public class ReporteController {

    private final BodegaProductoRepository bodegaProductoRepository;
    private final DetalleMovimientoRepository detalleMovimientoRepository;

    @Operation(summary = "Resumen general", description = "Retorna stock total por bodega y productos más movidos")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reporte generado exitosamente"),
            @ApiResponse(responseCode = "403", description = "No autorizado")
    })
    @GetMapping("/resumen")
    public ResponseEntity<Map<String, Object>> resumenGeneral() {
        List<Object[]> stockPorBodega = bodegaProductoRepository.stockTotalPorBodega();
        List<Object[]> productosMasMovidos = detalleMovimientoRepository.productosMasMovidos();

        Map<String, Object> stockBodegaMap = new HashMap<>();
        for (Object[] row : stockPorBodega) {
            stockBodegaMap.put((String) row[0], row[1]);
        }

        Map<String, Object> productosMap = new HashMap<>();
        for (Object[] row : productosMasMovidos) {
            productosMap.put((String) row[0], row[1]);
        }

        Map<String, Object> reporte = new HashMap<>();
        reporte.put("stockTotalPorBodega", stockBodegaMap);
        reporte.put("productosMasMovidos", productosMap);

        return ResponseEntity.ok(reporte);
    }
}

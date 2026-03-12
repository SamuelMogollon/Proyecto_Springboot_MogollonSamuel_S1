package com.logitrack.logitrack.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "auditoria")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Auditoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoOperacion tipoOperacion;

    @Column(nullable = false)
    private LocalDateTime fechaHora;

    @Column(nullable = false)
    private String entidadAfectada;

    @Column(columnDefinition = "TEXT")
    private String valoresAnteriores;

    @Column(columnDefinition = "TEXT")
    private String valoresNuevos;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    public enum TipoOperacion {
        INSERT, UPDATE, DELETE
    }
}

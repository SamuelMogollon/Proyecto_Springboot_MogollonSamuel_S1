package com.logitrack.logitrack.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "movimiento")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Movimiento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime fecha;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoMovimiento tipo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_bodega_origen")
    private Bodega bodegaOrigen;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_bodega_destino")
    private Bodega bodegaDestino;

    @OneToMany(mappedBy = "movimiento",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<DetalleMovimiento> detalles;

    public enum TipoMovimiento {
        ENTRADA, SALIDA, TRANSFERENCIA
    }
}

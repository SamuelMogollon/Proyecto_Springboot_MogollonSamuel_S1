package com.logitrack.logitrack.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "detalle_movimiento")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class DetalleMovimiento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_movimiento", nullable = false)
    private Movimiento movimiento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_producto", nullable = false)
    private Producto producto;

    @Column(nullable = false)
    private Integer cantidad;
}

package com.logitrack.logitrack.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "bodega_producto")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class BodegaProducto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_bodega", nullable = false)
    private Bodega bodega;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_producto", nullable = false)
    private Producto producto;

    @Column(nullable = false)
    private Integer stock;
}
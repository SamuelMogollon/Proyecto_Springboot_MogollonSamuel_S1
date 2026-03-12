package com.logitrack.logitrack.repository;

import com.logitrack.logitrack.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
}
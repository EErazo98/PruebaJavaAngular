package com.serfinsa.backend.product.repository;

import com.serfinsa.backend.product.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio JPA para acceder a los productos en la base de datos.
 */
@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    
}

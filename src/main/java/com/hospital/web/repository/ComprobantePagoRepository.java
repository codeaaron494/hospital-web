package com.hospital.web.repository;

import com.hospital.web.entity.ComprobantePago;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ComprobantePagoRepository extends JpaRepository<ComprobantePago, Integer> {

    Optional<ComprobantePago> findByNumeroComprobante(String numeroComprobante);

    List<ComprobantePago> findByEstadoComprobante(String estadoComprobante);

    List<ComprobantePago> findByOrdenCompraIdOrdenCompra(Integer idOrdenCompra);

    boolean existsByNumeroComprobante(String numeroComprobante);
}
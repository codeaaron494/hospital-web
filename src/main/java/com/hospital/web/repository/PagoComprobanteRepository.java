package com.hospital.web.repository;

import com.hospital.web.entity.PagoComprobante;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PagoComprobanteRepository extends JpaRepository<PagoComprobante, Integer> {

    List<PagoComprobante> findByComprobantePagoIdComprobantePago(Integer idComprobantePago);

    List<PagoComprobante> findByEstadoPago(String estadoPago);

    boolean existsByComprobantePagoIdComprobantePago(Integer idComprobantePago);
}
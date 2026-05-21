package com.hospital.web.repository;

import com.hospital.web.entity.GuiaRemision;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GuiaRemisionRepository extends JpaRepository<GuiaRemision, Integer> {

    Optional<GuiaRemision> findByNumeroGuia(String numeroGuia);

    List<GuiaRemision> findByEstadoGuia(String estadoGuia);

    List<GuiaRemision> findByOrdenCompraIdOrdenCompra(Integer idOrdenCompra);

    boolean existsByNumeroGuia(String numeroGuia);
}
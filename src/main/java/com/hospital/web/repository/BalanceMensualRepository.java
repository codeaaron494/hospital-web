package com.hospital.web.repository;

import com.hospital.web.entity.BalanceMensual;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BalanceMensualRepository extends JpaRepository<BalanceMensual, Integer> {

    Optional<BalanceMensual> findByPeriodo(String periodo);

    List<BalanceMensual> findByEstadoBalance(String estadoBalance);

    List<BalanceMensual> findByUsuarioAlmaceneroIdUsuario(Integer idUsuarioAlmacenero);

    List<BalanceMensual> findByUsuarioQuimicoIdUsuario(Integer idUsuarioQuimico);

    boolean existsByPeriodo(String periodo);
}
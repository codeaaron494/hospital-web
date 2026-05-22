package com.hospital.web.repository;

import com.hospital.web.entity.ObservacionBalance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ObservacionBalanceRepository extends JpaRepository<ObservacionBalance, Integer> {

    List<ObservacionBalance> findByBalanceMensualIdBalanceMensual(Integer idBalanceMensual);

    List<ObservacionBalance> findByOrigenObservacion(String origenObservacion);

    List<ObservacionBalance> findByEstadoObservacion(String estadoObservacion);
}
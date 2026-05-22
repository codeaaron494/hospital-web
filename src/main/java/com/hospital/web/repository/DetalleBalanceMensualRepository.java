package com.hospital.web.repository;

import com.hospital.web.entity.DetalleBalanceMensual;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DetalleBalanceMensualRepository extends JpaRepository<DetalleBalanceMensual, Integer> {

    List<DetalleBalanceMensual> findByBalanceMensualIdBalanceMensual(Integer idBalanceMensual);

    List<DetalleBalanceMensual> findByMedicamentoIdMedicamento(Integer idMedicamento);

    Optional<DetalleBalanceMensual> findByBalanceMensualIdBalanceMensualAndMedicamentoIdMedicamento(
            Integer idBalanceMensual,
            Integer idMedicamento
    );
}
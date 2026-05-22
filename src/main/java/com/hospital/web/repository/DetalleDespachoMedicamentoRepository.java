package com.hospital.web.repository;

import com.hospital.web.entity.DetalleDespachoMedicamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DetalleDespachoMedicamentoRepository extends JpaRepository<DetalleDespachoMedicamento, Integer> {

    List<DetalleDespachoMedicamento> findByDespachoMedicamentoIdDespachoMedicamento(Integer idDespachoMedicamento);

    List<DetalleDespachoMedicamento> findByMedicamentoIdMedicamento(Integer idMedicamento);

    Optional<DetalleDespachoMedicamento> findByDespachoMedicamentoIdDespachoMedicamentoAndDetalleRecetaIdDetalleReceta(
            Integer idDespachoMedicamento,
            Integer idDetalleReceta
    );
}
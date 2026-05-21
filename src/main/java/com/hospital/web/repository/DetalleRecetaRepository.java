package com.hospital.web.repository;

import com.hospital.web.entity.DetalleReceta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DetalleRecetaRepository extends JpaRepository<DetalleReceta, Integer> {

    List<DetalleReceta> findByRecetaIdReceta(Integer idReceta);

    List<DetalleReceta> findByMedicamentoIdMedicamento(Integer idMedicamento);
}
package com.hospital.web.repository;

import com.hospital.web.entity.Medicamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MedicamentoRepository extends JpaRepository<Medicamento, Integer> {

    List<Medicamento> findByCategoriaIdCategoria(Integer idCategoria);

    List<Medicamento> findByNombreMedicamentoContainingIgnoreCase(String nombreMedicamento);
}
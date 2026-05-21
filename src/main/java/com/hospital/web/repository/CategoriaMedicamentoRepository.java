package com.hospital.web.repository;

import com.hospital.web.entity.CategoriaMedicamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoriaMedicamentoRepository extends JpaRepository<CategoriaMedicamento, Integer> {

    Optional<CategoriaMedicamento> findByNombreCategoria(String nombreCategoria);
}
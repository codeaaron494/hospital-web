package com.hospital.web.repository;

import com.hospital.web.entity.DespachoMedicamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DespachoMedicamentoRepository extends JpaRepository<DespachoMedicamento, Integer> {

    Optional<DespachoMedicamento> findByRecetaMedicaIdReceta(Integer idReceta);

    List<DespachoMedicamento> findByEstadoDespacho(String estadoDespacho);

    List<DespachoMedicamento> findByUsuarioTecnicoIdUsuario(Integer idUsuarioTecnico);

    boolean existsByRecetaMedicaIdReceta(Integer idReceta);
}
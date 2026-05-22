package com.hospital.web.repository;

import com.hospital.web.entity.DetalleConteoFisico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DetalleConteoFisicoRepository extends JpaRepository<DetalleConteoFisico, Integer> {

    List<DetalleConteoFisico> findByConteoFisicoIdConteoFisico(Integer idConteoFisico);

    List<DetalleConteoFisico> findByMedicamentoIdMedicamento(Integer idMedicamento);

    Optional<DetalleConteoFisico> findByConteoFisicoIdConteoFisicoAndMedicamentoIdMedicamento(
            Integer idConteoFisico,
            Integer idMedicamento
    );
}
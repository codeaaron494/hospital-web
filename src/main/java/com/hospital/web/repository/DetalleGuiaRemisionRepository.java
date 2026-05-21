package com.hospital.web.repository;

import com.hospital.web.entity.DetalleGuiaRemision;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DetalleGuiaRemisionRepository extends JpaRepository<DetalleGuiaRemision, Integer> {

    List<DetalleGuiaRemision> findByGuiaRemisionIdGuiaRemision(Integer idGuiaRemision);

    List<DetalleGuiaRemision> findByMedicamentoIdMedicamento(Integer idMedicamento);
}
package com.hospital.web.repository;

import com.hospital.web.entity.FichaAdmision;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FichaAdmisionRepository extends JpaRepository<FichaAdmision, Integer> {

    List<FichaAdmision> findByPacienteDniPaciente(String dniPaciente);

    List<FichaAdmision> findByEstadoFicha(String estadoFicha);

    Optional<FichaAdmision> findByCitaIdCita(Integer idCita);

    boolean existsByCitaIdCita(Integer idCita);
}
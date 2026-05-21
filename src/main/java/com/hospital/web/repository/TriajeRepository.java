package com.hospital.web.repository;

import com.hospital.web.entity.Triaje;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TriajeRepository extends JpaRepository<Triaje, Integer> {

    Optional<Triaje> findByFichaAdmisionIdFichaAdmision(Integer idFichaAdmision);

    List<Triaje> findByHistoriaClinicaIdHistoriaClinica(Integer idHistoriaClinica);

    boolean existsByFichaAdmisionIdFichaAdmision(Integer idFichaAdmision);
}
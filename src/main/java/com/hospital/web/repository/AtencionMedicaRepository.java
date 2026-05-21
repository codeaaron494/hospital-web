package com.hospital.web.repository;

import com.hospital.web.entity.AtencionMedica;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AtencionMedicaRepository extends JpaRepository<AtencionMedica, Integer> {

    List<AtencionMedica> findByHistoriaClinicaIdHistoriaClinica(Integer idHistoriaClinica);

    List<AtencionMedica> findByMedicoIdMedico(Integer idMedico);
}
package com.hospital.web.repository;

import com.hospital.web.entity.Medico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MedicoRepository extends JpaRepository<Medico, Integer> {

    Optional<Medico> findByCmpMedico(String cmpMedico);

    List<Medico> findByEspecialidadIdEspecialidad(Integer idEspecialidad);
}
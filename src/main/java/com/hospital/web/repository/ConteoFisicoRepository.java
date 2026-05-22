package com.hospital.web.repository;

import com.hospital.web.entity.ConteoFisico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConteoFisicoRepository extends JpaRepository<ConteoFisico, Integer> {

    List<ConteoFisico> findByEstadoConteo(String estadoConteo);

    List<ConteoFisico> findByUsuarioAlmaceneroIdUsuario(Integer idUsuarioAlmacenero);
}
package com.hospital.web.repository;

import com.hospital.web.entity.MovimientoKardex;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovimientoKardexRepository extends JpaRepository<MovimientoKardex, Integer> {

    List<MovimientoKardex> findByKardexIdKardex(Integer idKardex);

    List<MovimientoKardex> findByTipoMovimiento(String tipoMovimiento);

    List<MovimientoKardex> findByUsuarioIdUsuario(Integer idUsuario);
}
package com.hospital.web.repository;

import com.hospital.web.entity.AjusteInventario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AjusteInventarioRepository extends JpaRepository<AjusteInventario, Integer> {

    List<AjusteInventario> findByKardexIdKardex(Integer idKardex);

    List<AjusteInventario> findByDetalleConteoIdDetalleConteo(Integer idDetalleConteo);

    List<AjusteInventario> findByTipoAjuste(String tipoAjuste);
}
package com.hospital.web.repository;

import com.hospital.web.entity.Kardex;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface KardexRepository extends JpaRepository<Kardex, Integer> {

    Optional<Kardex> findByMedicamentoIdMedicamento(Integer idMedicamento);

    List<Kardex> findByEstadoKardex(String estadoKardex);

    List<Kardex> findByStockActualLessThanEqual(Integer stockMinimo);
}
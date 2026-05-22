package com.hospital.web.service;

import com.hospital.web.entity.Kardex;
import com.hospital.web.entity.MovimientoKardex;

import java.util.List;
import java.util.Optional;

public interface KardexService {

    List<Kardex> listarTodos();

    Optional<Kardex> buscarPorId(Integer idKardex);

    Optional<Kardex> buscarPorMedicamento(Integer idMedicamento);

    List<MovimientoKardex> listarMovimientos(Integer idKardex);

    MovimientoKardex registrarMovimiento(
            Integer idKardex,
            String tipoMovimiento,
            Integer cantidad,
            String motivo,
            Integer idUsuario
    );
}
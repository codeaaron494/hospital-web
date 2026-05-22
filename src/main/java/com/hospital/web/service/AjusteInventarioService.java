package com.hospital.web.service;

import com.hospital.web.entity.AjusteInventario;

import java.util.List;
import java.util.Optional;

public interface AjusteInventarioService {

    List<AjusteInventario> listarTodos();

    Optional<AjusteInventario> buscarPorId(Integer idAjusteInventario);

    List<AjusteInventario> buscarPorKardex(Integer idKardex);

    AjusteInventario registrarAjuste(
            Integer idDetalleConteo,
            Integer idUsuarioAlmacenero,
            String motivo
    );
}
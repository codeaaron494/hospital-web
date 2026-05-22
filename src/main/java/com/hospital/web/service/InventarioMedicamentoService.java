package com.hospital.web.service;

import com.hospital.web.entity.Medicamento;

import java.util.List;

public interface InventarioMedicamentoService {

    List<Medicamento> listarTodos();

    Medicamento registrarMedicamentoConKardex(
            String nombreMedicamento,
            String concentracion,
            String presentacion,
            Integer idCategoria,
            Integer stockActual,
            Integer stockMinimo,
            Integer idUsuario
    );
}
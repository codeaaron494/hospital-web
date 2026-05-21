package com.hospital.web.service;

import com.hospital.web.entity.Medicamento;

import java.util.List;
import java.util.Optional;

public interface MedicamentoService {

    List<Medicamento> listarTodos();

    Optional<Medicamento> buscarPorId(Integer idMedicamento);
}
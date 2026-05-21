package com.hospital.web.service.impl;

import com.hospital.web.entity.Medicamento;
import com.hospital.web.repository.MedicamentoRepository;
import com.hospital.web.service.MedicamentoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class MedicamentoServiceImpl implements MedicamentoService {

    private final MedicamentoRepository medicamentoRepository;

    public MedicamentoServiceImpl(MedicamentoRepository medicamentoRepository) {
        this.medicamentoRepository = medicamentoRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Medicamento> listarTodos() {
        return medicamentoRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Medicamento> buscarPorId(Integer idMedicamento) {
        return medicamentoRepository.findById(idMedicamento);
    }
}
package com.hospital.web.service.impl;

import com.hospital.web.entity.Proveedor;
import com.hospital.web.repository.ProveedorRepository;
import com.hospital.web.service.ProveedorService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProveedorServiceImpl implements ProveedorService {

    private static final String ACTIVO = "ACTIVO";

    private final ProveedorRepository proveedorRepository;

    public ProveedorServiceImpl(ProveedorRepository proveedorRepository) {
        this.proveedorRepository = proveedorRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Proveedor> listarTodos() {
        return proveedorRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Proveedor> listarActivos() {
        return proveedorRepository.findByEstadoProveedor(ACTIVO);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Proveedor> buscarPorId(Integer idProveedor) {
        return proveedorRepository.findById(idProveedor);
    }
}
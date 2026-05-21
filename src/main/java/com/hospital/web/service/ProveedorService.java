package com.hospital.web.service;

import com.hospital.web.entity.Proveedor;

import java.util.List;
import java.util.Optional;

public interface ProveedorService {

    List<Proveedor> listarTodos();

    List<Proveedor> listarActivos();

    Optional<Proveedor> buscarPorId(Integer idProveedor);
}
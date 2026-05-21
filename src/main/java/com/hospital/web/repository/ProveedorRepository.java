package com.hospital.web.repository;

import com.hospital.web.entity.Proveedor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProveedorRepository extends JpaRepository<Proveedor, Integer> {

    Optional<Proveedor> findByRucProveedor(String rucProveedor);

    List<Proveedor> findByEstadoProveedor(String estadoProveedor);

    List<Proveedor> findByRazonSocialContainingIgnoreCase(String razonSocial);
}
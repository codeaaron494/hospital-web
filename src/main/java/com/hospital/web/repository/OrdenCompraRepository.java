package com.hospital.web.repository;

import com.hospital.web.entity.OrdenCompra;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrdenCompraRepository extends JpaRepository<OrdenCompra, Integer> {

    List<OrdenCompra> findByEstadoOrden(String estadoOrden);

    List<OrdenCompra> findByProveedorIdProveedor(Integer idProveedor);

    List<OrdenCompra> findByUsuarioAlmaceneroIdUsuario(Integer idUsuarioAlmacenero);

    List<OrdenCompra> findByUsuarioQuimicoIdUsuario(Integer idUsuarioQuimico);
}
package com.hospital.web.repository;

import com.hospital.web.entity.DetalleOrdenCompra;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DetalleOrdenCompraRepository extends JpaRepository<DetalleOrdenCompra, Integer> {

    List<DetalleOrdenCompra> findByOrdenCompraIdOrdenCompra(Integer idOrdenCompra);

    List<DetalleOrdenCompra> findByMedicamentoIdMedicamento(Integer idMedicamento);
}
package com.hospital.web.service;

import com.hospital.web.entity.DespachoMedicamento;
import com.hospital.web.entity.DetalleDespachoMedicamento;
import com.hospital.web.entity.DetalleReceta;
import com.hospital.web.entity.RecetaMedica;

import java.util.List;
import java.util.Optional;

public interface SuministroMedicamentoService {

    List<RecetaMedica> listarRecetas();

    List<RecetaMedica> listarRecetasPorEstado(String estadoReceta);

    Optional<RecetaMedica> buscarRecetaPorId(Integer idReceta);

    List<DetalleReceta> listarDetalleReceta(Integer idReceta);

    Optional<DespachoMedicamento> buscarDespachoPorReceta(Integer idReceta);

    List<DespachoMedicamento> listarDespachos();

    List<DespachoMedicamento> listarDespachosPorEstado(String estadoDespacho);

    Optional<DespachoMedicamento> buscarDespachoPorId(Integer idDespacho);

    List<DetalleDespachoMedicamento> listarDetalleDespacho(Integer idDespacho);

    boolean recetaYaDespachada(Integer idReceta);

    boolean recetaTieneStockSuficiente(Integer idReceta);

    DespachoMedicamento registrarDespachoAtendido(
            Integer idReceta,
            Integer idUsuarioTecnico,
            String observacion
    );

    DespachoMedicamento registrarDespachoRechazado(
            Integer idReceta,
            Integer idUsuarioTecnico,
            String estadoDespacho,
            String observacion
    );
}
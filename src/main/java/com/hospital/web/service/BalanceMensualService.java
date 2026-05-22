package com.hospital.web.service;

import com.hospital.web.entity.BalanceMensual;
import com.hospital.web.entity.DetalleBalanceMensual;
import com.hospital.web.entity.ObservacionBalance;

import java.util.List;
import java.util.Optional;

public interface BalanceMensualService {

    List<BalanceMensual> listarTodos();

    List<BalanceMensual> listarPorEstado(String estadoBalance);

    Optional<BalanceMensual> buscarPorId(Integer idBalanceMensual);

    List<DetalleBalanceMensual> listarDetalle(Integer idBalanceMensual);

    List<ObservacionBalance> listarObservaciones(Integer idBalanceMensual);

    BalanceMensual generarBalance(
            String periodo,
            Integer idUsuarioAlmacenero,
            String observacion
    );

    BalanceMensual enviarAQuimico(Integer idBalanceMensual);

    BalanceMensual aprobarBalance(
            Integer idBalanceMensual,
            Integer idUsuarioQuimico,
            String observacion
    );

    BalanceMensual observarBalance(
            Integer idBalanceMensual,
            Integer idUsuarioQuimico,
            String descripcion
    );

    BalanceMensual exportarDigemid(Integer idBalanceMensual);

    BalanceMensual registrarConformidadDigemid(Integer idBalanceMensual);

    BalanceMensual registrarObservacionDigemid(
            Integer idBalanceMensual,
            String descripcion
    );

    ObservacionBalance subsanarObservacion(Integer idObservacionBalance);
}
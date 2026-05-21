package com.hospital.web.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "pago_comprobante")
public class PagoComprobante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pago_comprobante")
    private Integer idPagoComprobante;

    @Column(name = "fecha_pago", nullable = false)
    private LocalDateTime fechaPago;

    @Column(name = "monto_pagado", nullable = false, precision = 10, scale = 2)
    private BigDecimal montoPagado;

    @Column(name = "medio_pago", nullable = false, length = 50)
    private String medioPago;

    @Column(name = "estado_pago", nullable = false, length = 30)
    private String estadoPago;

    @Column(name = "observacion", columnDefinition = "TEXT")
    private String observacion;

    @ManyToOne
    @JoinColumn(name = "id_comprobante_pago", nullable = false)
    private ComprobantePago comprobantePago;

    @ManyToOne
    @JoinColumn(name = "id_usuario_cobranza", nullable = false)
    private Usuario usuarioCobranza;

    public PagoComprobante() {
    }

    public Integer getIdPagoComprobante() {
        return idPagoComprobante;
    }

    public void setIdPagoComprobante(Integer idPagoComprobante) {
        this.idPagoComprobante = idPagoComprobante;
    }

    public LocalDateTime getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(LocalDateTime fechaPago) {
        this.fechaPago = fechaPago;
    }

    public BigDecimal getMontoPagado() {
        return montoPagado;
    }

    public void setMontoPagado(BigDecimal montoPagado) {
        this.montoPagado = montoPagado;
    }

    public String getMedioPago() {
        return medioPago;
    }

    public void setMedioPago(String medioPago) {
        this.medioPago = medioPago;
    }

    public String getEstadoPago() {
        return estadoPago;
    }

    public void setEstadoPago(String estadoPago) {
        this.estadoPago = estadoPago;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public ComprobantePago getComprobantePago() {
        return comprobantePago;
    }

    public void setComprobantePago(ComprobantePago comprobantePago) {
        this.comprobantePago = comprobantePago;
    }

    public Usuario getUsuarioCobranza() {
        return usuarioCobranza;
    }

    public void setUsuarioCobranza(Usuario usuarioCobranza) {
        this.usuarioCobranza = usuarioCobranza;
    }
}
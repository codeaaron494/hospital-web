package com.hospital.web.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "observacion_balance")
public class ObservacionBalance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_observacion_balance")
    private Integer idObservacionBalance;

    @ManyToOne
    @JoinColumn(name = "id_balance_mensual", nullable = false)
    private BalanceMensual balanceMensual;

    @Column(name = "origen_observacion", nullable = false, length = 30)
    private String origenObservacion;

    @Column(name = "descripcion", nullable = false, columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "fecha_observacion", nullable = false)
    private LocalDateTime fechaObservacion;

    @Column(name = "estado_observacion", nullable = false, length = 30)
    private String estadoObservacion;

    public ObservacionBalance() {
    }

    public Integer getIdObservacionBalance() {
        return idObservacionBalance;
    }

    public void setIdObservacionBalance(Integer idObservacionBalance) {
        this.idObservacionBalance = idObservacionBalance;
    }

    public BalanceMensual getBalanceMensual() {
        return balanceMensual;
    }

    public void setBalanceMensual(BalanceMensual balanceMensual) {
        this.balanceMensual = balanceMensual;
    }

    public String getOrigenObservacion() {
        return origenObservacion;
    }

    public void setOrigenObservacion(String origenObservacion) {
        this.origenObservacion = origenObservacion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDateTime getFechaObservacion() {
        return fechaObservacion;
    }

    public void setFechaObservacion(LocalDateTime fechaObservacion) {
        this.fechaObservacion = fechaObservacion;
    }

    public String getEstadoObservacion() {
        return estadoObservacion;
    }

    public void setEstadoObservacion(String estadoObservacion) {
        this.estadoObservacion = estadoObservacion;
    }
}
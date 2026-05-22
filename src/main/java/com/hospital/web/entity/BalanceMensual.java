package com.hospital.web.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "balance_mensual")
public class BalanceMensual {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_balance_mensual")
    private Integer idBalanceMensual;

    @Column(name = "periodo", nullable = false, unique = true, length = 7)
    private String periodo;

    @Column(name = "fecha_generacion", nullable = false)
    private LocalDateTime fechaGeneracion;

    @Column(name = "estado_balance", nullable = false, length = 40)
    private String estadoBalance;

    @Column(name = "observacion", columnDefinition = "TEXT")
    private String observacion;

    @ManyToOne
    @JoinColumn(name = "id_usuario_almacenero", nullable = false)
    private Usuario usuarioAlmacenero;

    @ManyToOne
    @JoinColumn(name = "id_usuario_quimico")
    private Usuario usuarioQuimico;

    public BalanceMensual() {
    }

    public Integer getIdBalanceMensual() {
        return idBalanceMensual;
    }

    public void setIdBalanceMensual(Integer idBalanceMensual) {
        this.idBalanceMensual = idBalanceMensual;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    public LocalDateTime getFechaGeneracion() {
        return fechaGeneracion;
    }

    public void setFechaGeneracion(LocalDateTime fechaGeneracion) {
        this.fechaGeneracion = fechaGeneracion;
    }

    public String getEstadoBalance() {
        return estadoBalance;
    }

    public void setEstadoBalance(String estadoBalance) {
        this.estadoBalance = estadoBalance;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public Usuario getUsuarioAlmacenero() {
        return usuarioAlmacenero;
    }

    public void setUsuarioAlmacenero(Usuario usuarioAlmacenero) {
        this.usuarioAlmacenero = usuarioAlmacenero;
    }

    public Usuario getUsuarioQuimico() {
        return usuarioQuimico;
    }

    public void setUsuarioQuimico(Usuario usuarioQuimico) {
        this.usuarioQuimico = usuarioQuimico;
    }
}
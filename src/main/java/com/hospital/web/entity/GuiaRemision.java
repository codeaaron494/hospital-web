package com.hospital.web.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "guia_remision")
public class GuiaRemision {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_guia_remision")
    private Integer idGuiaRemision;

    @Column(name = "numero_guia", nullable = false, unique = true, length = 30)
    private String numeroGuia;

    @Column(name = "fecha_emision", nullable = false)
    private LocalDate fechaEmision;

    @Column(name = "fecha_recepcion", nullable = false)
    private LocalDateTime fechaRecepcion;

    @Column(name = "estado_guia", nullable = false, length = 30)
    private String estadoGuia;

    @Column(name = "observacion", columnDefinition = "TEXT")
    private String observacion;

    @ManyToOne
    @JoinColumn(name = "id_orden_compra", nullable = false)
    private OrdenCompra ordenCompra;

    @ManyToOne
    @JoinColumn(name = "id_usuario_almacenero", nullable = false)
    private Usuario usuarioAlmacenero;

    public GuiaRemision() {
    }

    public Integer getIdGuiaRemision() {
        return idGuiaRemision;
    }

    public void setIdGuiaRemision(Integer idGuiaRemision) {
        this.idGuiaRemision = idGuiaRemision;
    }

    public String getNumeroGuia() {
        return numeroGuia;
    }

    public void setNumeroGuia(String numeroGuia) {
        this.numeroGuia = numeroGuia;
    }

    public LocalDate getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(LocalDate fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public LocalDateTime getFechaRecepcion() {
        return fechaRecepcion;
    }

    public void setFechaRecepcion(LocalDateTime fechaRecepcion) {
        this.fechaRecepcion = fechaRecepcion;
    }

    public String getEstadoGuia() {
        return estadoGuia;
    }

    public void setEstadoGuia(String estadoGuia) {
        this.estadoGuia = estadoGuia;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public OrdenCompra getOrdenCompra() {
        return ordenCompra;
    }

    public void setOrdenCompra(OrdenCompra ordenCompra) {
        this.ordenCompra = ordenCompra;
    }

    public Usuario getUsuarioAlmacenero() {
        return usuarioAlmacenero;
    }

    public void setUsuarioAlmacenero(Usuario usuarioAlmacenero) {
        this.usuarioAlmacenero = usuarioAlmacenero;
    }
}
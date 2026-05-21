package com.hospital.web.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "orden_compra")
public class OrdenCompra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_orden_compra")
    private Integer idOrdenCompra;

    @Column(name = "fecha_emision", nullable = false)
    private LocalDateTime fechaEmision;

    @Column(name = "estado_orden", nullable = false, length = 30)
    private String estadoOrden;

    @Column(name = "observacion", columnDefinition = "TEXT")
    private String observacion;

    @ManyToOne
    @JoinColumn(name = "id_proveedor", nullable = false)
    private Proveedor proveedor;

    @ManyToOne
    @JoinColumn(name = "id_usuario_almacenero", nullable = false)
    private Usuario usuarioAlmacenero;

    @ManyToOne
    @JoinColumn(name = "id_usuario_quimico")
    private Usuario usuarioQuimico;

    public OrdenCompra() {
    }

    public Integer getIdOrdenCompra() {
        return idOrdenCompra;
    }

    public void setIdOrdenCompra(Integer idOrdenCompra) {
        this.idOrdenCompra = idOrdenCompra;
    }

    public LocalDateTime getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(LocalDateTime fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public String getEstadoOrden() {
        return estadoOrden;
    }

    public void setEstadoOrden(String estadoOrden) {
        this.estadoOrden = estadoOrden;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public Proveedor getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
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
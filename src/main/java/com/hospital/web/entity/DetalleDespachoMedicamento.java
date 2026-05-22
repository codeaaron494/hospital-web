package com.hospital.web.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "detalle_despacho_medicamento")
public class DetalleDespachoMedicamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_detalle_despacho")
    private Integer idDetalleDespacho;

    @ManyToOne
    @JoinColumn(name = "id_despacho_medicamento", nullable = false)
    private DespachoMedicamento despachoMedicamento;

    @ManyToOne
    @JoinColumn(name = "id_detalle_receta", nullable = false)
    private DetalleReceta detalleReceta;

    @ManyToOne
    @JoinColumn(name = "id_medicamento", nullable = false)
    private Medicamento medicamento;

    @Column(name = "cantidad_recetada", nullable = false)
    private Integer cantidadRecetada;

    @Column(name = "cantidad_entregada", nullable = false)
    private Integer cantidadEntregada;

    @Column(name = "estado_detalle", nullable = false, length = 30)
    private String estadoDetalle;

    @Column(name = "observacion", length = 255)
    private String observacion;

    public DetalleDespachoMedicamento() {
    }

    public Integer getIdDetalleDespacho() {
        return idDetalleDespacho;
    }

    public void setIdDetalleDespacho(Integer idDetalleDespacho) {
        this.idDetalleDespacho = idDetalleDespacho;
    }

    public DespachoMedicamento getDespachoMedicamento() {
        return despachoMedicamento;
    }

    public void setDespachoMedicamento(DespachoMedicamento despachoMedicamento) {
        this.despachoMedicamento = despachoMedicamento;
    }

    public DetalleReceta getDetalleReceta() {
        return detalleReceta;
    }

    public void setDetalleReceta(DetalleReceta detalleReceta) {
        this.detalleReceta = detalleReceta;
    }

    public Medicamento getMedicamento() {
        return medicamento;
    }

    public void setMedicamento(Medicamento medicamento) {
        this.medicamento = medicamento;
    }

    public Integer getCantidadRecetada() {
        return cantidadRecetada;
    }

    public void setCantidadRecetada(Integer cantidadRecetada) {
        this.cantidadRecetada = cantidadRecetada;
    }

    public Integer getCantidadEntregada() {
        return cantidadEntregada;
    }

    public void setCantidadEntregada(Integer cantidadEntregada) {
        this.cantidadEntregada = cantidadEntregada;
    }

    public String getEstadoDetalle() {
        return estadoDetalle;
    }

    public void setEstadoDetalle(String estadoDetalle) {
        this.estadoDetalle = estadoDetalle;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }
}
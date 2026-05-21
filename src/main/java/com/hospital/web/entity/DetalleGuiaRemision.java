package com.hospital.web.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "detalle_guia_remision")
public class DetalleGuiaRemision {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_detalle_guia")
    private Integer idDetalleGuia;

    @Column(name = "cantidad_recibida", nullable = false)
    private Integer cantidadRecibida;

    @Column(name = "observacion", length = 255)
    private String observacion;

    @ManyToOne
    @JoinColumn(name = "id_guia_remision", nullable = false)
    private GuiaRemision guiaRemision;

    @ManyToOne
    @JoinColumn(name = "id_medicamento", nullable = false)
    private Medicamento medicamento;

    public DetalleGuiaRemision() {
    }

    public Integer getIdDetalleGuia() {
        return idDetalleGuia;
    }

    public void setIdDetalleGuia(Integer idDetalleGuia) {
        this.idDetalleGuia = idDetalleGuia;
    }

    public Integer getCantidadRecibida() {
        return cantidadRecibida;
    }

    public void setCantidadRecibida(Integer cantidadRecibida) {
        this.cantidadRecibida = cantidadRecibida;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public GuiaRemision getGuiaRemision() {
        return guiaRemision;
    }

    public void setGuiaRemision(GuiaRemision guiaRemision) {
        this.guiaRemision = guiaRemision;
    }

    public Medicamento getMedicamento() {
        return medicamento;
    }

    public void setMedicamento(Medicamento medicamento) {
        this.medicamento = medicamento;
    }
}
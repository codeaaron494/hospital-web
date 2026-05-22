package com.hospital.web.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "detalle_conteo_fisico")
public class DetalleConteoFisico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_detalle_conteo")
    private Integer idDetalleConteo;

    @ManyToOne
    @JoinColumn(name = "id_conteo_fisico", nullable = false)
    private ConteoFisico conteoFisico;

    @ManyToOne
    @JoinColumn(name = "id_medicamento", nullable = false)
    private Medicamento medicamento;

    @Column(name = "stock_sistema", nullable = false)
    private Integer stockSistema;

    @Column(name = "stock_fisico", nullable = false)
    private Integer stockFisico;

    @Column(name = "diferencia", nullable = false)
    private Integer diferencia;

    @Column(name = "observacion", length = 255)
    private String observacion;

    public DetalleConteoFisico() {
    }

    public Integer getIdDetalleConteo() {
        return idDetalleConteo;
    }

    public void setIdDetalleConteo(Integer idDetalleConteo) {
        this.idDetalleConteo = idDetalleConteo;
    }

    public ConteoFisico getConteoFisico() {
        return conteoFisico;
    }

    public void setConteoFisico(ConteoFisico conteoFisico) {
        this.conteoFisico = conteoFisico;
    }

    public Medicamento getMedicamento() {
        return medicamento;
    }

    public void setMedicamento(Medicamento medicamento) {
        this.medicamento = medicamento;
    }

    public Integer getStockSistema() {
        return stockSistema;
    }

    public void setStockSistema(Integer stockSistema) {
        this.stockSistema = stockSistema;
    }

    public Integer getStockFisico() {
        return stockFisico;
    }

    public void setStockFisico(Integer stockFisico) {
        this.stockFisico = stockFisico;
    }

    public Integer getDiferencia() {
        return diferencia;
    }

    public void setDiferencia(Integer diferencia) {
        this.diferencia = diferencia;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }
}
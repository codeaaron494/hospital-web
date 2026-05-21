package com.hospital.web.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "medicamento")
public class Medicamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_medicamento")
    private Integer idMedicamento;

    @Column(name = "nombre_medicamento", nullable = false, length = 150)
    private String nombreMedicamento;

    @Column(name = "concentracion", length = 50)
    private String concentracion;

    @Column(name = "presentacion", length = 50)
    private String presentacion;

    @Column(name = "stock_actual", nullable = false)
    private Integer stockActual;

    @Column(name = "stock_minimo", nullable = false)
    private Integer stockMinimo;

    @ManyToOne
    @JoinColumn(name = "id_categoria", nullable = false)
    private CategoriaMedicamento categoria;

    public Medicamento() {
    }

    public Integer getIdMedicamento() {
        return idMedicamento;
    }

    public void setIdMedicamento(Integer idMedicamento) {
        this.idMedicamento = idMedicamento;
    }

    public String getNombreMedicamento() {
        return nombreMedicamento;
    }

    public void setNombreMedicamento(String nombreMedicamento) {
        this.nombreMedicamento = nombreMedicamento;
    }

    public String getConcentracion() {
        return concentracion;
    }

    public void setConcentracion(String concentracion) {
        this.concentracion = concentracion;
    }

    public String getPresentacion() {
        return presentacion;
    }

    public void setPresentacion(String presentacion) {
        this.presentacion = presentacion;
    }

    public Integer getStockActual() {
        return stockActual;
    }

    public void setStockActual(Integer stockActual) {
        this.stockActual = stockActual;
    }

    public Integer getStockMinimo() {
        return stockMinimo;
    }

    public void setStockMinimo(Integer stockMinimo) {
        this.stockMinimo = stockMinimo;
    }

    public CategoriaMedicamento getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaMedicamento categoria) {
        this.categoria = categoria;
    }

    public String getNombreCompleto() {
        return nombreMedicamento + " " + concentracion + " - " + presentacion;
    }
}
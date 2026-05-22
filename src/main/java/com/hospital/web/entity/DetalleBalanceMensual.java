package com.hospital.web.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "detalle_balance_mensual")
public class DetalleBalanceMensual {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_detalle_balance")
    private Integer idDetalleBalance;

    @ManyToOne
    @JoinColumn(name = "id_balance_mensual", nullable = false)
    private BalanceMensual balanceMensual;

    @ManyToOne
    @JoinColumn(name = "id_medicamento", nullable = false)
    private Medicamento medicamento;

    @Column(name = "stock_inicial", nullable = false)
    private Integer stockInicial;

    @Column(name = "ingresos", nullable = false)
    private Integer ingresos;

    @Column(name = "salidas", nullable = false)
    private Integer salidas;

    @Column(name = "ajustes", nullable = false)
    private Integer ajustes;

    @Column(name = "stock_final", nullable = false)
    private Integer stockFinal;

    public DetalleBalanceMensual() {
    }

    public Integer getIdDetalleBalance() {
        return idDetalleBalance;
    }

    public void setIdDetalleBalance(Integer idDetalleBalance) {
        this.idDetalleBalance = idDetalleBalance;
    }

    public BalanceMensual getBalanceMensual() {
        return balanceMensual;
    }

    public void setBalanceMensual(BalanceMensual balanceMensual) {
        this.balanceMensual = balanceMensual;
    }

    public Medicamento getMedicamento() {
        return medicamento;
    }

    public void setMedicamento(Medicamento medicamento) {
        this.medicamento = medicamento;
    }

    public Integer getStockInicial() {
        return stockInicial;
    }

    public void setStockInicial(Integer stockInicial) {
        this.stockInicial = stockInicial;
    }

    public Integer getIngresos() {
        return ingresos;
    }

    public void setIngresos(Integer ingresos) {
        this.ingresos = ingresos;
    }

    public Integer getSalidas() {
        return salidas;
    }

    public void setSalidas(Integer salidas) {
        this.salidas = salidas;
    }

    public Integer getAjustes() {
        return ajustes;
    }

    public void setAjustes(Integer ajustes) {
        this.ajustes = ajustes;
    }

    public Integer getStockFinal() {
        return stockFinal;
    }

    public void setStockFinal(Integer stockFinal) {
        this.stockFinal = stockFinal;
    }
}
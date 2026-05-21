package com.hospital.web.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "triaje")
public class Triaje {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_triaje")
    private Integer idTriaje;

    @Column(name = "peso", precision = 5, scale = 2)
    private BigDecimal peso;

    @Column(name = "talla", precision = 3, scale = 2)
    private BigDecimal talla;

    @Column(name = "temperatura", precision = 4, scale = 2)
    private BigDecimal temperatura;

    @Column(name = "presion_arterial", length = 15)
    private String presionArterial;

    @Column(name = "frecuencia_cardiaca")
    private Integer frecuenciaCardiaca;

    @OneToOne
    @JoinColumn(name = "id_ficha_admision", nullable = false, unique = true)
    private FichaAdmision fichaAdmision;

    @ManyToOne
    @JoinColumn(name = "id_historia_clinica", nullable = false)
    private HistoriaClinica historiaClinica;

    public Triaje() {
    }

    public Integer getIdTriaje() {
        return idTriaje;
    }

    public void setIdTriaje(Integer idTriaje) {
        this.idTriaje = idTriaje;
    }

    public BigDecimal getPeso() {
        return peso;
    }

    public void setPeso(BigDecimal peso) {
        this.peso = peso;
    }

    public BigDecimal getTalla() {
        return talla;
    }

    public void setTalla(BigDecimal talla) {
        this.talla = talla;
    }

    public BigDecimal getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(BigDecimal temperatura) {
        this.temperatura = temperatura;
    }

    public String getPresionArterial() {
        return presionArterial;
    }

    public void setPresionArterial(String presionArterial) {
        this.presionArterial = presionArterial;
    }

    public Integer getFrecuenciaCardiaca() {
        return frecuenciaCardiaca;
    }

    public void setFrecuenciaCardiaca(Integer frecuenciaCardiaca) {
        this.frecuenciaCardiaca = frecuenciaCardiaca;
    }

    public FichaAdmision getFichaAdmision() {
        return fichaAdmision;
    }

    public void setFichaAdmision(FichaAdmision fichaAdmision) {
        this.fichaAdmision = fichaAdmision;
    }

    public HistoriaClinica getHistoriaClinica() {
        return historiaClinica;
    }

    public void setHistoriaClinica(HistoriaClinica historiaClinica) {
        this.historiaClinica = historiaClinica;
    }
}
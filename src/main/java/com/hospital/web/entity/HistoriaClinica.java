package com.hospital.web.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "historia_clinica")
public class HistoriaClinica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_historia_clinica")
    private Integer idHistoriaClinica;

    @Column(name = "fecha_apertura", nullable = false)
    private LocalDate fechaApertura;

    @Column(name = "estado_historia", nullable = false, length = 20)
    private String estadoHistoria;

    @OneToOne
    @JoinColumn(name = "id_paciente", nullable = false, unique = true)
    private Paciente paciente;

    public HistoriaClinica() {
    }

    public Integer getIdHistoriaClinica() {
        return idHistoriaClinica;
    }

    public void setIdHistoriaClinica(Integer idHistoriaClinica) {
        this.idHistoriaClinica = idHistoriaClinica;
    }

    public LocalDate getFechaApertura() {
        return fechaApertura;
    }

    public void setFechaApertura(LocalDate fechaApertura) {
        this.fechaApertura = fechaApertura;
    }

    public String getEstadoHistoria() {
        return estadoHistoria;
    }

    public void setEstadoHistoria(String estadoHistoria) {
        this.estadoHistoria = estadoHistoria;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }
}
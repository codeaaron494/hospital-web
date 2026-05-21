package com.hospital.web.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ficha_admision")
public class FichaAdmision {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ficha_admision")
    private Integer idFichaAdmision;

    @Column(name = "fecha_admision", nullable = false)
    private LocalDateTime fechaAdmision;

    @Column(name = "tipo_admision", length = 50)
    private String tipoAdmision;

    @Column(name = "prioridad", length = 20)
    private String prioridad;

    @Column(name = "estado_ficha", nullable = false, length = 20)
    private String estadoFicha;

    @ManyToOne
    @JoinColumn(name = "id_paciente", nullable = false)
    private Paciente paciente;

    @OneToOne
    @JoinColumn(name = "id_cita", nullable = false, unique = true)
    private CitaMedica cita;

    public FichaAdmision() {
    }

    public Integer getIdFichaAdmision() {
        return idFichaAdmision;
    }

    public void setIdFichaAdmision(Integer idFichaAdmision) {
        this.idFichaAdmision = idFichaAdmision;
    }

    public LocalDateTime getFechaAdmision() {
        return fechaAdmision;
    }

    public void setFechaAdmision(LocalDateTime fechaAdmision) {
        this.fechaAdmision = fechaAdmision;
    }

    public String getTipoAdmision() {
        return tipoAdmision;
    }

    public void setTipoAdmision(String tipoAdmision) {
        this.tipoAdmision = tipoAdmision;
    }

    public String getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(String prioridad) {
        this.prioridad = prioridad;
    }

    public String getEstadoFicha() {
        return estadoFicha;
    }

    public void setEstadoFicha(String estadoFicha) {
        this.estadoFicha = estadoFicha;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public CitaMedica getCita() {
        return cita;
    }

    public void setCita(CitaMedica cita) {
        this.cita = cita;
    }
}
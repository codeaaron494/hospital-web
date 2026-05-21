package com.hospital.web.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "agenda_medica")
public class AgendaMedica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_agenda")
    private Integer idAgenda;

    @Column(name = "fecha_agenda", nullable = false)
    private LocalDate fechaAgenda;

    @Column(name = "hora_inicio", nullable = false)
    private LocalTime horaInicio;

    @Column(name = "hora_fin", nullable = false)
    private LocalTime horaFin;

    @Column(name = "estado_horario", nullable = false, length = 20)
    private String estadoHorario;

    @ManyToOne
    @JoinColumn(name = "id_medico", nullable = false)
    private Medico medico;

    public AgendaMedica() {
    }

    public Integer getIdAgenda() {
        return idAgenda;
    }

    public void setIdAgenda(Integer idAgenda) {
        this.idAgenda = idAgenda;
    }

    public LocalDate getFechaAgenda() {
        return fechaAgenda;
    }

    public void setFechaAgenda(LocalDate fechaAgenda) {
        this.fechaAgenda = fechaAgenda;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
    }

    public LocalTime getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(LocalTime horaFin) {
        this.horaFin = horaFin;
    }

    public String getEstadoHorario() {
        return estadoHorario;
    }

    public void setEstadoHorario(String estadoHorario) {
        this.estadoHorario = estadoHorario;
    }

    public Medico getMedico() {
        return medico;
    }

    public void setMedico(Medico medico) {
        this.medico = medico;
    }
}
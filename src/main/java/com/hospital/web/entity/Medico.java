package com.hospital.web.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "medico")
public class Medico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_medico")
    private Integer idMedico;

    @Column(name = "nombres_medico", nullable = false, length = 100)
    private String nombresMedico;

    @Column(name = "apellidos_medico", nullable = false, length = 100)
    private String apellidosMedico;

    @Column(name = "cmp_medico", nullable = false, unique = true, length = 10)
    private String cmpMedico;

    @ManyToOne
    @JoinColumn(name = "id_especialidad", nullable = false)
    private Especialidad especialidad;

    public Medico() {
    }

    public Integer getIdMedico() {
        return idMedico;
    }

    public void setIdMedico(Integer idMedico) {
        this.idMedico = idMedico;
    }

    public String getNombresMedico() {
        return nombresMedico;
    }

    public void setNombresMedico(String nombresMedico) {
        this.nombresMedico = nombresMedico;
    }

    public String getApellidosMedico() {
        return apellidosMedico;
    }

    public void setApellidosMedico(String apellidosMedico) {
        this.apellidosMedico = apellidosMedico;
    }

    public String getCmpMedico() {
        return cmpMedico;
    }

    public void setCmpMedico(String cmpMedico) {
        this.cmpMedico = cmpMedico;
    }

    public Especialidad getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(Especialidad especialidad) {
        this.especialidad = especialidad;
    }

    public String getNombreCompleto() {
        return nombresMedico + " " + apellidosMedico;
    }
}
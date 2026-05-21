package com.hospital.web.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "paciente")
public class Paciente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_paciente")
    private Integer idPaciente;

    @Column(name = "dni_paciente", nullable = false, unique = true, length = 8)
    private String dniPaciente;

    @Column(name = "nombres_paciente", nullable = false, length = 100)
    private String nombresPaciente;

    @Column(name = "apellidos_paciente", nullable = false, length = 100)
    private String apellidosPaciente;

    @Column(name = "telefono_paciente", length = 15)
    private String telefonoPaciente;

    @Column(name = "estado_paciente", nullable = false, length = 20)
    private String estadoPaciente = "ACTIVO";

    public Paciente() {
    }

    public Integer getIdPaciente() {
        return idPaciente;
    }

    public void setIdPaciente(Integer idPaciente) {
        this.idPaciente = idPaciente;
    }

    public String getDniPaciente() {
        return dniPaciente;
    }

    public void setDniPaciente(String dniPaciente) {
        this.dniPaciente = dniPaciente;
    }

    public String getNombresPaciente() {
        return nombresPaciente;
    }

    public void setNombresPaciente(String nombresPaciente) {
        this.nombresPaciente = nombresPaciente;
    }

    public String getApellidosPaciente() {
        return apellidosPaciente;
    }

    public void setApellidosPaciente(String apellidosPaciente) {
        this.apellidosPaciente = apellidosPaciente;
    }

    public String getTelefonoPaciente() {
        return telefonoPaciente;
    }

    public void setTelefonoPaciente(String telefonoPaciente) {
        this.telefonoPaciente = telefonoPaciente;
    }

    public String getEstadoPaciente() {
        return estadoPaciente;
    }

    public void setEstadoPaciente(String estadoPaciente) {
        this.estadoPaciente = estadoPaciente;
    }

    public String getNombreCompleto() {
        return nombresPaciente + " " + apellidosPaciente;
    }
}
package com.hospital.web.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "receta_medica")
public class RecetaMedica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_receta")
    private Integer idReceta;

    @Column(name = "fecha_emision", nullable = false)
    private LocalDateTime fechaEmision;

    @Column(name = "estado_receta", nullable = false, length = 20)
    private String estadoReceta;

    @OneToOne
    @JoinColumn(name = "id_atencion", nullable = false, unique = true)
    private AtencionMedica atencion;

    public RecetaMedica() {
    }

    public Integer getIdReceta() {
        return idReceta;
    }

    public void setIdReceta(Integer idReceta) {
        this.idReceta = idReceta;
    }

    public LocalDateTime getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(LocalDateTime fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public String getEstadoReceta() {
        return estadoReceta;
    }

    public void setEstadoReceta(String estadoReceta) {
        this.estadoReceta = estadoReceta;
    }

    public AtencionMedica getAtencion() {
        return atencion;
    }

    public void setAtencion(AtencionMedica atencion) {
        this.atencion = atencion;
    }
}
package com.hospital.web.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "despacho_medicamento")
public class DespachoMedicamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_despacho_medicamento")
    private Integer idDespachoMedicamento;

    @OneToOne
    @JoinColumn(name = "id_receta", nullable = false, unique = true)
    private RecetaMedica recetaMedica;

    @Column(name = "fecha_despacho", nullable = false)
    private LocalDateTime fechaDespacho;

    @Column(name = "estado_despacho", nullable = false, length = 30)
    private String estadoDespacho;

    @Column(name = "observacion", columnDefinition = "TEXT")
    private String observacion;

    @ManyToOne
    @JoinColumn(name = "id_usuario_tecnico", nullable = false)
    private Usuario usuarioTecnico;

    public DespachoMedicamento() {
    }

    public Integer getIdDespachoMedicamento() {
        return idDespachoMedicamento;
    }

    public void setIdDespachoMedicamento(Integer idDespachoMedicamento) {
        this.idDespachoMedicamento = idDespachoMedicamento;
    }

    public RecetaMedica getRecetaMedica() {
        return recetaMedica;
    }

    public void setRecetaMedica(RecetaMedica recetaMedica) {
        this.recetaMedica = recetaMedica;
    }

    public LocalDateTime getFechaDespacho() {
        return fechaDespacho;
    }

    public void setFechaDespacho(LocalDateTime fechaDespacho) {
        this.fechaDespacho = fechaDespacho;
    }

    public String getEstadoDespacho() {
        return estadoDespacho;
    }

    public void setEstadoDespacho(String estadoDespacho) {
        this.estadoDespacho = estadoDespacho;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public Usuario getUsuarioTecnico() {
        return usuarioTecnico;
    }

    public void setUsuarioTecnico(Usuario usuarioTecnico) {
        this.usuarioTecnico = usuarioTecnico;
    }
}
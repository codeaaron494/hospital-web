package com.hospital.web.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "conteo_fisico")
public class ConteoFisico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_conteo_fisico")
    private Integer idConteoFisico;

    @Column(name = "fecha_conteo", nullable = false)
    private LocalDateTime fechaConteo;

    @Column(name = "estado_conteo", nullable = false, length = 30)
    private String estadoConteo;

    @Column(name = "observacion", columnDefinition = "TEXT")
    private String observacion;

    @ManyToOne
    @JoinColumn(name = "id_usuario_almacenero", nullable = false)
    private Usuario usuarioAlmacenero;

    public ConteoFisico() {
    }

    public Integer getIdConteoFisico() {
        return idConteoFisico;
    }

    public void setIdConteoFisico(Integer idConteoFisico) {
        this.idConteoFisico = idConteoFisico;
    }

    public LocalDateTime getFechaConteo() {
        return fechaConteo;
    }

    public void setFechaConteo(LocalDateTime fechaConteo) {
        this.fechaConteo = fechaConteo;
    }

    public String getEstadoConteo() {
        return estadoConteo;
    }

    public void setEstadoConteo(String estadoConteo) {
        this.estadoConteo = estadoConteo;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public Usuario getUsuarioAlmacenero() {
        return usuarioAlmacenero;
    }

    public void setUsuarioAlmacenero(Usuario usuarioAlmacenero) {
        this.usuarioAlmacenero = usuarioAlmacenero;
    }
}
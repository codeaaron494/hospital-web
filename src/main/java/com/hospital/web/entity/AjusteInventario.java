package com.hospital.web.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ajuste_inventario")
public class AjusteInventario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ajuste_inventario")
    private Integer idAjusteInventario;

    @ManyToOne
    @JoinColumn(name = "id_detalle_conteo", nullable = false)
    private DetalleConteoFisico detalleConteo;

    @ManyToOne
    @JoinColumn(name = "id_kardex", nullable = false)
    private Kardex kardex;

    @Column(name = "cantidad_ajuste", nullable = false)
    private Integer cantidadAjuste;

    @Column(name = "tipo_ajuste", nullable = false, length = 20)
    private String tipoAjuste;

    @Column(name = "motivo", nullable = false, length = 255)
    private String motivo;

    @Column(name = "fecha_ajuste", nullable = false)
    private LocalDateTime fechaAjuste;

    @ManyToOne
    @JoinColumn(name = "id_usuario_almacenero", nullable = false)
    private Usuario usuarioAlmacenero;

    public AjusteInventario() {
    }

    public Integer getIdAjusteInventario() {
        return idAjusteInventario;
    }

    public void setIdAjusteInventario(Integer idAjusteInventario) {
        this.idAjusteInventario = idAjusteInventario;
    }

    public DetalleConteoFisico getDetalleConteo() {
        return detalleConteo;
    }

    public void setDetalleConteo(DetalleConteoFisico detalleConteo) {
        this.detalleConteo = detalleConteo;
    }

    public Kardex getKardex() {
        return kardex;
    }

    public void setKardex(Kardex kardex) {
        this.kardex = kardex;
    }

    public Integer getCantidadAjuste() {
        return cantidadAjuste;
    }

    public void setCantidadAjuste(Integer cantidadAjuste) {
        this.cantidadAjuste = cantidadAjuste;
    }

    public String getTipoAjuste() {
        return tipoAjuste;
    }

    public void setTipoAjuste(String tipoAjuste) {
        this.tipoAjuste = tipoAjuste;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public LocalDateTime getFechaAjuste() {
        return fechaAjuste;
    }

    public void setFechaAjuste(LocalDateTime fechaAjuste) {
        this.fechaAjuste = fechaAjuste;
    }

    public Usuario getUsuarioAlmacenero() {
        return usuarioAlmacenero;
    }

    public void setUsuarioAlmacenero(Usuario usuarioAlmacenero) {
        this.usuarioAlmacenero = usuarioAlmacenero;
    }
}
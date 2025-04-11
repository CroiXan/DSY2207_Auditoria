package com.grupo8.audit.models;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;

@Entity
@Table(name = "AUD_USUARIOS")
public class AudUsuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_AUDITORIA")
    private Long idAuditoria;

    @Column(name = "ID_USUARIO_AFECTADO")
    private Integer idUsuarioAfectado;

    @Column(name = "ID_USUARIO_EJECUTOR")
    private Integer idUsuarioEjecutor;

    @Column(name = "ACCION")
    private String accion;

    @Lob
    @Column(name = "DETALLE_CAMBIOS")
    private String detalleCambios;

    @Column(name = "FECHA_CAMBIO")
    private LocalDateTime fechaCambio;

    @Column(name = "IP_ORIGEN")
    private String ipOrigen;

    public Long getIdAuditoria() {
        return idAuditoria;
    }

    public void setIdAuditoria(Long idAuditoria) {
        this.idAuditoria = idAuditoria;
    }

    public Integer getIdUsuarioAfectado() {
        return idUsuarioAfectado;
    }

    public void setIdUsuarioAfectado(Integer idUsuarioAfectado) {
        this.idUsuarioAfectado = idUsuarioAfectado;
    }

    public Integer getIdUsuarioEjecutor() {
        return idUsuarioEjecutor;
    }

    public void setIdUsuarioEjecutor(Integer idUsuarioEjecutor) {
        this.idUsuarioEjecutor = idUsuarioEjecutor;
    }

    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }

    public String getDetalleCambios() {
        return detalleCambios;
    }

    public void setDetalleCambios(String detalleCambios) {
        this.detalleCambios = detalleCambios;
    }

    public LocalDateTime getFechaCambio() {
        return fechaCambio;
    }

    public void setFechaCambio(LocalDateTime fechaCambio) {
        this.fechaCambio = fechaCambio;
    }

    public String getIpOrigen() {
        return ipOrigen;
    }

    public void setIpOrigen(String ipOrigen) {
        this.ipOrigen = ipOrigen;
    }

}

package com.grupo8.audit.models;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "AUD_USUARIOS_ROLES")
public class AudUsuarioRol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_AUDITORIA")
    private Long idAuditoria;

    @Column(name = "ID_USUARIO")
    private Integer idUsuario;

    @Column(name = "ID_ROL")
    private Integer idRol;

    @Column(name = "ID_USUARIO_EJECUTOR")
    private Integer idUsuarioEjecutor;

    @Column(name = "ACCION")
    private String accion;

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

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Integer getIdRol() {
        return idRol;
    }

    public void setIdRol(Integer idRol) {
        this.idRol = idRol;
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

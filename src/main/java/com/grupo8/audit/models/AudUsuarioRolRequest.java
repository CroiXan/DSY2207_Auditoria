package com.grupo8.audit.models;

public class AudUsuarioRolRequest {

    private int idUsuario;
    private int idRol;
    private int idUsuarioEjecutor;
    private String accion;
    private String ipOrigen;

    public int getIdUsuario() {
        return idUsuario;
    }
    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }
    public int getIdRol() {
        return idRol;
    }
    public void setIdRol(int idRol) {
        this.idRol = idRol;
    }
    public int getIdUsuarioEjecutor() {
        return idUsuarioEjecutor;
    }
    public void setIdUsuarioEjecutor(int idUsuarioEjecutor) {
        this.idUsuarioEjecutor = idUsuarioEjecutor;
    }
    public String getAccion() {
        return accion;
    }
    public void setAccion(String accion) {
        this.accion = accion;
    }
    public String getIpOrigen() {
        return ipOrigen;
    }
    public void setIpOrigen(String ipOrigen) {
        this.ipOrigen = ipOrigen;
    }
    
}

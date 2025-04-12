package com.grupo8.audit.models;

public class AudUsuarioRequest {

    private int idUsuarioAfectado;
    private int idUsuarioEjecutor;
    private String accion;
    private String detalleCambios;
    private String ipOrigen;

    public int getIdUsuarioAfectado() {
        return idUsuarioAfectado;
    }
    public void setIdUsuarioAfectado(int idUsuarioAfectado) {
        this.idUsuarioAfectado = idUsuarioAfectado;
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
    public String getDetalleCambios() {
        return detalleCambios;
    }
    public void setDetalleCambios(String detalleCambios) {
        this.detalleCambios = detalleCambios;
    }
    public String getIpOrigen() {
        return ipOrigen;
    }
    public void setIpOrigen(String ipOrigen) {
        this.ipOrigen = ipOrigen;
    }

}

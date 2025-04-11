package com.grupo8.audit.service;

import java.sql.*;
import java.util.*;

import com.grupo8.audit.config.OracleConnectionUtil;

public class AudUsuarioRolJdbcService {
    public List<Map<String, Object>> listar() throws Exception {
        List<Map<String, Object>> resultados = new ArrayList<>();
        try (Connection conn = OracleConnectionUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM AUD_USUARIOS_ROLES");
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Map<String, Object> fila = new HashMap<>();
                fila.put("idAuditoria", rs.getLong("ID_AUDITORIA"));
                fila.put("idUsuario", rs.getInt("ID_USUARIO"));
                fila.put("idRol", rs.getInt("ID_ROL"));
                fila.put("idUsuarioEjecutor", rs.getInt("ID_USUARIO_EJECUTOR"));
                fila.put("accion", rs.getString("ACCION"));
                fila.put("fechaCambio", rs.getTimestamp("FECHA_CAMBIO").toString());
                fila.put("ipOrigen", rs.getString("IP_ORIGEN"));
                resultados.add(fila);
            }
        }
        return resultados;
    }

    public Map<String, Object> insertar(int idUsuario, int idRol, int idEjecutor, String accion, String ip) throws Exception {
        String sql = "INSERT INTO AUD_USUARIOS_ROLES (ID_USUARIO, ID_ROL, ID_USUARIO_EJECUTOR, ACCION, IP_ORIGEN) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = OracleConnectionUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, new String[] {"ID_AUDITORIA"})) {
            stmt.setInt(1, idUsuario);
            stmt.setInt(2, idRol);
            stmt.setInt(3, idEjecutor);
            stmt.setString(4, accion);
            stmt.setString(5, ip);
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                Map<String, Object> creado = new HashMap<>();
                creado.put("idAuditoria", rs.getLong(1));
                return creado;
            } else {
                throw new RuntimeException("No se generó ID");
            }
        }
    }

    public boolean eliminar(long idAuditoria) throws Exception {
        try (Connection conn = OracleConnectionUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM AUD_USUARIOS_ROLES WHERE ID_AUDITORIA = ?")) {
            stmt.setLong(1, idAuditoria);
            return stmt.executeUpdate() > 0;
        }
    }

    public Map<String, Object> buscarPorId(long idAuditoria) throws Exception {
        try (Connection conn = OracleConnectionUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM AUD_USUARIOS_ROLES WHERE ID_AUDITORIA = ?")) {
            stmt.setLong(1, idAuditoria);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Map<String, Object> fila = new HashMap<>();
                fila.put("idAuditoria", rs.getLong("ID_AUDITORIA"));
                fila.put("idUsuario", rs.getInt("ID_USUARIO"));
                fila.put("idRol", rs.getInt("ID_ROL"));
                fila.put("idUsuarioEjecutor", rs.getInt("ID_USUARIO_EJECUTOR"));
                fila.put("accion", rs.getString("ACCION"));
                fila.put("fechaCambio", rs.getTimestamp("FECHA_CAMBIO").toString());
                fila.put("ipOrigen", rs.getString("IP_ORIGEN"));
                return fila;
            }
            return null;
        }
    }
}

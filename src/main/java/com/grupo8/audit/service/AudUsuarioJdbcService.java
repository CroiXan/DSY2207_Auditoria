package com.grupo8.audit.service;

import java.sql.*;
import java.util.*;

import com.grupo8.audit.config.OracleConnectionUtil;

public class AudUsuarioJdbcService {
    public List<Map<String, Object>> listar() throws Exception {
        List<Map<String, Object>> resultados = new ArrayList<>();
        try (Connection conn = OracleConnectionUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM AUD_USUARIOS");
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Map<String, Object> fila = new HashMap<>();
                fila.put("idAuditoria", rs.getLong("ID_AUDITORIA"));
                fila.put("idUsuarioAfectado", rs.getInt("ID_USUARIO_AFECTADO"));
                fila.put("idUsuarioEjecutor", rs.getInt("ID_USUARIO_EJECUTOR"));
                fila.put("accion", rs.getString("ACCION"));
                fila.put("detalleCambios", rs.getString("DETALLE_CAMBIOS"));
                fila.put("fechaCambio", rs.getTimestamp("FECHA_CAMBIO").toString());
                fila.put("ipOrigen", rs.getString("IP_ORIGEN"));
                resultados.add(fila);
            }
        }
        return resultados;
    }

    public Map<String, Object> insertar(int idAfectado, int idEjecutor, String accion, String detalle, String ip) throws Exception {
        String sql = "INSERT INTO AUD_USUARIOS (ID_USUARIO_AFECTADO, ID_USUARIO_EJECUTOR, ACCION, DETALLE_CAMBIOS, IP_ORIGEN) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = OracleConnectionUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, new String[] {"ID_AUDITORIA"})) {
            stmt.setInt(1, idAfectado);
            stmt.setInt(2, idEjecutor);
            stmt.setString(3, accion);
            stmt.setString(4, detalle);
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
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM AUD_USUARIOS WHERE ID_AUDITORIA = ?")) {
            stmt.setLong(1, idAuditoria);
            return stmt.executeUpdate() > 0;
        }
    }

    public Map<String, Object> buscarPorId(long idAuditoria) throws Exception {
        try (Connection conn = OracleConnectionUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM AUD_USUARIOS WHERE ID_AUDITORIA = ?")) {
            stmt.setLong(1, idAuditoria);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Map<String, Object> fila = new HashMap<>();
                fila.put("idAuditoria", rs.getLong("ID_AUDITORIA"));
                fila.put("idUsuarioAfectado", rs.getInt("ID_USUARIO_AFECTADO"));
                fila.put("idUsuarioEjecutor", rs.getInt("ID_USUARIO_EJECUTOR"));
                fila.put("accion", rs.getString("ACCION"));
                fila.put("detalleCambios", rs.getString("DETALLE_CAMBIOS"));
                fila.put("fechaCambio", rs.getTimestamp("FECHA_CAMBIO").toString());
                fila.put("ipOrigen", rs.getString("IP_ORIGEN"));
                return fila;
            }
            return null;
        }
    }

    public List<Map<String, Object>> buscarPorFiltro(Integer idUsuario, String accion) throws Exception {
        StringBuilder sql = new StringBuilder("SELECT * FROM AUD_USUARIOS WHERE 1=1");
        List<Object> params = new ArrayList<>();
    
        if (idUsuario != null) {
            sql.append(" AND ID_USUARIO_AFECTADO = ?");
            params.add(idUsuario);
        }
    
        if (accion != null && !accion.isEmpty()) {
            sql.append(" AND ACCION = ?");
            params.add(accion);
        }
    
        try (Connection conn = OracleConnectionUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
    
            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }
    
            ResultSet rs = stmt.executeQuery();
            List<Map<String, Object>> resultados = new ArrayList<>();
    
            while (rs.next()) {
                Map<String, Object> fila = new HashMap<>();
                fila.put("idAuditoria", rs.getLong("ID_AUDITORIA"));
                fila.put("idUsuarioAfectado", rs.getInt("ID_USUARIO_AFECTADO"));
                fila.put("accion", rs.getString("ACCION"));
                fila.put("detalleCambios", rs.getString("DETALLE_CAMBIOS"));
                fila.put("fechaCambio", rs.getTimestamp("FECHA_CAMBIO").toString());
                fila.put("ipOrigen", rs.getString("IP_ORIGEN"));
                resultados.add(fila);
            }
    
            return resultados;
        }
    }
    
}

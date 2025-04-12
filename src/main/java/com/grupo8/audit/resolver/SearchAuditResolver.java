package com.grupo8.audit.resolver;

import java.util.HashMap;
import java.util.Map;

import com.grupo8.audit.service.AudRolJdbcService;
import com.grupo8.audit.service.AudUsuarioJdbcService;
import com.grupo8.audit.service.AudUsuarioRolJdbcService;

public class SearchAuditResolver {

    private final AudUsuarioJdbcService usuarioService = new AudUsuarioJdbcService();
    private final AudRolJdbcService rolService = new AudRolJdbcService();
    private final AudUsuarioRolJdbcService usuarioRolService = new AudUsuarioRolJdbcService();

    public Map<String, Object> buscar(String tabla, Integer idUsuario, String accion) throws Exception {
        Map<String, Object> resultado = new HashMap<>();

        if (tabla == null || tabla.equalsIgnoreCase("AUD_USUARIOS")) {
            resultado.put("audUsuarios", usuarioService.buscarPorFiltro(idUsuario, accion));
        }
        if (tabla == null || tabla.equalsIgnoreCase("AUD_ROLES")) {
            resultado.put("audRoles", rolService.buscarPorFiltro(idUsuario, accion));
        }
        if (tabla == null || tabla.equalsIgnoreCase("AUD_USUARIOS_ROLES")) {
            resultado.put("audUsuariosRoles", usuarioRolService.buscarPorFiltro(idUsuario, accion));
        }

        return resultado;
    }
    
}

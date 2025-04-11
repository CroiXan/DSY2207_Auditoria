package com.grupo8.audit.config;

import com.grupo8.audit.service.AudRolJdbcService;
import com.grupo8.audit.service.AudUsuarioJdbcService;
import com.grupo8.audit.service.AudUsuarioRolJdbcService;

import graphql.schema.idl.RuntimeWiring;

public class GraphQLWiring {

    public static RuntimeWiring build() {
        AudUsuarioJdbcService usuarioService = new AudUsuarioJdbcService();
        AudRolJdbcService rolService = new AudRolJdbcService();
        AudUsuarioRolJdbcService usuarioRolService = new AudUsuarioRolJdbcService();

        return RuntimeWiring.newRuntimeWiring()
            .type("Query", builder -> builder
                .dataFetcher("listarAudUsuarios", env -> usuarioService.listar())
                .dataFetcher("listarAudRoles", env -> rolService.listar())
                .dataFetcher("listarAudUsuariosRoles", env -> usuarioRolService.listar())

                .dataFetcher("buscarAudUsuarioPorId", env -> {
                    Long id = Long.valueOf(env.getArgument("idAuditoria"));
                    return usuarioService.buscarPorId(id);
                })
                .dataFetcher("buscarAudRolPorId", env -> {
                    Long id = Long.valueOf(env.getArgument("idAuditoria"));
                    return rolService.buscarPorId(id);
                })
                .dataFetcher("buscarAudUsuarioRolPorId", env -> {
                    Long id = Long.valueOf(env.getArgument("idAuditoria"));
                    return usuarioRolService.buscarPorId(id);
                })
            )
            .type("Mutation", builder -> builder
                .dataFetcher("insertarAudUsuario", env -> usuarioService.insertar(
                    env.getArgument("idUsuarioAfectado"),
                    env.getArgument("idUsuarioEjecutor"),
                    env.getArgument("accion"),
                    env.getArgument("detalleCambios"),
                    env.getArgument("ipOrigen")
                ))
                .dataFetcher("insertarAudRol", env -> rolService.insertar(
                    env.getArgument("idRolAfectado"),
                    env.getArgument("idUsuarioEjecutor"),
                    env.getArgument("accion"),
                    env.getArgument("detalleCambios"),
                    env.getArgument("ipOrigen")
                ))
                .dataFetcher("insertarAudUsuarioRol", env -> usuarioRolService.insertar(
                    env.getArgument("idUsuario"),
                    env.getArgument("idRol"),
                    env.getArgument("idUsuarioEjecutor"),
                    env.getArgument("accion"),
                    env.getArgument("ipOrigen")
                ))
                .dataFetcher("eliminarAudUsuario", env -> {
                    Long id = Long.valueOf(env.getArgument("idAuditoria"));
                    return usuarioService.eliminar(id);
                })
                .dataFetcher("eliminarAudRol", env -> {
                    Long id = Long.valueOf(env.getArgument("idAuditoria"));
                    return rolService.eliminar(id);
                })
                .dataFetcher("eliminarAudUsuarioRol", env -> {
                    Long id = Long.valueOf(env.getArgument("idAuditoria"));
                    return usuarioRolService.eliminar(id);
                })
            )
            .build();
    }
    
}

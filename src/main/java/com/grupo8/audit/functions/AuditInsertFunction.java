package com.grupo8.audit.functions;

import java.util.Optional;

import com.grupo8.audit.models.AudRolRequest;
import com.grupo8.audit.models.AudUsuarioRequest;
import com.grupo8.audit.models.AudUsuarioRolRequest;
import com.grupo8.audit.service.AudRolJdbcService;
import com.grupo8.audit.service.AudUsuarioJdbcService;
import com.grupo8.audit.service.AudUsuarioRolJdbcService;
import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpMethod;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.HttpStatus;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;

public class AuditInsertFunction {

    private final AudUsuarioJdbcService usuarioService = new AudUsuarioJdbcService();
    private final AudRolJdbcService rolService = new AudRolJdbcService();
    private final AudUsuarioRolJdbcService usuarioRolService = new AudUsuarioRolJdbcService();

    @FunctionName("userAuditInsert")
    public HttpResponseMessage UserAuditInsert(
        @HttpTrigger(name = "req", methods = {HttpMethod.POST}, authLevel = AuthorizationLevel.FUNCTION)
        HttpRequestMessage<Optional<AudUsuarioRequest>> request,
        final ExecutionContext context) {

        AudUsuarioRequest audUser = request.getBody().orElse(null);
        if (audUser == null) {
            return request.createResponseBuilder(HttpStatus.BAD_REQUEST).body("Request vacío").build();
        }

        try {
            usuarioService.insertar(
                audUser.getIdUsuarioAfectado(),
                audUser.getIdUsuarioEjecutor(),
                audUser.getAccion(),
                audUser.getDetalleCambios(),
                audUser.getIpOrigen());
            return request.createResponseBuilder(HttpStatus.CREATED).body("Registro creado").build();
        } catch (Exception e) {
            context.getLogger().severe(e.getMessage());
            return request.createResponseBuilder(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al insertar").build();
        }
    }

    @FunctionName("rolAuditInsert")
    public HttpResponseMessage RolAuditInsert(
        @HttpTrigger(name = "req", methods = {HttpMethod.POST}, authLevel = AuthorizationLevel.FUNCTION)
        HttpRequestMessage<Optional<AudRolRequest>> request,
        final ExecutionContext context) {

        AudRolRequest audRol = request.getBody().orElse(null);
        if (audRol == null) {
            return request.createResponseBuilder(HttpStatus.BAD_REQUEST).body("Request vacío").build();
        }

        try {
            rolService.insertar(
                audRol.getIdRolAfectado(),
                audRol.getIdUsuarioEjecutor(),
                audRol.getAccion(),
                audRol.getDetalleCambios(),
                audRol.getIpOrigen());
            return request.createResponseBuilder(HttpStatus.CREATED).body("Registro creado").build();
        } catch (Exception e) {
            context.getLogger().severe(e.getMessage());
            return request.createResponseBuilder(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al insertar").build();
        }
    }

    @FunctionName("userRolAuditInsert")
    public HttpResponseMessage UserRolAuditInsert(
        @HttpTrigger(name = "req", methods = {HttpMethod.POST}, authLevel = AuthorizationLevel.FUNCTION)
        HttpRequestMessage<Optional<AudUsuarioRolRequest>> request,
        final ExecutionContext context) {

        AudUsuarioRolRequest audUserRol = request.getBody().orElse(null);
        if (audUserRol == null) {
            return request.createResponseBuilder(HttpStatus.BAD_REQUEST).body("Request vacío").build();
        }

        try {
            usuarioRolService.insertar(
                audUserRol.getIdUsuario(),
                audUserRol.getIdRol(),
                audUserRol.getIdUsuarioEjecutor(),
                audUserRol.getAccion(),
                audUserRol.getIpOrigen());
            return request.createResponseBuilder(HttpStatus.CREATED).body("Registro creado").build();
        } catch (Exception e) {
            context.getLogger().severe(e.getMessage());
            return request.createResponseBuilder(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al insertar").build();
        }
    }
}

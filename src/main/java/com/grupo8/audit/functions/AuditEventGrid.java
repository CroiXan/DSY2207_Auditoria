package com.grupo8.audit.functions;

import java.util.Optional;

import com.azure.core.credential.AzureKeyCredential;
import com.azure.core.util.BinaryData;
import com.azure.messaging.eventgrid.EventGridEvent;
import com.azure.messaging.eventgrid.EventGridPublisherClient;
import com.azure.messaging.eventgrid.EventGridPublisherClientBuilder;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.grupo8.audit.models.AudRolRequest;
import com.grupo8.audit.models.AudUsuarioRequest;
import com.grupo8.audit.models.AudUsuarioRolRequest;
import com.grupo8.audit.models.EventGridObject;
import com.grupo8.audit.service.AudRolJdbcService;
import com.grupo8.audit.service.AudUsuarioJdbcService;
import com.grupo8.audit.service.AudUsuarioRolJdbcService;
import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpMethod;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.HttpStatus;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.EventGridTrigger;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;

public class AuditEventGrid {

    private final AudUsuarioJdbcService usuarioService = new AudUsuarioJdbcService();
    private final AudRolJdbcService rolService = new AudRolJdbcService();
    private final AudUsuarioRolJdbcService usuarioRolService = new AudUsuarioRolJdbcService();

    private final String eventGridEndpoint = "https://dsy2007grupo8eventgrid.eastus2-1.eventgrid.azure.net/api/events";
    private final String eventGridKey = "5FfNuMNJI1WEYUmVYOr16GgbXu8ezyYaXgHtJrDovp1c1K1mLfCpJQQJ99BDACHYHv6XJ3w3AAABAZEGje4u";

    @FunctionName("userAuditTrigger")
    public HttpResponseMessage UserAuditTrigger(
            @HttpTrigger(name = "req", methods = {
                    HttpMethod.POST }, authLevel = AuthorizationLevel.FUNCTION) HttpRequestMessage<Optional<AudUsuarioRequest>> request,
            final ExecutionContext context) {

        if (request.getBody().get().getAccion().isEmpty()) {
            return request.createResponseBuilder(HttpStatus.BAD_REQUEST).body("Accion de request vacío").build();
        }

        try {
            EventGridPublisherClient<EventGridEvent> client = new EventGridPublisherClientBuilder()
                    .endpoint(eventGridEndpoint)
                    .credential(new AzureKeyCredential(eventGridKey))
                    .buildEventGridEventPublisherClient();

            EventGridEvent event = new EventGridEvent("/UserAudit/save",
                    "User.auditsave", BinaryData.fromObject(request.getBody().get()),
                    "1.0");

            client.sendEvent(event);

            return request.createResponseBuilder(HttpStatus.OK).body("Evento Generado").build();
        } catch (Exception e) {
            context.getLogger().severe(e.getMessage());
            return request.createResponseBuilder(HttpStatus.INTERNAL_SERVER_ERROR).body("Error generar evento").build();
        }
    }

    @FunctionName("roleAuditTrigger")
    public HttpResponseMessage RoleAuditTrigger(
            @HttpTrigger(name = "req", methods = {
                    HttpMethod.POST }, authLevel = AuthorizationLevel.FUNCTION) HttpRequestMessage<Optional<AudRolRequest>> request,
            final ExecutionContext context) {

        if (request.getBody().get().getAccion().isEmpty()) {
            return request.createResponseBuilder(HttpStatus.BAD_REQUEST).body("Accion de request vacío").build();
        }

        try {
            EventGridPublisherClient<EventGridEvent> client = new EventGridPublisherClientBuilder()
                    .endpoint(eventGridEndpoint)
                    .credential(new AzureKeyCredential(eventGridKey))
                    .buildEventGridEventPublisherClient();

            EventGridEvent event = new EventGridEvent("/RoleAudit/save",
                    "Role.auditsave", BinaryData.fromObject(request.getBody().get()),
                    "1.0");

            client.sendEvent(event);

            return request.createResponseBuilder(HttpStatus.OK).body("Evento Generado").build();
        } catch (Exception e) {
            context.getLogger().severe(e.getMessage());
            return request.createResponseBuilder(HttpStatus.INTERNAL_SERVER_ERROR).body("Error generar evento").build();
        }
    }

    @FunctionName("userRoleAuditTrigger")
    public HttpResponseMessage UserRoleAuditTrigger(
            @HttpTrigger(name = "req", methods = {
                    HttpMethod.POST }, authLevel = AuthorizationLevel.FUNCTION) HttpRequestMessage<Optional<AudUsuarioRolRequest>> request,
            final ExecutionContext context) {

        if (request.getBody().get().getAccion().isEmpty()) {
            return request.createResponseBuilder(HttpStatus.BAD_REQUEST).body("Accion de request vacío").build();
        }

        try {
            EventGridPublisherClient<EventGridEvent> client = new EventGridPublisherClientBuilder()
                    .endpoint(eventGridEndpoint)
                    .credential(new AzureKeyCredential(eventGridKey))
                    .buildEventGridEventPublisherClient();

            EventGridEvent event = new EventGridEvent("/UserRoleAudit/save",
                    "UserRole.auditsave", BinaryData.fromObject(request.getBody().get()),
                    "1.0");

            client.sendEvent(event);

            return request.createResponseBuilder(HttpStatus.OK).body("Evento Generado").build();
        } catch (Exception e) {
            context.getLogger().severe(e.getMessage());
            return request.createResponseBuilder(HttpStatus.INTERNAL_SERVER_ERROR).body("Error generar evento").build();
        }
    }

    @FunctionName("userAuditSaveEvent")
    public void runAudUserEvent(
            @EventGridTrigger(name = "userAuditEvent") String content,
            final ExecutionContext context) {

        if (content == null || content.isEmpty()) {
            return;
        }

        context.getLogger().info("Evento recibido: " + content);

        Gson gson = new Gson();

        try {

            EventGridObject event = gson.fromJson(content, EventGridObject.class);

            JsonElement dataElement = gson.toJsonTree(event.getData());
            AudUsuarioRequest data = gson.fromJson(dataElement, AudUsuarioRequest.class);

            usuarioService.insertar(
                    data.getIdUsuarioAfectado(),
                    data.getIdUsuarioEjecutor(),
                    data.getAccion(),
                    data.getDetalleCambios(),
                    data.getIpOrigen());

        } catch (Exception e) {
            context.getLogger().severe(e.getMessage());
        }
    }

    @FunctionName("roleAuditSaveEvent")
    public void runAudRoleEvent(
            @EventGridTrigger(name = "roleAuditEvent") String content,
            final ExecutionContext context) {

        if (content == null || content.isEmpty()) {
            return;
        }

        Gson gson = new Gson();

        try {

            EventGridObject event = gson.fromJson(content, EventGridObject.class);

            JsonElement dataElement = gson.toJsonTree(event.getData());
            AudRolRequest data = gson.fromJson(dataElement, AudRolRequest.class);

            rolService.insertar(
                    data.getIdRolAfectado(),
                    data.getIdUsuarioEjecutor(),
                    data.getAccion(),
                    data.getDetalleCambios(),
                    data.getIpOrigen());

        } catch (Exception e) {
            context.getLogger().severe(e.getMessage());
        }
    }

    @FunctionName("userRoleAuditSaveEvent")
    public void runAudUserRoleEvent(
            @EventGridTrigger(name = "userRoleAuditEvent") String content,
            final ExecutionContext context) {

        if (content == null || content.isEmpty()) {
            return;
        }

        Gson gson = new Gson();

        try {

            EventGridObject event = gson.fromJson(content, EventGridObject.class);

            JsonElement dataElement = gson.toJsonTree(event.getData());
            AudUsuarioRolRequest data = gson.fromJson(dataElement, AudUsuarioRolRequest.class);

            usuarioRolService.insertar(
                    data.getIdUsuario(),
                    data.getIdRol(),
                    data.getIdUsuarioEjecutor(),
                    data.getAccion(),
                    data.getIpOrigen());

        } catch (Exception e) {
            context.getLogger().severe(e.getMessage());
        }
    }

}

package com.grupo8.audit.functions;

import java.io.InputStreamReader;
import java.util.Map;
import java.util.Optional;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.grupo8.audit.resolver.SearchAuditResolver;
import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpMethod;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.HttpStatus;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;

import graphql.ExecutionInput;
import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;

public class SearchAuditFunction {

    private final GraphQL graphQL;

    public SearchAuditFunction() {
        TypeDefinitionRegistry typeRegistry = new TypeDefinitionRegistry();
        SchemaParser parser = new SchemaParser();
        try (InputStreamReader reader = new InputStreamReader(
                getClass().getResourceAsStream("/search.graphqls"))) {
            typeRegistry.merge(parser.parse(reader));
        } catch (Exception e) {
            throw new RuntimeException("No se pudo cargar el schema de búsqueda", e);
        }

        RuntimeWiring wiring = RuntimeWiring.newRuntimeWiring()
                .type("Query", builder -> builder
                        .dataFetcher("buscarAuditoria", env -> {
                            SearchAuditResolver resolver = new SearchAuditResolver();
                            return resolver.buscar(
                                    env.getArgument("tabla"),
                                    env.getArgument("idUsuario"),
                                    env.getArgument("accion")
                            );
                        }))
                .build();

        GraphQLSchema schema = new SchemaGenerator().makeExecutableSchema(typeRegistry, wiring);
        graphQL = GraphQL.newGraphQL(schema).build();
    }

    @FunctionName("auditsearch")
    public HttpResponseMessage run(
            @HttpTrigger(name = "req", methods = {HttpMethod.POST}, authLevel = AuthorizationLevel.ANONYMOUS)
            HttpRequestMessage<Optional<String>> request,
            ExecutionContext context
    ) {
        context.getLogger().info("Ejecutando función GraphQL de búsqueda...");

        try {
            String body = request.getBody().orElse("");
            ObjectMapper mapper = new ObjectMapper();
            JsonNode json = mapper.readTree(body);

            ExecutionInput input = ExecutionInput.newExecutionInput()
                    .query(json.get("query").asText())
                    .variables(json.has("variables") && !json.get("variables").isNull()
                            ? mapper.convertValue(json.get("variables"), Map.class)
                            : Map.of())
                    .build();

            ExecutionResult result = graphQL.execute(input);

            return request.createResponseBuilder(HttpStatus.OK)
                    .header("Content-Type", "application/json")
                    .body(mapper.writeValueAsString(result.toSpecification()))
                    .build();

        } catch (Exception e) {
            context.getLogger().severe("Error ejecutando GraphQL búsqueda: " + e.getMessage());
            return request.createResponseBuilder(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }

}

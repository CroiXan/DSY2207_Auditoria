package com.grupo8.audit.functions;

import java.io.InputStreamReader;
import java.util.Map;
import java.util.Optional;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.grupo8.audit.config.GraphQLWiring;
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

public class AudUsuarioFunction {
    private final GraphQL graphQL;

    public AudUsuarioFunction() {
        TypeDefinitionRegistry typeRegistry = new SchemaParser()
            .parse(new InputStreamReader(getClass().getResourceAsStream("/schema.graphqls")));

        RuntimeWiring wiring = GraphQLWiring.build();

        GraphQLSchema schema = new SchemaGenerator().makeExecutableSchema(typeRegistry, wiring);
        graphQL = GraphQL.newGraphQL(schema).build();
    }

    @FunctionName("graphql")
    public HttpResponseMessage run(
        @HttpTrigger(name = "req", methods = {HttpMethod.POST}, authLevel = AuthorizationLevel.ANONYMOUS)
        HttpRequestMessage<Optional<String>> request,
        ExecutionContext context
    ) {
        context.getLogger().info("🔍 Ejecutando función GraphQL...");

        try {
            String body = request.getBody().orElse("");
            ObjectMapper mapper = new ObjectMapper();
            JsonNode json = mapper.readTree(body);

            String query = json.has("query") ? json.get("query").asText() : null;
            Map<String, Object> variables = json.has("variables") && !json.get("variables").isNull()
                ? mapper.convertValue(json.get("variables"), Map.class)
                : Map.of();

            if (query == null || query.isBlank()) {
                return request.createResponseBuilder(HttpStatus.BAD_REQUEST)
                    .body("{\"error\": \"No query was provided\"}")
                    .build();
            }

            ExecutionInput executionInput = ExecutionInput.newExecutionInput()
                .query(query)
                .variables(variables)
                .build();

            ExecutionResult result = graphQL.execute(executionInput);

            return request.createResponseBuilder(HttpStatus.OK)
                .header("Content-Type", "application/json")
                .body(mapper.writeValueAsString(result.toSpecification()))
                .build();

        } catch (Exception e) {
            context.getLogger().severe("Error al ejecutar GraphQL: " + e.getMessage());
            return request.createResponseBuilder(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("{\"error\": \"" + e.getMessage() + "\"}")
                .build();
        }
    }

}

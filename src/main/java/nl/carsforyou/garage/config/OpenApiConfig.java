package nl.carsforyou.garage.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.OAuthFlow;
import io.swagger.v3.oas.annotations.security.OAuthFlows;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info = @Info(title = "Garage API", version = "v1"),
    security = @SecurityRequirement(name = "keycloak")
)
@SecurityScheme(
    name = "keycloak",
    type = SecuritySchemeType.OAUTH2,
    //Below is required to inform OpenAPI about the OAuth2 flow, Swagger shows “Authorize” and knows where to login
    //instead of manually copy/paste tokens, not user-friendly
    flows = @OAuthFlows(
        authorizationCode = @OAuthFlow(
            authorizationUrl = "http://localhost:9090/realms/Garage/protocol/openid-connect/auth",
            tokenUrl = "http://localhost:9090/realms/Garage/protocol/openid-connect/token"
        )
    )
)
public class OpenApiConfig { }
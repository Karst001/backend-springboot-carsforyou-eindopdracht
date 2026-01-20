package nl.carsforyou.garage.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Configuration
public class SecurityConfig {

    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    private String issuer;

    @Value("${spring.security.oauth2.resourceserver.jwt.audiences}")
    private String audience;

    @Value("${client-id}")
    private String clientId;

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        // .anyRequest().permitAll()  //bypasses the security

        // .authorizeHttpRequests(authorize -> authorize
        //      .anyRequest().authenticated()
        // )

        //Start KeyCloak in PowerShell: E:\Novi\EindopdrachtBackend\keycloak-26.4.7\bin\kc.bat start-dev --http-port 9090

        return http
                .httpBasic(hp -> hp.disable())
                .csrf(csrf->csrf.disable())
                .cors(cors->{})
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt
                                .jwtAuthenticationConverter(jwtAuthenticationConverter())
                                .decoder(jwtDecoder())
                        ))
                //below is the centralized authorization for all controllers
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()  //this prevents Swagger UI to be blocked by .anyRequest().denyAll()

                        //Appointments
                        //'user' + 'admin' can do everything
                        .requestMatchers("/appointments/**").hasAnyAuthority("role_client_admin", "role_client_user")


                        //Customers more fine tuned rules
                        //GET: for 'admin' + 'user'
                        .requestMatchers(HttpMethod.GET, "/customers/**").hasAnyAuthority("role_client_admin", "role_client_user")
                        //UPDATE (PUT and PATCH): 'admin' + 'user'
                        .requestMatchers(HttpMethod.PUT, "/customers/**").hasAnyAuthority("role_client_admin", "role_client_user")
                        .requestMatchers(HttpMethod.PATCH, "/customers/**").hasAnyAuthority("role_client_admin", "role_client_user")
                        //CREATE + DELETE: 'admin' only
                        .requestMatchers(HttpMethod.POST, "/customers/**").hasAuthority("role_client_admin")
                        .requestMatchers(HttpMethod.DELETE, "/customers/**").hasAuthority("role_client_admin")


                        //CustomerUploads
                        //'user' + 'admin' can do everything
                        .requestMatchers("/customeruploads/**").hasAnyAuthority("role_client_admin", "role_client_user")


                        //CustomerVisitReport
                        //'user' + 'admin' can do everything
                        .requestMatchers("/reports/customers/**").hasAnyAuthority("role_client_admin", "role_client_user")


                        //Parts
                        //GET: 'admin' + 'user'
                        .requestMatchers(HttpMethod.GET, "/parts/**").hasAnyAuthority("role_client_admin", "role_client_user")

                        //CREATE, UPDATE, DELETE: 'admin' only
                        .requestMatchers("/parts/**").hasAuthority("role_client_admin")


                        //ServiceOrders
                        //'admin' only, all CRUD operations
                        .requestMatchers("/serviceorders/**").hasAuthority("role_client_admin")


                        //ServiceOrderParts
                        //'admin' only, all CRUD operations
                        .requestMatchers("/serviceorderparts/**").hasAuthority("role_client_admin")


                        //Users
                        //GET, CREATE: 'admin' + 'user'
                        .requestMatchers(HttpMethod.GET, "/users/**").hasAnyAuthority("role_client_admin", "role_client_user")
                        .requestMatchers(HttpMethod.POST, "/users/**").hasAnyAuthority("role_client_admin", "role_client_user")

                        //UPDATE, DELETE: 'admin' only
                        .requestMatchers(HttpMethod.PUT, "/users/**").hasAuthority("role_client_admin")
                        .requestMatchers(HttpMethod.PATCH, "/users/**").hasAuthority("role_client_admin")
                        .requestMatchers(HttpMethod.DELETE, "/users/**").hasAuthority("role_client_admin")


                        //Vehicles
                        //'admin' only, all CRUD operations
                        .requestMatchers("/vehicles/**").hasAuthority("role_client_admin")

                        //all other GET requests require login
                        .requestMatchers(HttpMethod.GET, "/**").authenticated()

                        //lastly, anything else deny it
                        .anyRequest().denyAll()
                )
                .sessionManagement(session-> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }


    public JwtDecoder jwtDecoder(){
        NimbusJwtDecoder jwtDecoder = JwtDecoders.fromOidcIssuerLocation(issuer);

        OAuth2TokenValidator<Jwt> audienceValidator = new JwtAudienceValidator(audience);
        OAuth2TokenValidator<Jwt> withIssuer = JwtValidators.createDefaultWithIssuer(issuer);
        OAuth2TokenValidator<Jwt> withAudience = new DelegatingOAuth2TokenValidator<>(withIssuer, audienceValidator);
        jwtDecoder.setJwtValidator(withAudience);
        return jwtDecoder;
    }


    public JwtAuthenticationConverter jwtAuthenticationConverter() {

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new Converter<>() {
            @Override
            public Collection<GrantedAuthority> convert(Jwt source) {
                Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();
                for (String authority : getAuthorities(source)) {
                    grantedAuthorities.add(new SimpleGrantedAuthority( authority));
                }
                return grantedAuthorities;
            }
            private List<String> getAuthorities(Jwt jwt) {
                // Check Client roles first
                Map<String, Object> resourceAccess = jwt.getClaim("resource_access");
                if (resourceAccess != null) {
                    Object clientObj = resourceAccess.get(clientId);
                    if (clientObj instanceof Map<?, ?> clientMap) {
                        Object rolesObj = clientMap.get("roles");
                        if (rolesObj instanceof List<?> rolesList) {
                            List<String> roles = new ArrayList<>();
                            for (Object r : rolesList) roles.add(String.valueOf(r));
                            return roles;
                        }
                    }
                }

                //Fall-back: Realm roles: realm_access.roles
                Map<String, Object> realmAccess = jwt.getClaim("realm_access");
                if (realmAccess != null) {
                    Object rolesObj = realmAccess.get("roles");
                    if (rolesObj instanceof List<?> rolesList) {
                        List<String> roles = new ArrayList<>();
                        for (Object r : rolesList) roles.add(String.valueOf(r));
                        return roles;
                    }
                }

                return List.of();
            }
        });
        return jwtAuthenticationConverter;
    }
}

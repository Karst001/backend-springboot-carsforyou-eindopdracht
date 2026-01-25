package nl.carsforyou.garage.config;

import jakarta.servlet.DispatcherType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Configuration
@ConditionalOnProperty(name = "app.security.oauth2.enabled", havingValue = "true", matchIfMissing = true)
public class SecurityConfigOauth2 {
    //grabbing settings from KeyCloak
    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    private String issuer;

    @Value("${spring.security.oauth2.resourceserver.jwt.audiences}")
    private String audience;

    @Value("${client-id}")
    private String clientId;

    //setting Cors
    UrlBasedCorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(List.of("*"));
        corsConfiguration.setAllowedMethods(List.of("*"));
        corsConfiguration.setAllowedHeaders(List.of("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {

        return http
                .httpBasic(hp -> hp.disable())                                  //disabled because Basic Auth is not safe anymore
                .csrf(csrf->csrf.disable())                                         //disabled because we work with tokens instead of cookies
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))  //inject cors settings
                .oauth2ResourceServer(oauth2 -> oauth2
                    .jwt(jwt -> jwt
                            .jwtAuthenticationConverter(jwtAuthenticationConverter())
                            .decoder(jwtDecoder())
                    ))

                //below is the centralized authorization for all Controllers
                .authorizeHttpRequests(authorize -> authorize

                    //added ERROR.permitAll() to properly handle a 404 from the Services
                    //scenario without ERROR.permitAll(): delete a appointment
                    //Then try to delete the same appointment again, now in Swagger you see a 403 showing it need higher privileges, this is incorrect
                    //and should have been a 404 coming from the AppointmentService exception error
                    //By setting ERROR.permitAll() Spring Boot’s BasicErrorController handles errors on /error.
                    //Then when an exception occurs during a request, the container dispatches internally to /error to render the error response body.
                    //If security blocks /error, you get a secondary failure in this case 403
                    .dispatcherTypeMatchers(DispatcherType.ERROR).permitAll()
                    .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll() //this prevents Swagger UI to be blocked by .anyRequest().denyAll()


                    //Appointments
                    //'user' + 'admin' can do everything
                    .requestMatchers("/appointments/**").hasAnyRole("ADMIN", "USER")


                    //CustomerReports
                    //'user' + 'admin' can do everything
                    .requestMatchers("/reports/customers/**").hasAnyRole("ADMIN", "USER")


                    //Customers more specific rules
                    //GET: for 'admin' + 'user'
                    .requestMatchers(HttpMethod.GET, "/customers/**").hasAnyRole("ADMIN", "USER")
                    //UPDATE (PUT and PATCH): 'admin' + 'user'
                    .requestMatchers(HttpMethod.PUT, "/customers/**").hasAnyRole("ADMIN", "USER")
                    .requestMatchers(HttpMethod.PATCH, "/customers/**").hasAnyRole("ADMIN", "USER")
                    //CREATE + DELETE: 'admin' only
                    .requestMatchers(HttpMethod.POST, "/customers/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.DELETE, "/customers/**").hasRole("ADMIN")


                    //CustomerUploads
                    //'user' + 'admin' can do everything
                    .requestMatchers("/customeruploads/**").hasAnyRole("ADMIN", "USER")


                    //Parts
                    //GET: 'admin' + 'user'
                    .requestMatchers(HttpMethod.GET, "/parts/**").hasAnyRole("ADMIN", "USER")

                    //CREATE, UPDATE, DELETE: 'admin' only
                    .requestMatchers("/parts/**").hasRole("ADMIN")


                    //ServiceOrderParts
                    //'admin' only, all CRUD operations
                    .requestMatchers("/serviceorderparts/**").hasRole("ADMIN")


                    //ServiceOrders
                    //'admin' only, all CRUD operations
                    .requestMatchers("/serviceorders/**").hasRole("ADMIN")


                    //Users
                    //GET, CREATE: 'admin' + 'user'
                    .requestMatchers(HttpMethod.GET, "/users/**").hasAnyRole("ADMIN")
                    .requestMatchers(HttpMethod.POST, "/users/**").hasAnyRole("ADMIN", "USER")


                    //UPDATE, DELETE: 'admin' only
                    .requestMatchers(HttpMethod.PUT, "/users/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.PATCH, "/users/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.DELETE, "/users/**").hasRole("ADMIN")


                    //Vehicles
                    //'admin' only, all CRUD operations
                    .requestMatchers("/vehicles/**").hasRole("ADMIN")

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

                //for debugging to see in log what role was sent by JWT
                //List<String> roles = getAuthorities(source);
                //System.out.println("JWT roles seen by app: " + roles);

                for (String authority : getAuthorities(source)) {
                    grantedAuthorities.add(new SimpleGrantedAuthority(authority));
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

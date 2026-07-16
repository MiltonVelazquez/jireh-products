package jireh.productos.security;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity 
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors(withDefaults()) 
            .csrf(config -> config.disable())
            .authorizeHttpRequests(auth -> auth
                // .requestMatchers(HttpMethod.GET, "/products/product/**").permitAll()
                // .requestMatchers(HttpMethod.GET, "/products/category/**").permitAll()
                // .requestMatchers(HttpMethod.GET, "/products/subcategory/**").permitAll()
                // .requestMatchers(HttpMethod.GET, "/products/calification/product/**").permitAll()

                // .requestMatchers(HttpMethod.POST, "/products/product/**").hasRole("ADMIN")
                // .requestMatchers(HttpMethod.DELETE, "/products/product/**").hasRole("ADMIN")
                // .requestMatchers(HttpMethod.POST, "/products/category/**").hasRole("ADMIN")
                // .requestMatchers(HttpMethod.POST, "/products/subcategory/**").hasRole("ADMIN")

                // .requestMatchers("/products/wishlist/**").hasRole("USER")
                // .requestMatchers(HttpMethod.POST, "/products/calification/**").hasAnyRole("USER", "ADMIN")

                // .anyRequest().authenticated()
                .anyRequest().permitAll()
            )
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
            );

        return http.build();
    }
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        configuration.setAllowedOrigins(Arrays.asList(
            "https://eddc0f65-a17f-4af9-9774-b722606f3449-00-2zbw4m3gak2ba.worf.replit.dev",
            "https://jireh-frontend-chi.vercel.app/"
        ));
        
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "Accept"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        
        grantedAuthoritiesConverter.setAuthoritiesClaimName("roles"); 
        
        grantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        String secretKey = "dad7150098c718a48c716173593961ca661da5efc48c158f1d1e46b115afe845"; 
    
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), "HmacSHA256");
        return NimbusJwtDecoder.withSecretKey(secretKeySpec).build();
    }
}

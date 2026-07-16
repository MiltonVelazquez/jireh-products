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


@Configuration
@EnableWebSecurity
@EnableMethodSecurity 
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
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
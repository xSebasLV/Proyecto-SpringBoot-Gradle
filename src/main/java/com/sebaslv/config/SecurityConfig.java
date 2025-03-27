package com.sebaslv.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/login").permitAll() // Permite el acceso al endpoint de login
                        .anyRequest().authenticated() // Requiere autenticación para todas las demás solicitudes
                )
                .formLogin(form -> form
                        .permitAll() // Permite acceso al formulario de inicio de sesión
                        .successHandler(successHandler()) // Define el manejador de inicio de sesión exitoso
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.ALWAYS) // Siempre crea una nueva sesión
                        .invalidSessionUrl("/login") // Redirige al login si la sesión es inválida
                        .sessionFixation(sessionFixation -> sessionFixation
                                .migrateSession() // Migra la sesión para protegerla contra fijación de sesión
                        )
                )
                .sessionManagement(session -> session
                        .maximumSessions(1) // Limita a una sesión por usuario
                        .expiredUrl("/login") // Redirige al login si la sesión expira
                        .maxSessionsPreventsLogin(false) // Permite iniciar sesión si otra sesión existe, expulsándola
                        .sessionRegistry(sessionRegistry()) // Configura el registro de sesiones
                );

        return httpSecurity.build();
    }

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl(); // Crea un registro de sesiones para gestionar múltiples sesiones
    }

    @Bean
    public AuthenticationSuccessHandler successHandler() {
        return (request, response, authentication) -> {
            response.sendRedirect("/api/usuarios"); // Redirige al endpoint "/api/usuarios" tras inicio exitoso
        };
    }
}
package com.parque_industrial.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 1. Force CORS to be handled first
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // 2. Disable CSRF
                .csrf(AbstractHttpConfigurer::disable)

                // 3. Configure route protections
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/auth/login").permitAll()
                        .requestMatchers("/auth/register").permitAll()
                        .requestMatchers("/auth/registerAdminParque").hasRole("ADMINISTRADOR_PARQUE") // Login &
                                                                                                      // Register are
                                                                                                      // free
                        .requestMatchers("/auth/registerExtraRepresentanteEmpresa").hasRole("REPRESENTANTE_EMPRESA")
                        // Permite que cualquiera (sin token) vea las publicaciones en la Landing
                        .requestMatchers(HttpMethod.GET, "/api/publicaciones/**").permitAll()

                        // EXPLICITADO: La mensajería requiere autenticación obligatoria (Cualquier ROL)
                        .requestMatchers("/api/mensajes/**").authenticated()

                        // --- ENDPOINTS DE INVENTARIO (BLINDADO) ---
                        // Aseguramos que el ABM completo de inventario responda únicamente a
                        // ADMINISTRADOR_PARQUE
                        .requestMatchers("/api/inventario", "/api/inventario/**").hasRole("ADMINISTRADOR_PARQUE")

                        // --- ENDPOINTS DE CONSUMOS (REFACTORIZADO Y BLINDADO) ---
                        // El reporte global es exclusivo para los administradores del parque industrial
                        .requestMatchers("/api/consumos/reporte-global/**").hasRole("ADMINISTRADOR_PARQUE") //antes tenia ADMIN_PARQUE q no existe, lo cambie a ADMINISTRADOR_PARQUE

                        // Agrupamos la ruta exacta y sus sub-rutas para el rol REPRESENTANTE_EMPRESA.
                        // Esto cubre tanto el POST a '/api/consumos' como el GET a
                        // '/api/consumos/historial'.
                        .requestMatchers("/api/consumos", "/api/consumos/**").hasRole("REPRESENTANTE_EMPRESA")


                        .requestMatchers("/api/lotes/**").hasRole("ADMINISTRADOR_PARQUE")

                        // Cualquier otra acción del sistema requerirá estar autenticado
                        .anyRequest().authenticated())

                // 4. Kill default interactive login prompts
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)

                // 5. ATTACH THE JWT FILTER!
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOrigins(List.of(
                "http://localhost:5173",
                "https://pablogalindoroldan.github.io"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("Authorization", "Cache-Control", "Content-Type"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
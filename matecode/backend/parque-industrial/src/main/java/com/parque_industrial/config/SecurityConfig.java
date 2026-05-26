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
                        .requestMatchers("/auth/registerAdminParque").hasRole("ADMINISTRADOR_SISTEMA")
                        .requestMatchers("/auth/registerExtraRepresentanteEmpresa").hasRole("REPRESENTANTE_EMPRESA")

                        // Permite que cualquiera (sin token) vea las publicaciones en la Landing
                        .requestMatchers(HttpMethod.GET, "/api/publicaciones/**").permitAll()

                        // EXPLICITADO: La mensajería requiere autenticación obligatoria (Cualquier ROL)
                        .requestMatchers("/api/mensajes/**").authenticated()

                        // --- ENDPOINTS DE INVENTARIO ---
                        .requestMatchers("/api/inventario", "/api/inventario/**").hasRole("ADMINISTRADOR_PARQUE")

                        // --- ENDPOINTS DE CONSUMOS ---
                        .requestMatchers("/api/consumos/reporte-global/**").hasRole("ADMINISTRADOR_PARQUE")
                        .requestMatchers(HttpMethod.GET, "/api/consumos/historial/*").hasRole("ADMINISTRADOR_PARQUE")
                        .requestMatchers("/api/consumos", "/api/consumos/**").hasRole("REPRESENTANTE_EMPRESA")

                        // --- ENDPOINTS DE PROYECTOS ---
                        //.requestMatchers("/api/proyectos", "/api/proyectos/**").authenticated()
                        .requestMatchers("/api/proyectos").permitAll()

                        // --- ENDPOINTS DE LOTES ---
                        .requestMatchers("/api/lotes/**").hasRole("ADMINISTRADOR_PARQUE")

                        // --- ENDPOINTS DE EMPRESAS ---
                        .requestMatchers(HttpMethod.GET, "/api/empresas", "/api/empresas/**").permitAll()
                        .requestMatchers("/api/empresas/**").hasRole("ADMINISTRADOR_PARQUE")

                        // --- ENDPOINTS DE PRESUPUESTO Y BALANCES (LEY 5763) ---
                        .requestMatchers("/api/presupuesto", "/api/presupuesto/**").hasRole("ADMINISTRADOR_PARQUE")

                        // Cualquier otra acción del sistema requerirá estar autenticado
                        .anyRequest().authenticated())

                // 4. Kill default interactive login prompts
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)

                // 5. CACHE SURGERY: Prevent Spring from injecting 'no-store' ONLY for the map
                // endpoint
                .headers(headers -> headers
                        .cacheControl(cache -> cache.disable()) // Turn off default global builder
                )

                // 6. ATTACH THE JWT FILTER!
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
        config.setAllowedHeaders(List.of("Authorization", "Cache-Control", "Content-Type", "If-None-Match"));

        // CRITICAL FOR PATH B: This allows Axios on localhost:5173 to physically see
        // the ETag property
        // returned from your localhost:8080 server
        config.setExposedHeaders(List.of("ETag"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
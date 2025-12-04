package com.sitra.sitra.config.cors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // Aplica esta configuración CORS a TODAS las rutas de tu API
                        //.allowedOrigins("*") // PERMITE solicitudes desde tu aplicación Angular
                        // Si tu aplicación Angular se despliega en otro dominio/puerto en producción, lo añadirías aquí:
                        // .allowedOrigins("http://localhost:4200", "https://tudominio.com", "https://otrodm.com")
                        // O para desarrollo, si no te importa la seguridad por un momento, puedes usar:
                        // .allowedOrigins("*") // CUIDADO: Esto permite cualquier origen. ¡NO USAR EN PRODUCCIÓN!
                        .allowedOriginPatterns("*") // esto es para permitir allowedCredentials(true)
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Define los métodos HTTP permitidos
                        .allowedHeaders("*") // Permite todas las cabeceras en la solicitud
                        .allowCredentials(true); // Importante si envías cookies, tokens JWT en cabeceras de autorización
            }
        };
    }
}

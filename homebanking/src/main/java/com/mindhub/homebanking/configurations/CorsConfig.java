package com.mindhub.homebanking.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

//CROSS ORIGIN RESOURSE SHARING
//COMPARTIR RECURSOS DE ORIGEN CRUZADOS
//ES UNA CARACTERISTICA DE SEGURIDAD IMPLEMENTADA EN LOS NAVEGADORES WEB
//PARA RESTRINGIR LAS PAGINAS WEB DE HACER SOLICITUDES
//PERMITE A LOS SERVIDORES ESPECIFICAR QUE ORIGENES TIENEN PERMITIDO ACCEDER
// A SUS RECURSOS A TRAVES DE ENCABEZADOS HTTP

@Configuration
public class CorsConfig {

    //LO PONGO EN EL CONTEXTO DE SPRING Y QUE SEA UNA DE LAS PRIMERAS COSAS QUE SE EJECUTE
    @Bean
    public CorsConfigurationSource corsConfigurationSource () {
        //CREO UNA INSTANCIA DE CORSCOFIGURATION PARA HACERLE MODIFICACIONES
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("https://homebanking-front-ttiy.onrender.com", "http://localhost:5173"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
        configuration.setAllowedHeaders(List.of("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
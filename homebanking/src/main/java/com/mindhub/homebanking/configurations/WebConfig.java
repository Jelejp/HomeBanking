package com.mindhub.homebanking.configurations;

import com.mindhub.homebanking.filters.JwtRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

//PARA QUE SEA UNA CONFIGURACION Y QUE SEA PREVIO
@Configuration
public class WebConfig {

    //INYECTO EL JWTREQUESTFILTER
    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    //INYECTO EL CORSCONFIGURATIONSOURCE
    @Autowired
    private CorsConfigurationSource corsConfigurationSource;

    @Bean
    public SecurityFilterChain securityFilterChain (HttpSecurity httpSecurity) throws Exception {

        httpSecurity //DEL HTTP SECURITY QUE RECIBO COMO PARAMETRO ESTABLEZCO LA CONFIGURACION DE CORS QUE HICE
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                .csrf(AbstractHttpConfigurer :: disable)
                .httpBasic(AbstractHttpConfigurer :: disable)
                .formLogin(AbstractHttpConfigurer :: disable)

                .headers(httpSecurityHeadersConfigurer -> httpSecurityHeadersConfigurer.frameOptions(
                        HeadersConfigurer.FrameOptionsConfig :: disable))

                .authorizeHttpRequests(authorize ->
                        authorize
                                .requestMatchers("/api/auth/login", "/api/auth/register", "/h2-console/**").permitAll()
                                .requestMatchers("/api/clients/", "/api/clients/{id}", "/api/accounts", "/api/accounts/{id}").hasRole("ADMIN")
                                .requestMatchers("/api/auth/current", "/api/auth/test", "/api/clients/current/accounts", "/api/clients/current/cards", "/api/transactions/").hasRole("CLIENT")
                                .anyRequest().authenticated()
                )

                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );
        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
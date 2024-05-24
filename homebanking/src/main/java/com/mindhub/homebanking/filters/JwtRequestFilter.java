package com.mindhub.homebanking.filters;

import com.mindhub.homebanking.servicesSecurity.JwtUtilService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter { // PARA QUE EL FILTRO SE EJECUTE SOLO UNA VEZ POR CADA SOLI HTTP
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired //INYECTO JWTUTILSERVICE PARA CREAR EL TOKEN
    private JwtUtilService jwtUtilService;

    //PUEDE ARROJAR UNA EXCEPCION, QUE PUEDE SER GENERAL O UNA EXCEPCION A LA HORA DE RECIBIRLA O DEVOLVERLA
    @Override
    protected void doFilterInternal (HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            //DE LA PETICION QUE RECIBO POR PARAMETRO OBTENGO EL HEADER
            final String authorizationHeader = request.getHeader( "Authorization");
            String userName = null; //
            String jwt = null; //DECLARO VARIABLES POR EL MOMENTO NULAS

            //SI AUTHORIZATIONHEADER NO ES NULO Y COMIENZA CON BEARER..
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                jwt = authorizationHeader.substring(7); //LE ASIGNO A JWT (BORRANDO EL BEARER ) EL TOKEN PURO
                userName = jwtUtilService.extractUserName(jwt); //LE ASIGNO USANDO EL SERVICIO DE JWTUTILSERVICE PARA EXTRAER EL NOMBRE DEL JWT
            }

            //SI SE EXTRAJO UN NOMBRE DE USUARIO DEL JWT Y SI EL CONTEXTO DE SEGURIDAD NO TIENE UNA AUTENTICACION ESTABLECIDA
            if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userName);
                //SI EL TOKEN NO ESTA EXPIRADO (USO EL METODO QUE DEFINI EN JWTUTILSERVICE)
                if (!jwtUtilService.isTokenExpired(jwt)) {
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities() //PASO LAS CREDENCIALES COMO NULL PORQUE LA AUTENTICACION LA HAGO ATRAVEZ DEL TOKEN
                    );
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    //ESTABLEZCO LA AUTENTICACION DEL USUARIO ACTUAL
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }

        finally {
            filterChain.doFilter(request, response);
        }
    }
}

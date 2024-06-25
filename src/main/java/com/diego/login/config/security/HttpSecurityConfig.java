package com.diego.login.config.security;

import com.diego.login.config.security.filter.JwtAuthenticationFilter;
import com.diego.login.persistence.util.Rol;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class HttpSecurityConfig {


    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private AuthenticationProvider authProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sesionManager -> sesionManager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(authRequest -> {
                    authRequest.requestMatchers("/auth/**").permitAll();
                    authRequest.requestMatchers("/user/**").hasRole(Rol.ADMINISTRADOR.name());
                    authRequest.anyRequest().authenticated();
                })
                .exceptionHandling(mensaje -> mensaje.accessDeniedHandler(accessDeniedHandler()))
                .build();
    }


    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new ManejadorAccesoDenegadoPersonalizado();
    }

}

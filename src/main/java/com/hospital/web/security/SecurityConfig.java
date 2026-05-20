package com.hospital.web.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/login",
                    "/css/**",
                    "/js/**",
                    "/img/**",
                    "/vendor/**"
                ).permitAll()
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .successHandler((request, response, authentication) -> {
                    boolean isAdmin = authentication.getAuthorities().stream()
                            .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

                    boolean isRecepcionista = authentication.getAuthorities().stream()
                            .anyMatch(auth -> auth.getAuthority().equals("ROLE_RECEPCIONISTA"));

                    boolean isEnfermero = authentication.getAuthorities().stream()
                            .anyMatch(auth -> auth.getAuthority().equals("ROLE_ENFERMERO"));

                    boolean isMedico = authentication.getAuthorities().stream()
                            .anyMatch(auth -> auth.getAuthority().equals("ROLE_MEDICO"));

                    boolean isAlmacenero = authentication.getAuthorities().stream()
                            .anyMatch(auth -> auth.getAuthority().equals("ROLE_ALMACENERO"));

                    boolean isQuimico = authentication.getAuthorities().stream()
                            .anyMatch(auth -> auth.getAuthority().equals("ROLE_QUIMICO_FARMACEUTICO"));

                    boolean isCobranza = authentication.getAuthorities().stream()
                            .anyMatch(auth -> auth.getAuthority().equals("ROLE_COBRANZA"));

                    if (isAdmin) {
                        response.sendRedirect("/admin/dashboard");
                    } else if (isRecepcionista) {
                        response.sendRedirect("/dashboard");
                    } else if (isEnfermero) {
                        response.sendRedirect("/enfermeria/dashboard");
                    } else if (isMedico) {
                        response.sendRedirect("/medico/dashboard");
                    } else if (isAlmacenero) {
                        response.sendRedirect("/farmacia/compras/dashboard");
                    } else if (isQuimico) {
                        response.sendRedirect("/farmacia/compras/quimico/dashboard");
                    } else if (isCobranza) {
                        response.sendRedirect("/farmacia/compras/cobranza/dashboard");
                    } else {
                        response.sendRedirect("/login");
                    }
                })
                .failureUrl("/login?error")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            );

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {

        UserDetails admin = User
                .withUsername("admin")
                .password("{noop}admin123")
                .roles("ADMIN")
                .build();

        UserDetails recepcionista = User
                .withUsername("recepcionista")
                .password("{noop}recep123")
                .roles("RECEPCIONISTA")
                .build();

        UserDetails enfermero = User
                .withUsername("enfermero")
                .password("{noop}enfermero123")
                .roles("ENFERMERO")
                .build();

        UserDetails medico = User
                .withUsername("medico")
                .password("{noop}medico123")
                .roles("MEDICO")
                .build();

        UserDetails almacenero = User
                .withUsername("almacenero")
                .password("{noop}almacen123")
                .roles("ALMACENERO")
                .build();

        UserDetails quimico = User
                .withUsername("quimico")
                .password("{noop}quimico123")
                .roles("QUIMICO_FARMACEUTICO")
                .build();

        UserDetails cobranza = User
                .withUsername("cobranza")
                .password("{noop}cobranza123")
                .roles("COBRANZA")
                .build();

        return new InMemoryUserDetailsManager(
        admin,
        recepcionista,
        enfermero,
        medico,
        almacenero,
        quimico,
        cobranza
        );
    }
}
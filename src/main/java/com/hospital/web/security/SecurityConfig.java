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
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth

                // Recursos públicos
                .requestMatchers(
                    "/login",
                    "/css/**",
                    "/js/**",
                    "/img/**",
                    "/vendor/**"
                ).permitAll()

                // Dashboard administrador
                .requestMatchers("/admin/**")
                .hasRole("ADMIN")

                // CUS 1 - Gestión de Citas
                .requestMatchers("/dashboard")
                .hasAnyRole("ADMIN", "RECEPCIONISTA")

                .requestMatchers("/citas/**")
                .hasAnyRole("ADMIN", "RECEPCIONISTA")

                .requestMatchers("/pacientes/**")
                .hasAnyRole("ADMIN", "RECEPCIONISTA")

                // CUS 2 - Gestión de Historia Clínica: Recepción
                .requestMatchers("/fichas/**")
                .hasAnyRole("ADMIN", "RECEPCIONISTA")

                // CUS 2 - Gestión de Historia Clínica: Enfermería
                .requestMatchers("/enfermeria/**")
                .hasAnyRole("ADMIN", "ENFERMERO")

                // CUS 2 - Gestión de Historia Clínica: Médico
                .requestMatchers("/medico/**")
                .hasAnyRole("ADMIN", "MEDICO")

                // CUS 3 - Gestión de Compras: Almacenero
                .requestMatchers(
                    "/farmacia/compras/dashboard",
                    "/farmacia/compras/kardex",
                    "/farmacia/compras/ordenes/**",
                    "/farmacia/compras/guias/**"
                )
                .hasAnyRole("ADMIN", "ALMACENERO")

                // CUS 3 - Gestión de Compras: Químico Farmacéutico
                .requestMatchers(
                    "/farmacia/compras/quimico/**"
                )
                .hasAnyRole("ADMIN", "QUIMICO_FARMACEUTICO")

                // CUS 3 - Gestión de Compras: Cobranza
                .requestMatchers(
                    "/farmacia/compras/cobranza/**"
                )
                .hasAnyRole("ADMIN", "COBRANZA")

                // CUS 4 - Gestión de Inventario: Químico Farmacéutico
                .requestMatchers(
                    "/farmacia/inventario/quimico/**"
                )
                .hasAnyRole("ADMIN", "QUIMICO_FARMACEUTICO")

                // CUS 4 - Gestión de Inventario: Almacenero
                .requestMatchers(
                    "/farmacia/inventario/dashboard",
                    "/farmacia/inventario/medicamentos/**",
                    "/farmacia/inventario/kardex/**",
                    "/farmacia/inventario/conteos/**",
                    "/farmacia/inventario/balances/**",
                    "/farmacia/inventario/observaciones/**"
                )
                .hasAnyRole("ADMIN", "ALMACENERO")

                // CUS 5 - Entrega de Medicamentos
                .requestMatchers("/farmacia/entregas/**")
                .hasAnyRole("ADMIN", "TECNICO_FARMACIA")

                // Cualquier otra ruta requiere login
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

                    boolean isTecnico = authentication.getAuthorities().stream()
                            .anyMatch(auth -> auth.getAuthority().equals("ROLE_TECNICO_FARMACIA"));

                    if (isAdmin) {
                        response.sendRedirect("/admin/dashboard");
                    } else if (isRecepcionista) {
                        response.sendRedirect("/dashboard");
                    } else if (isEnfermero) {
                        response.sendRedirect("/enfermeria/dashboard");
                    } else if (isMedico) {
                        response.sendRedirect("/medico/dashboard");
                    } else if (isAlmacenero) {
                        response.sendRedirect("/farmacia/inventario/dashboard");
                    } else if (isQuimico) {
                        response.sendRedirect("/farmacia/inventario/quimico/dashboard");
                    } else if (isCobranza) {
                        response.sendRedirect("/farmacia/compras/cobranza/dashboard");
                    } else if (isTecnico) {
                        response.sendRedirect("/farmacia/entregas/dashboard");
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

        UserDetails tecnico = User
                .withUsername("tecnico")
                .password("{noop}tecnico123")
                .roles("TECNICO_FARMACIA")
                .build();

        return new InMemoryUserDetailsManager(
                admin,
                recepcionista,
                enfermero,
                medico,
                almacenero,
                quimico,
                cobranza,
                tecnico
        );
    }
}
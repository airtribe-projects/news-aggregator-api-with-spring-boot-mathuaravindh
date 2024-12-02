package org.airtribe.newsaggregator.config;

import org.airtribe.newsaggregator.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final UserRepository userRepository;
    private final AuthenticationProvider authenticationProvider;
    private final AuthFilter jwtAuthenticationFilter;

    public SecurityConfig(UserRepository userRepository, AuthenticationProvider authenticationProvider, AuthFilter jwtAuthenticationFilter) {
        this.userRepository = userRepository;
        this.authenticationProvider = authenticationProvider;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf(csrf -> csrf.disable())  // Disable CSRF protection for simplicity
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/h2-console/**","/api/register","api/login").permitAll()  // Permit all to H2 console
//                        .anyRequest().authenticated()  // Require authentication for other requests
//                )
//                .formLogin(login -> login.permitAll())  // Allow form-based login for all users
//                .headers(headers -> headers
//                        .frameOptions(frameOptions -> frameOptions.sameOrigin())  // Allow frames for H2 console
//                );
//
//        return http.build();
    //}

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .headers(httpSecurityHeadersConfigurer ->
                {
                    httpSecurityHeadersConfigurer.frameOptions(frameOptionsConfig ->
                    {
                        frameOptionsConfig.disable();
                    });
                })
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/h2-console/**","/api/register","api/login").permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(List.of("http://localhost:8005"));
        configuration.setAllowedMethods(List.of("GET","POST"));
        configuration.setAllowedHeaders(List.of("Authorization","Content-Type"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**",configuration);

        return source;
    }

}

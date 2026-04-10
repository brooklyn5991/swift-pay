package com.swiftpay.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
        		.csrf(csrf -> csrf.disable())
        		.authorizeHttpRequests(auth -> auth
        			    // The ** means "this path and anything under it"
        			    .requestMatchers("/register/**", "/login/**", "/css/**", "/images/**").permitAll() 
        			    .requestMatchers("/").permitAll()
        			    .requestMatchers("/dashboard/**", "/transfer/**").hasAuthority("ROLE_USER")
        			    .anyRequest().authenticated()
        			)
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/dashboard", true)
                .permitAll()
            )
            .logout(logout -> logout
            	    .logoutUrl("/logout")
            	    .logoutSuccessUrl("/login?logout") // Adding ?logout helps the login page show a "Logged out" message
            	    .invalidateHttpSession(true)
            	    .clearAuthentication(true) // Add this to wipe the security context
            	    .deleteCookies("JSESSIONID")
            	    .permitAll()
            	)
            // --- ADDED FOR FINTECH SECURITY: PREVENT "BACK BUTTON" ACCESS ---
            .headers(headers -> headers
            	    .cacheControl(cache -> cache.disable()) // The standard way
            	    .defaultsDisabled() // Turn off defaults so we can be extra strict
            	    .contentTypeOptions(conf -> conf.disable())
            	    .frameOptions(frame -> frame.sameOrigin()) // Helpful for H2 console
            	    .addHeaderWriter((request, response) -> {
            	        // The "Triple Threat" headers that force browsers to stay fresh
            	        response.setHeader("Cache-Control", "no-cache, no-store, max-age=0, must-revalidate");
            	        response.setHeader("Pragma", "no-cache");
            	        response.setHeader("Expires", "0");
            	    })
            	)
            .build();
    }
}
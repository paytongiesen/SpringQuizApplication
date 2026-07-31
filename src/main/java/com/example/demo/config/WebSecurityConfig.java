package com.example.demo.config;

import com.example.demo.service.QuizUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            .authorizeHttpRequests(auth -> auth

                // Public pages
                .requestMatchers(
                    "/login",
                    "/registration",
                    "/style.css",
                    "/css/**",
                    "/js/**",
                    "/images/**"
                ).permitAll()

                // Admin only
                .requestMatchers("/quizList/**").hasRole("ADMIN")
                .requestMatchers("/addQuiz/**").hasRole("ADMIN")
                .requestMatchers("/editQuiz/**").hasRole("ADMIN")
                .requestMatchers("/deleteQuiz/**").hasRole("ADMIN")

                // Regular users
                .requestMatchers("/quiz/**").hasRole("USER")
                .requestMatchers("/submit/**").hasRole("USER")
                .requestMatchers("/result/**").hasRole("USER")

                // Everything else
                .anyRequest().authenticated()
            )

            .formLogin(login -> login
                .loginPage("/login")
                .successHandler((request, response, authentication) -> {

                    boolean isAdmin = authentication.getAuthorities()
                            .stream()
                            .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

                    if (isAdmin) {
                        response.sendRedirect("/quizList");
                    } else {
                        response.sendRedirect("/quiz");
                    }
                })
                .permitAll()
            )

            .logout(logout -> logout
                    .logoutSuccessUrl("/login?logout")
                    .permitAll());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
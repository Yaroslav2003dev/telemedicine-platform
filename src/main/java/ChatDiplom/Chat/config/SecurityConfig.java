package ChatDiplom.Chat.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

import java.io.IOException;
import java.util.Collection;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "/register", "/css/**", "/js/**").permitAll()  // Публичный доступ
                        .requestMatchers("/patient/**").hasRole("PATIENT")
                        .requestMatchers("/doctor/**").hasRole("DOCTOR")
                        .anyRequest().authenticated()  // Всё остальное требует входа
                )
                .formLogin(login -> login
                        .loginPage("/login")
                        .successHandler(customAuthenticationSuccessHandler()) // Используем кастомный обработчик
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
//                        .addLogoutHandler((request, response, authentication) -> {
//                            System.out.println("Pered: " + SecurityContextHolder.getContext().getAuthentication());
//                            new SecurityContextLogoutHandler().logout(request, response, authentication);
//                            SecurityContextHolder.clearContext();
//                            request.getSession().invalidate();
//                            System.out.println("Posle: " + SecurityContextHolder.getContext().getAuthentication());
//                        })
//                        .logoutSuccessHandler((request, response, authentication) -> {
//                            response.sendRedirect("/");
//                        })
//                        .logoutSuccessUrl("/")
//                        .clearAuthentication(true)
//                        .invalidateHttpSession(true)
//                        .deleteCookies("JSESSIONID", "remember-me")
                                .logoutUrl("/logout")
                                .logoutSuccessUrl("/")
                                .permitAll()

                );

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationSuccessHandler customAuthenticationSuccessHandler() {
        return (HttpServletRequest request, HttpServletResponse response, Authentication authentication) -> {
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

            for (GrantedAuthority authority : authorities) {
                if (authority.getAuthority().equals("ROLE_PATIENT")) {
                    response.sendRedirect("/patient");
                    return;
                } else if (authority.getAuthority().equals("ROLE_DOCTOR")) {
                    response.sendRedirect("/doctor");
                    return;
                }
            }

            response.sendRedirect("/dashboard"); // Перенаправление по умолчанию
        };
    }
}

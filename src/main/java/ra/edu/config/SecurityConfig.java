package ra.edu.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ra.edu.config.jwt.JwtAccessDeniedHandler;
import ra.edu.config.jwt.JwtAuthenticationEntryPoint;
import ra.edu.config.jwt.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationProvider authenticationProvider) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .authenticationProvider(authenticationProvider)
                .authorizeHttpRequests(auth -> auth
                        // Auth
                        .requestMatchers("/api/auth/login").permitAll()

                        .requestMatchers("/api/auth/me").hasAnyRole("ADMIN", "MENTOR", "STUDENT")

                        // Users
                        .requestMatchers("/api/users/**").hasRole("ADMIN")

                        // Students
                        .requestMatchers(HttpMethod.GET, "/api/students").hasAnyRole("ADMIN", "MENTOR")

                        .requestMatchers(HttpMethod.GET, "/api/students/*").hasAnyRole("ADMIN", "MENTOR", "STUDENT")

                        .requestMatchers(HttpMethod.POST, "/api/students").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.PUT, "/api/students/*").hasAnyRole("ADMIN", "STUDENT")

                        // Mentors
                        .requestMatchers(HttpMethod.GET, "/api/mentors/assigned").hasRole("STUDENT")

                        .requestMatchers(HttpMethod.GET, "/api/mentors/assigned/*").hasRole("STUDENT")

                        .requestMatchers(HttpMethod.GET, "/api/mentors").hasAnyRole("ADMIN", "STUDENT")

                        .requestMatchers(HttpMethod.GET, "/api/mentors/*").hasAnyRole("ADMIN", "MENTOR", "STUDENT")

                        .requestMatchers(HttpMethod.POST, "/api/mentors").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.PUT, "/api/mentors/*").hasAnyRole("ADMIN", "MENTOR")

                        // Internship Phases
                        .requestMatchers(HttpMethod.GET, "/api/internship-phases").hasAnyRole("ADMIN", "MENTOR", "STUDENT")

                        .requestMatchers(HttpMethod.GET, "/api/internship-phases/*").hasAnyRole("ADMIN", "MENTOR", "STUDENT")

                        .requestMatchers(HttpMethod.POST, "/api/internship-phases").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.PUT, "/api/internship-phases/*").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.DELETE, "/api/internship-phases/*").hasRole("ADMIN")

                        .anyRequest().authenticated()
                )
                .exceptionHandling(ex -> ex.authenticationEntryPoint(jwtAuthenticationEntryPoint)
                        .accessDeniedHandler(jwtAccessDeniedHandler))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(ss-> ss.sessionCreationPolicy(SessionCreationPolicy.STATELESS)); // phi trạng thái của restful
        return http.build();
    }

}

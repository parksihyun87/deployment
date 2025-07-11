package com.example.hibnb_project.config;

import com.example.hibnb_project.component.CustomAuthenticationEntryPoint;
import com.example.hibnb_project.component.CustomerAccessDeniedHandler;
import com.example.hibnb_project.data.repository.BlacklistRepository;
import com.example.hibnb_project.data.repository.UserRepository;
import com.example.hibnb_project.jwt.JwtFilter;
import com.example.hibnb_project.jwt.JwtLoginFilter;
import com.example.hibnb_project.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtUtil jwtUtil;
    private final AuthenticationConfiguration authenticationConfiguration;
    private final UserRepository userRepository;
    private final BlacklistRepository blacklistRepository;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomerAccessDeniedHandler customerAccessDeniedHandler;

    // AuthenticationManager 빈 생성
    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    // PasswordEncoder 빈 생성
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // SecurityFilterChain 설정
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .formLogin(form -> form.disable())
                .httpBasic(httpBasic -> httpBasic.disable())
                .authorizeHttpRequests(authorize -> {
                    // 여기 순서 주의: 구체적인 경로를 먼저 쓰고, 마지막에 anyRequest() 호출해야 함
                    authorize.requestMatchers("/", "/api/login", "/api/join", "/api/board/postlist", "/api/reissue", "/api/re-confirm-id-email", "/api/re-confirm-id", "/api/re-confirm-pw", "/api/accom/list", "/api/accom/list/detailedlist", "/api/accom/post", "/api/accom/test", "/api/kakao/**").permitAll();
                    authorize.requestMatchers("/api/admin/**").hasRole("ADMIN");
                    authorize.anyRequest().authenticated();
                })
                .cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration corsConfig = new CorsConfiguration();
                    corsConfig.addAllowedOrigin("http://localhost:3000");
                    corsConfig.addAllowedOrigin("http://3.34.237.122");
                    corsConfig.addAllowedHeader("*");
                    corsConfig.setExposedHeaders(List.of("Authorization"));
                    corsConfig.addAllowedMethod("*");
                    corsConfig.setAllowCredentials(true);
                    return corsConfig;
                }))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // 필터 순서 조절: JwtLoginFilter는 UsernamePasswordAuthenticationFilter 자리에 등록
                .addFilterBefore(new JwtFilter(jwtUtil), JwtLoginFilter.class)
                .addFilterAt(new JwtLoginFilter(jwtUtil, authenticationManager(), userRepository, blacklistRepository), UsernamePasswordAuthenticationFilter.class)
        //.exceptionHandling(exceptions -> {
        //    exceptions.authenticationEntryPoint(customAuthenticationEntryPoint);
        //    exceptions.accessDeniedHandler(customerAccessDeniedHandler);
        //})
        ;
        return http.build();
    }
}

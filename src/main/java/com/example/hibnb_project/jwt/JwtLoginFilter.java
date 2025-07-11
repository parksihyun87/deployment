package com.example.hibnb_project.jwt;

import com.example.hibnb_project.data.entity.BlacklistEntity;
import com.example.hibnb_project.data.entity.UserEntity;
import com.example.hibnb_project.data.repository.BlacklistRepository;
import com.example.hibnb_project.data.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class JwtLoginFilter extends UsernamePasswordAuthenticationFilter {
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager; // 직접구현한 로그인필터여서 인증매니저가 필요함
    private final UserRepository userRepository;
    private final BlacklistRepository blacklistRepository;

    public JwtLoginFilter(JwtUtil jwtUtil, AuthenticationManager authenticationManager, UserRepository userRepository, BlacklistRepository blacklistRepository) {
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.blacklistRepository = blacklistRepository;
        this.setFilterProcessesUrl("/api/login");
    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        String username = obtainUsername(request);
        String password = obtainPassword(request);
        if (username == null || password == null) {
            throw new AuthenticationServiceException("Username and password cannot be empty");
        }
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password, null);
        return authenticationManager.authenticate(authToken);
    }

//    @Override
//    public void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
//                                         Authentication authResult) throws IOException, ServletException {
//        UserDetails userDetails = (UserDetails) authResult.getPrincipal(); // getPrincipal 리턴값 : Object
//
//        String username = userDetails.getUsername();
//
//        UserEntity user = this.userRepository.findById(username).orElse(null);
//
//        // 1) 블랙리스트 체크
//        if (user != null && user.isBlacklisted()) {  // user.isBlacklisted() : 블랙리스트 여부 boolean 필드 가정
//            response.setStatus(HttpServletResponse.SC_FORBIDDEN); // 403 Forbidden
//            response.setContentType("application/json;charset=UTF-8");
//            response.getWriter().write("{\"result\":\"fail\", \"message\":\"블랙리스트 사용자입니다. 로그인 불가.\"}");
//            return;  // 여기서 리턴해서 로그인 성공 응답 종료
//        }
//
//        Collection<? extends GrantedAuthority> grantedAuthorities = userDetails.getAuthorities(); //
//        Iterator<? extends GrantedAuthority> iterator = grantedAuthorities.iterator();
//        GrantedAuthority grantedAuthority = iterator.next();
//        String role = grantedAuthority.getAuthority();
//
//        Map<String, Object> responseData = new HashMap<>();
//        responseData.put("username", username);
//        responseData.put("role", role);
//        responseData.put("result", "success");
//        UserEntity userentity = this.userRepository.findById(username).orElse(null);
//        if (userentity != null) {
//            responseData.put("email", userentity.getEmail());
//            responseData.put("name", userentity.getName());
//            responseData.put("phone", userentity.getPhone());
//            responseData.put("age", userentity.getAge());
//        }
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        String jsonMessage = objectMapper.writeValueAsString(responseData);
//
//        String access = this.jwtUtil.createToken("access", username, role, 60*60*1000L ); // L > Long
//        String refresh = this.jwtUtil.createToken("refresh", username, role, 60*60*24*1000L );
//
//        response.addHeader("Authorization", "Bearer " + access);
//        response.addCookie(this.createCookie("refresh", refresh));
//
//        response.setCharacterEncoding("UTF-8");
//        response.setContentType("application/json");
//        response.setStatus(HttpServletResponse.SC_OK);
//        response.getWriter().write(jsonMessage);
//
//    }

    @Override
    public void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                         Authentication authResult) throws IOException, ServletException {
        UserDetails userDetails = (UserDetails) authResult.getPrincipal();
        String username = userDetails.getUsername();

        // 사용자 정보 가져오기
        UserEntity user = this.userRepository.findById(username).orElse(null);

        // ✅ 블랙리스트 체크 (BlacklistEntity 조회)
        BlacklistEntity blacklist = this.blacklistRepository.findByUsername(username);
        if (blacklist != null) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN); // 403
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"result\":\"fail\", \"message\":\"블랙리스트 사용자입니다. 로그인 불가.\"}");
            return;
        }

        // 권한 확인
        Collection<? extends GrantedAuthority> grantedAuthorities = userDetails.getAuthorities();
        String role = grantedAuthorities.iterator().next().getAuthority();

        // 응답 데이터 구성
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("username", username);
        responseData.put("role", role);
        responseData.put("result", "success");
        if (user != null) {
            responseData.put("email", user.getEmail());
            responseData.put("name", user.getName());
            responseData.put("phone", user.getPhone());
            responseData.put("age", user.getAge());
        }

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonMessage = objectMapper.writeValueAsString(responseData);

        // JWT 토큰 생성
        String access = this.jwtUtil.createToken("access", username, role, 60 * 60 * 1000L);
        String refresh = this.jwtUtil.createToken("refresh", username, role, 60 * 60 * 24 * 1000L);

        response.addHeader("Authorization", "Bearer " + access);
        response.addCookie(this.createCookie("refresh", refresh));

        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write(jsonMessage);
    }


    @Override
    public void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                           AuthenticationException failed) throws IOException, ServletException {
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("result", failed.getMessage());

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonMessage = objectMapper.writeValueAsString(responseData);

        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(jsonMessage);
    }

    private Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setPath("/"); //브라우저에 저장될 쿠키가 프론트에서 보내는 모든 url경로의 요청에 포함되게 하겠다.
        cookie.setHttpOnly(true); // 브라우저에서 , 프론트에서 사용할 수 없음 withcredential true면 그냥 포함되게만 함
        cookie.setMaxAge(60 * 60 * 24); // 밀리초 x 초단위
        return cookie;
    }

}

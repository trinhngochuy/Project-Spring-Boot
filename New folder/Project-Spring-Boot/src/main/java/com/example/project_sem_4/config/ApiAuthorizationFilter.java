package com.example.project_sem_4.config;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.project_sem_4.database.entities.Account;
import com.example.project_sem_4.database.repository.AccountRepository;
import com.example.project_sem_4.util.JwtUtil;
import com.example.project_sem_4.util.exception_custom_message.ApiExceptionNotAcceptable;
import com.example.project_sem_4.util.exception_custom_message.ApiExceptionNotFound;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

import static java.util.Arrays.stream;

// this filter will run first with all request
// here we get the token from user then assign role for them
@Log4j2
public class ApiAuthorizationFilter extends OncePerRequestFilter {
    private static final String[] IGNORE_PATHS = {"/api/v1/login", "/api/v1/register", "/api/v1/token/refresh", "/cloud/uploads"};

    @Autowired
    private AccountRepository accountRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
        //let login and register pass through
        String requestPath = request.getServletPath();
        if (Arrays.asList(IGNORE_PATHS).contains(requestPath)) {
            filterChain.doFilter(request, response);
            return;
        }

        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        //get token in header then decode jwt token to get username and roles -> then set roles for request
        try {
            String token = authorizationHeader.replace("Bearer", "").trim();
            DecodedJWT decodedJWT = JwtUtil.getDecodedJwt(token);
            String username = decodedJWT.getSubject();

            Account checkAccount = accountRepository.findByEmail(username).orElse(null);
            if (checkAccount == null){
                throw new ApiExceptionNotFound("accounts","email", "không tìm thấy email là " + username);
            }
            if (checkAccount.getStatus() <= 0){
                throw new ApiExceptionNotAcceptable("Tài khoản chưa được kích hoạt hoặc bị khóa");
            }

            String[] roles= decodedJWT.getClaim(JwtUtil.ROLE_CLAIM_KEY).asArray(String.class);

            Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();

            stream(roles).forEach(role -> {
                authorities.add(new SimpleGrantedAuthority(role));
            });

            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(username, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            filterChain.doFilter(request, response);
        } catch (Exception ex) {
            //show error
            System.err.println(ex.getMessage());
            response.setStatus(HttpStatus.FORBIDDEN.value());
            Map<String, String> errors = new HashMap<>();
            errors.put("error", ex.getMessage());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            new ObjectMapper().writeValue(response.getOutputStream(), errors);
        }
    }
}

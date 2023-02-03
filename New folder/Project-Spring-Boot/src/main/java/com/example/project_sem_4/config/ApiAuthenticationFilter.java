package com.example.project_sem_4.config;

import com.example.project_sem_4.database.dto.CredentialDTO;
import com.example.project_sem_4.database.dto.RegisterDTO;
import com.example.project_sem_4.database.entities.Account;
import com.example.project_sem_4.database.entities.Role;
import com.example.project_sem_4.database.repository.AccountRepository;
import com.example.project_sem_4.enum_project.RoleEnum;
import com.example.project_sem_4.util.JwtUtil;
import com.example.project_sem_4.util.exception_custom_message.ApiExceptionNotAcceptable;
import com.example.project_sem_4.util.exception_custom_message.ApiExceptionNotFound;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * this filter is call when user want to login to the system
 * the default login path is "/login" but it can be override, see override login path in ApiSecurityConfig
 */
public class ApiAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    //we need this class to authenticate user when they login successfully

    private final AuthenticationManager authenticationManager;

    @Autowired
    AccountRepository accountRepository;

    public ApiAuthenticationFilter(AuthenticationManager authenticationManager, ApplicationContext ctx) {
        this.authenticationManager = authenticationManager;
        this.accountRepository = ctx.getBean(AccountRepository.class);
//        this.mailLogService = ctx.getBean(MailLogService.class);
    }

    //this function is call first when user try to login with their user name and password
    //so here we get username and password from request body then let spring do the magic
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            String jsonData = request.getReader().lines().collect(Collectors.joining());
            Gson gson = new Gson();
            //it should be loginDTO
            RegisterDTO registerDTO = gson.fromJson(jsonData, RegisterDTO.class);
            Account checkAccount = accountRepository.findByEmail(registerDTO.getEmail()).orElse(null);
            if (checkAccount == null){
                response.setStatus(HttpStatus.NOT_FOUND.value());
                Map<String, String> errors = new HashMap<>();
                errors.put("error", "Không tìm thấy email là " + registerDTO.getEmail());
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), errors);
            }
            if (checkAccount.getStatus() <= 0){
                response.setStatus(HttpStatus.NOT_ACCEPTABLE.value());
                Map<String, String> errors = new HashMap<>();
                errors.put("error", "Tài khoản chưa được kích hoạt hoặc bị khóa");
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), errors);
            }
            String email = registerDTO.getEmail();
            String password = registerDTO.getPassword();
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, password);
            return authenticationManager.authenticate(authenticationToken);
        } catch (IOException e) {
            System.out.println("Bach " + e);
            return null;
        }
    }

    //when username and password is correct this function will be call and pass in current login success information
    //so here we will return token for user
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        User user = (User) authentication.getPrincipal(); //get user that successfully login
        Account checkAccount = accountRepository.findByEmail(user.getUsername()).orElse(null);
        if (checkAccount == null){
            response.setStatus(HttpStatus.NOT_FOUND.value());
            Map<String, String> errors = new HashMap<>();
            errors.put("error", "Không tìm thấy email là " + user.getUsername());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            new ObjectMapper().writeValue(response.getOutputStream(), errors);
        }
        if (checkAccount.getStatus() <= 0){
            response.setStatus(HttpStatus.NOT_ACCEPTABLE.value());
            Map<String, String> errors = new HashMap<>();
            errors.put("error", "Tài khoản chưa được kích hoạt hoặc bị khóa");
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            new ObjectMapper().writeValue(response.getOutputStream(), errors);
        }
        //generate tokens
        String accessToken = JwtUtil.generateToken(user.getUsername(),
                user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()),
                request.getRequestURL().toString(),
                JwtUtil.ONE_DAY * 7);
        // generate refresh token
        String refreshToken = JwtUtil.generateToken(user.getUsername(),
                user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()),
                request.getRequestURL().toString(),
                JwtUtil.ONE_DAY * 14);
        boolean checkADMIN = false;
        for (Role role: checkAccount.getRoles()) {
            if (role.getName().equals(RoleEnum.ADMIN.role)){
                checkADMIN = true;
                break;
            }
        }

        CredentialDTO credential = new CredentialDTO(checkAccount.getId(),checkAccount.getName(),checkAccount.getPhone(),checkAccount.getEmail(),checkADMIN,checkAccount.getCreated_at(),checkAccount.getUpdated_at(),accessToken, refreshToken,user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()));
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), credential);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        super.unsuccessfulAuthentication(request, response, failed);
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getOutputStream().println("{ \"error\": \"" + "bach falied login" + "\" }");
    }

    //    @Override
//    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
//        System.out.println("Bach Authentication Failed");
//        MailLog mailLog = MailLog.builder()
//                .title("Bach test")
//                .email("bachntth2010055@fpt.edu.vn")
//                .status(0)
//                .build();
//        mailLogService.saveMailLog(mailLog);
//        //Add more descriptive message
//        response.setContentType("application/json");
//        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//        response.getOutputStream().println("{ \"error\": \"" + "bach falied login" + "\" }");
//    }
}

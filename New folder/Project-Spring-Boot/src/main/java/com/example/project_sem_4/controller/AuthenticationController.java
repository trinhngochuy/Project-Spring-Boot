package com.example.project_sem_4.controller;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.project_sem_4.database.dto.AccountDTO;
import com.example.project_sem_4.database.dto.CredentialDTO;
import com.example.project_sem_4.database.dto.RegisterCustomerDTO;
import com.example.project_sem_4.database.dto.RegisterDTO;
import com.example.project_sem_4.database.entities.Account;
import com.example.project_sem_4.database.entities.Role;
import com.example.project_sem_4.database.search_body.AccountSearchBody;
import com.example.project_sem_4.enum_project.RoleEnum;
import com.example.project_sem_4.service.authen.AuthenticationService;
import com.example.project_sem_4.service.mail.mail_comfirm.MailConfirmService;
import com.example.project_sem_4.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<Object> register(@RequestBody @Valid RegisterDTO registerDTO) {
        AccountDTO account = authenticationService.saveAccount(registerDTO);
        return ResponseEntity.ok().body(account);
    }

    @RequestMapping(value = "/registerCustomer", method = RequestMethod.POST)
    public ResponseEntity<Object> registerCustomer(@RequestBody @Valid RegisterCustomerDTO registerCustomerDTO) {
        AccountDTO account = authenticationService.saveAccountCustomer(registerCustomerDTO);
        return ResponseEntity.ok().body(account);
    }

    @RequestMapping(value = "/token/refresh", method = RequestMethod.GET)
    public ResponseEntity<Object> refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body("require token in header");
        }
        try {
            String token = authorizationHeader.replace("Bearer", "").trim();
            DecodedJWT decodedJWT = JwtUtil.getDecodedJwt(token);
            String username = decodedJWT.getSubject();
            //load account in the token
            Account account = authenticationService.getAccount(username);
            if (account == null) {
                return ResponseEntity.badRequest().body("Wrong token: Username not exist");
            }
            //now return new token
            //generate tokens
            Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
            List<String> roles = new ArrayList<>();
            for (Role role:
                    account.getRoles()) {
                authorities.add(new SimpleGrantedAuthority(role.getName()));
                roles.add(role.getName());
            }

            String accessToken = JwtUtil.generateToken(
                    account.getEmail(),
                    roles,
                    request.getRequestURL().toString(),
                    JwtUtil.ONE_DAY * 7);

            String refreshToken = JwtUtil.generateToken(
                    account.getEmail(),
                    null,
                    request.getRequestURL().toString(),
                    JwtUtil.ONE_DAY * 14);

            boolean checkADMIN = false;
            for (Role role: account.getRoles()) {
                if (role.getName().equals(RoleEnum.ADMIN.role)){
                    checkADMIN = true;
                    break;
                }
            }
            CredentialDTO credential = new CredentialDTO(account.getId(),account.getName(),account.getPhone(),account.getEmail(),checkADMIN,account.getCreated_at(),account.getUpdated_at(),accessToken, refreshToken,roles);
            return ResponseEntity.ok(credential);
        } catch (Exception ex) {
            //show error
            return ResponseEntity.internalServerError().body(ex.getMessage());
        }
    }

    @RequestMapping(value = "/account/search",method = RequestMethod.POST)
    public ResponseEntity<List<Account>> findAllJDBC(
//            @RequestParam(name = "page", defaultValue = "1") int page,
//            @RequestParam(name = "limit", defaultValue = "4") int limit,
//            @RequestParam(name = "sort", defaultValue = "asc") String sort,
//            @RequestParam(name = "name", required = false) String name,
//            @RequestParam(name = "role_id", defaultValue = "-1") Integer role_id,
//            @RequestParam(name = "member_ship_class_id", defaultValue = "-1") Integer member_ship_class_id,
//            @RequestParam(name = "phone", required = false) String phone,
//            @RequestParam(name = "gender", required = false) String gender,
//            @RequestParam(name = "status", defaultValue = "-1") Integer status,
//            @RequestParam(name = "start", required = false) String start,
//            @RequestParam(name = "end", required = false) String end
            @RequestBody AccountSearchBody accountSearchBody
    ){
        /*
            {
                "page": 1,
                "limit":4,
                "sort":"asc",
                "role_id":-1,
                "member_ship_class_id":-1,
                "status":-1
            }
        */

        return new ResponseEntity(authenticationService.findAllAccount(accountSearchBody), HttpStatus.OK);
    }

    @RequestMapping(value = "/account/{id}",method = RequestMethod.GET)
    public ResponseEntity<Account> findById(
            @PathVariable int id
    ){
        return new ResponseEntity(authenticationService.findAccountById(id), HttpStatus.OK);
    }

    @RequestMapping(value = "/account/findByRole_id/{id}",method = RequestMethod.GET)
    public ResponseEntity<Account> findByRole_id(
            @PathVariable int id
    ){
        return new ResponseEntity(authenticationService.findAccountByRole_id(id), HttpStatus.OK);
    }

    // Đi cùng với khóa và mở khóa tài khoản
    @RequestMapping(value = "/account/update/{id}",method = RequestMethod.POST)
    public ResponseEntity<Object> update(
            @RequestBody @Valid RegisterDTO registerDTO,
            @PathVariable int id){
        return ResponseEntity.ok().body(authenticationService.updateAccount(registerDTO,id));
    }

    @RequestMapping(value = "/account/active/{id}",method = RequestMethod.GET)
    public ResponseEntity<Account> activeAccount(
            @PathVariable Integer id
    ){
        return new ResponseEntity(authenticationService.activeAccount(id), HttpStatus.OK);
    }

    @RequestMapping(value = "/account/delete/{id}",method = RequestMethod.GET)
    public ResponseEntity<Account> deleteAccount(
            @PathVariable Integer id
    ){
        return new ResponseEntity(authenticationService.deleteAccount(id), HttpStatus.OK);
    }

    @RequestMapping(value = "/account/role",method = RequestMethod.GET)
    public ResponseEntity<Account> findAllRole(){
        return new ResponseEntity(authenticationService.findAllRole(), HttpStatus.OK);
    }
}

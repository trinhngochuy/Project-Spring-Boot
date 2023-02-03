package com.example.project_sem_4.service.authen;

import com.example.project_sem_4.database.dto.AccountDTO;
import com.example.project_sem_4.database.dto.RegisterCustomerDTO;
import com.example.project_sem_4.database.dto.RegisterDTO;
import com.example.project_sem_4.database.dto.search.account.AccountSearchDTO;
import com.example.project_sem_4.database.entities.Account;
import com.example.project_sem_4.database.entities.MembershipClass;
import com.example.project_sem_4.database.entities.Role;
import com.example.project_sem_4.database.jdbc_query.QueryAccountByJDBC;
import com.example.project_sem_4.database.repository.AccountRepository;
import com.example.project_sem_4.database.repository.MembershipClassRepository;
import com.example.project_sem_4.database.repository.RoleRepository;
import com.example.project_sem_4.database.search_body.AccountSearchBody;
import com.example.project_sem_4.enum_project.GenderEnum;
import com.example.project_sem_4.enum_project.StatusEnum;
import com.example.project_sem_4.enum_project.constant.GenderConstant;
import com.example.project_sem_4.enum_project.ERROR;
import com.example.project_sem_4.service.mail.mail_comfirm.MailConfirmService;
import com.example.project_sem_4.util.exception_custom_message.ApiExceptionBadRequest;
import com.example.project_sem_4.util.exception_custom_message.ApiExceptionNotFound;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

import javax.transaction.Transactional;
import java.lang.reflect.Type;
import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
@Log4j2
@CrossOrigin(origins = "*")
public class AuthenticationService implements UserDetailsService {
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Autowired
    private MembershipClassRepository membershipClassRepository;
    @Autowired
    private QueryAccountByJDBC queryAccountByJDBC;
    @Autowired
    private MailConfirmService mailConfirmService;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Account> accountOptional = accountRepository.findByEmail(email);
        Account account = accountOptional.orElse(null);
        if (account == null) {
            throw new UsernameNotFoundException("Email not found in database");
        }
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        for (Role role:
                account.getRoles()) {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        }
        UserDetails userDetail
                = new User(account.getEmail(), account.getPassword(), authorities);
        return
                userDetail;

    }

    @Transactional
    public AccountDTO saveAccount(RegisterDTO registerDTO){
        //create new user role if not exist
        Set<Role> roles = new HashSet<>();
        for (Role role: registerDTO.getRoles()) {
            Optional<Role> userRoleOptional = roleRepository.findByName(role.getName());
            Role userRole = userRoleOptional.orElse(null);
            if (userRole == null) {
                return null;
            }
            roles.add(userRoleOptional.get());
        }
        if (accountRepository.findAccountsByEmail(registerDTO.getEmail()).size() >= 1){
            throw new ApiExceptionBadRequest("accounts","email","Email đã tồn tại");
        }
        //check if username has exist
        Optional<Account> byUsername = accountRepository.findByEmail(registerDTO.getEmail());
        if (byUsername.isPresent()) {
            return null;
        }
        Account account = new Account();
        account.setName(registerDTO.getName());
        account.setAddress(registerDTO.getAddress());
        account.setPhone(registerDTO.getPhone());
        account.setEmail(registerDTO.getEmail());
        account.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        switch (registerDTO.getGender()){
            case GenderConstant.CHECKGENER.MALE:
                account.setGender(GenderEnum.MALE.toString());
                break;
            case GenderConstant.CHECKGENER.FEMALE:
                account.setGender(GenderEnum.FEMALE.toString());
                break;
        }

        if (registerDTO.getThumbnail() !=null){
            account.setThumbnail(registerDTO.getThumbnail());
        }

        account.setCreated_at(new Date());
        account.setStatus(StatusEnum.UN_ACTIVE.status);

        MembershipClass membershipClass = membershipClassRepository.findById(1).orElse(null);
        if (membershipClass == null){
            throw new ApiExceptionNotFound("membership_classes","id","với giá trị nhập cứng là "+5);
        }
        account.setMembershipClass(membershipClass);
        account.setRoles(roles);
        account.setDescription(registerDTO.getDescription());
        Account save = accountRepository.save(account);

        mailConfirmService.sendMailConfirm(save.getEmail(),save.getId());

        return new AccountDTO(save);
    }

    @Transactional
    public AccountDTO saveAccountCustomer(RegisterCustomerDTO registerCustomerDTO){
        //create new user role if not exist
        Set<Role> roles = new HashSet<>();
//        roles.add(Role.builder().name("CUSTOMER").build());

        Optional<Role> userRoleOptional = roleRepository.findByName("CUSTOMER");
        Role userRole = userRoleOptional.orElse(null);
        if (userRole == null) {
            throw new ApiExceptionNotFound("roles","name","Không thấy role CUSTOMER");
        }
        roles.add(userRoleOptional.get());

        if (accountRepository.findAccountsByEmail(registerCustomerDTO.getEmail()).size() >= 1){
            throw new ApiExceptionBadRequest("accounts","email","Email đã tồn tại");
        }
        //check if username has exist
        Optional<Account> byUsername = accountRepository.findByEmail(registerCustomerDTO.getEmail());
        if (byUsername.isPresent()) {
            return null;
        }
        Account account = new Account();
        account.setName(registerCustomerDTO.getName());
        account.setAddress(registerCustomerDTO.getAddress());
        account.setPhone(registerCustomerDTO.getPhone());
        account.setEmail(registerCustomerDTO.getEmail());
        account.setPassword(passwordEncoder.encode(registerCustomerDTO.getPassword()));
        switch (registerCustomerDTO.getGender()){
            case GenderConstant.CHECKGENER.MALE:
                account.setGender(GenderEnum.MALE.toString());
                break;
            case GenderConstant.CHECKGENER.FEMALE:
                account.setGender(GenderEnum.FEMALE.toString());
                break;
        }

        if (registerCustomerDTO.getThumbnail() !=null){
            account.setThumbnail(registerCustomerDTO.getThumbnail());
        }

        account.setCreated_at(new Date());
        account.setStatus(StatusEnum.UN_ACTIVE.status);

        MembershipClass membershipClass = membershipClassRepository.findById(5).orElse(null);
        if (membershipClass == null){
            throw new ApiExceptionNotFound("membership_classes","id","với giá trị nhập cứng là "+5);
        }
        account.setMembershipClass(membershipClass);
        account.setRoles(roles);
        account.setDescription(registerCustomerDTO.getDescription());
        Account save = accountRepository.save(account);

        mailConfirmService.sendMailConfirm(save.getEmail(),save.getId());

        return new AccountDTO(save);
    }

    public Account getAccount(String email) {
        Optional<Account> byUsername = accountRepository.findByEmail(email);
        return byUsername.orElse(null);
    }
    @Transactional
    public Account updateAccount(RegisterDTO account, int id){
        Account checkAccount = accountRepository.findById(id).orElse(null);
        if (checkAccount == null){
            throw new ApiExceptionNotFound("accounts","id", "có giá trị là " + id);
        }
//        Account.AccountBuilder updateAccount = Account.builder()
//                .name(account.getName());
        if (!checkAccount.getEmail().equals(account.getEmail())){
            if (accountRepository.findAccountsByEmail(account.getEmail()).size() >= 1){
                throw new ApiExceptionBadRequest("accounts","email","Email đã tồn tại");
            }
            checkAccount.setEmail(account.getEmail());
        }

        if (account.getPassword() != null){
            checkAccount.setPassword(passwordEncoder.encode(account.getPassword()));
        }

        if (account.getMember_ship_class_id() != null){
            MembershipClass membershipClass = membershipClassRepository.findById(account.getMember_ship_class_id()).orElse(null);
            if (membershipClass == null){
                throw new ApiExceptionNotFound("membership_classes","id","với giá trị là "+account.getMember_ship_class_id());
            }
            checkAccount.setMembershipClass(membershipClass);
        }

        if (account.getTotal_payment() != null){
            checkAccount.setTotal_payment(account.getTotal_payment());
        }

        if (account.getRoles() != null && account.getRoles().size() <= 0){
            throw new ApiExceptionBadRequest("accounts","roles","Yêu cầu tồn tại ít nhất một quyền");
        }

        if (account.getGender() != null){
            if (!account.getGender().equals(GenderConstant.CHECKGENER.MALE) && !account.getGender().equals(GenderConstant.CHECKGENER.MALE)){
                throw new ApiExceptionBadRequest("accounts","gender","Sai quy định dạng giới tính");
            }
        }

        if (account.getRoles() != null && account.getRoles().size() >0){
            Set<Role> roles = new HashSet<>();
            for (Role role: account.getRoles()) {
                Optional<Role> userRoleOptional = roleRepository.findByName(role.getName());
                Role userRole = userRoleOptional.orElse(null);
                if (userRole == null) {
                    //create new role
//            userRole = roleRepository.save(new Role(USER_ROLE));
                    return null;
                }
                roles.add(userRoleOptional.get());
            }

            checkAccount.setRoles(roles);
        }
        if (account.getAddress() != null){
            checkAccount.setAddress(account.getAddress());
        }
        if (account.getThumbnail() != null){
            checkAccount.setThumbnail(account.getThumbnail());
        }
        if (account.getPhone() != null){
            checkAccount.setPhone(account.getPhone());
        }
        checkAccount.setName(account.getName());
        checkAccount.setDescription(account.getDescription());

        return accountRepository.save(checkAccount);
    }

    public Account findAccountById(int id){
        return accountRepository.findById(id).orElse(null);
    }
    public List<Account> findAccountByRole_id(int id){
        return accountRepository.findAccountsByRole_id(id);
    }

    public Account activeAccount(int id){
        Account account = accountRepository.findById(id).orElse(null);
        if (account == null){
            throw new ApiExceptionNotFound("accounts","id", "không tìm thấy id là " + id);
        }
        account.setStatus(StatusEnum.ACTIVE.status);
        return accountRepository.save(account);
    }

    public Account deleteAccount(int id){
        Account account = accountRepository.findById(id).orElse(null);
        if (account == null){
            throw new ApiExceptionNotFound("accounts","id", "không tìm thấy id là " + id);
        }
        account.setStatus(StatusEnum.DELETE.status);
        return accountRepository.save(account);
    }

    public Map<String, Object> findAllAccount(AccountSearchBody searchBody){
        Gson gson = new Gson();
        Type listType = new TypeToken<Set<Role>>(){}.getType();
        List<AccountSearchDTO> listContentPage = queryAccountByJDBC.filterWithPaging(searchBody);
        for (AccountSearchDTO check: listContentPage) {
            check.setRoles(gson.fromJson(check.getRolesListBefore(),listType));
        }
        List<AccountSearchDTO> listContentNoPage = queryAccountByJDBC.filterWithNoPaging(searchBody);

        Map<String, Object> responses = new HashMap<>();
        responses.put("content",listContentPage);
        responses.put("currentPage",searchBody.getPage());
        responses.put("totalItems",listContentNoPage.size());
        responses.put("totalPage",(int) Math.ceil((double) listContentNoPage.size() / searchBody.getLimit()));
        return responses;
    }

    @Transactional
    public Account saveWalk_In_Guest(){
        MembershipClass membershipClassAgain = membershipClassRepository.findById(5).orElse(null);

        Set<Role> roles = new HashSet<>();
        Optional<Role> userRoleOptional = roleRepository.findByName("CUSTOMER");
        Role userRole = userRoleOptional.orElse(null);
        if (userRole == null) {
            throw new ApiExceptionNotFound("roles","name","Không thấy role CUSTOMER");
        }
        roles.add(userRoleOptional.get());

        Account create_Walk_In_Guest = new Account();
        create_Walk_In_Guest.setName("Walk_In_Guest");
        create_Walk_In_Guest.setEmail("Walk_In_Guest@gmail.com");
        create_Walk_In_Guest.setAddress("From No Where");
        create_Walk_In_Guest.setPhone("123");
        create_Walk_In_Guest.setPassword(passwordEncoder.encode("123"));
        create_Walk_In_Guest.setGender(GenderEnum.MALE.gender);
        create_Walk_In_Guest.setMembershipClass(membershipClassAgain);
        create_Walk_In_Guest.setRoles(roles);
        create_Walk_In_Guest.setCreated_at(new Date());
        create_Walk_In_Guest.setStatus(StatusEnum.ACTIVE.status);

        return accountRepository.save(create_Walk_In_Guest);

    }

    public List<Role> findAllRole(){
        return roleRepository.findAll();
    }
}

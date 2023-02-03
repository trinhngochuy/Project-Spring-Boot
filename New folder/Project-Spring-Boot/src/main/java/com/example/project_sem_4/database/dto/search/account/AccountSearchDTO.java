package com.example.project_sem_4.database.dto.search.account;

import com.example.project_sem_4.database.entities.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountSearchDTO {
    private Integer accounts_id;
    private String accounts_name;
    private String accounts_email;
    private String account_description;
    private String phone;
    private String gender;
    private String address;
    private String thumbnail;
    private Double total_payment;
    private String account_created_at;
    @JsonIgnore
    private String rolesListBefore;
    private Set<Role> roles;
}

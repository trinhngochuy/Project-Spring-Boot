package com.example.project_sem_4.database.dto;

import com.example.project_sem_4.database.entities.Account;
import com.example.project_sem_4.database.entities.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;
import java.util.Set;

@Data
@AllArgsConstructor
public class AccountDTO {
    private long id;
    private String name;
    private String email;
    private Set<Role> roles;
    private int status;
    private Date createdAt;
    private Date updatedAt;

    public AccountDTO(Account account) {
        this.id = account.getId();
        this.name = account.getName();
        this.email = account.getEmail();
        this.roles = account.getRoles();
        this.status = account.getStatus();
        this.createdAt = account.getCreated_at();
        this.updatedAt = account.getUpdated_at();
    }

}

package com.example.project_sem_4.database.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
public class CredentialDTO {
    private Integer id;
    private String username;
    private String phone;
    private String email;
    @JsonProperty(namespace = "isAdmin")
    private boolean isAdmin;
    private Date created_at;
    private Date updated_at;
    private String accessToken;
    private String refreshToken;
    private List<String> roles;
}

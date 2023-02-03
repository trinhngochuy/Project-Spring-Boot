package com.example.project_sem_4.database.dto;

import com.example.project_sem_4.database.entities.Role;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterCustomerDTO {
    @NotEmpty(message = "Thiếu tên")
    private String name;
    private String address;
    private String description;
    @Email(message = "Sai định dạng email")
    @NotEmpty(message = "Thiếu email")
    private String email;
    @NotEmpty(message = "Thiếu password")
    private String password;
    @NotEmpty(message = "Thiếu số điện thoại")
    private String phone;
    private String thumbnail;
    @NotEmpty(message = "Thiếu giới tính")
    private String gender;
}

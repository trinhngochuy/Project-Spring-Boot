package com.example.project_sem_4.database.entities;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@Entity
@AllArgsConstructor
@Builder
@Table(name = "feed_backs")
public class FeedBack extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotEmpty(message = "Thiếu tiêu đề")
    private String title;
    @NotEmpty(message = "Thiếu email")
    @Email(message = "Sai định dạng email")
    private String email;
    @NotEmpty(message = "Thiếu số điện thoại")
    private String phone;
    @NotEmpty(message = "Thiếu mô tả")
    private String description;
    @Column(columnDefinition = "integer default 0", name = "account_id",nullable = false)
    private int account_id;

    public FeedBack() {
        super();
    }

}

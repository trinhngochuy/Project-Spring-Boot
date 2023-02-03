package com.example.project_sem_4.database.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
public class BlogDTO {

    @NotEmpty(message = "Thiếu mô tả")
    private String description;
    @NotEmpty(message = "Thiếu Tiêu đề")
    private String title;
    @NotEmpty(message = "Thiếu Nội dung")
    private String content;

    private String thumbnail;

    private int account_id;
}

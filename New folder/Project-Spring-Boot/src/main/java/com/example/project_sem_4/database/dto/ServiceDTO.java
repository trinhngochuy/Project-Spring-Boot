package com.example.project_sem_4.database.dto;

import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ServiceDTO {
    @NotEmpty(message = "Thiếu tên dịch vụ")
    private String name;
    private String description;
    @NotNull(message = "Thiếu loại dịch vụ")
    @Min(value = 1, message= "Thiếu loại dịch vụ")
    private Integer typeServiceId;
    private String thumbnail;
    @NotNull(message = "Thiếu giá dịch vụ")
    @Min(value = 1, message= "Thiếu giá dịch vụ")
    private Double price;
}

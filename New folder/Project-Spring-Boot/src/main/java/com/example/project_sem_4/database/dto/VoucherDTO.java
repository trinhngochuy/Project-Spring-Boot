package com.example.project_sem_4.database.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.validation.constraints.NotEmpty;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VoucherDTO {
    @NotEmpty(message = "Thiếu tên")
    private String name;
    private Double discount;
    private Date expired_date;
}

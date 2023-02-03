package com.example.project_sem_4.database.dto.order;

import com.example.project_sem_4.database.entities.OrderDetail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.*;
import java.util.Set;

@Data
@AllArgsConstructor
@Builder
public class OrderDTO {
    private String voucher_id;
    @NotEmpty(message = "Thiếu lịch đặt")
    private String booking_id;
    @NotNull(message = "Thiếu id nhân viên")
    @Min(value = 1, message= "Thiếu id nhân viên")
    private Integer customer_id;
    @Email(message = "Sai định dạng email")
    @NotEmpty(message = "Thiếu email")
    private String email;
    @NotEmpty(message = "Thiếu số điện thoại")
    @Size(min = 10, max = 10, message = "Sai định dạng điện thoại")
    private String phone;
    @NotEmpty(message = "Thiếu tên người đặt")
    private String name_booking;
}

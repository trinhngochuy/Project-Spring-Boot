package com.example.project_sem_4.database.dto.order;

import com.example.project_sem_4.database.entities.Account;
import com.example.project_sem_4.database.entities.Booking;
import com.example.project_sem_4.database.entities.Voucher;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderSearchDTO {
    private Integer id;
    private Integer total_price;
    private Integer status;
    private Integer user_id;
    private Account customer;
    private Booking booking;
    private Voucher voucher;
    private String created_at;
}

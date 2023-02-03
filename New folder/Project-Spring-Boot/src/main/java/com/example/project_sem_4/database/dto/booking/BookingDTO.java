package com.example.project_sem_4.database.dto.booking;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class BookingDTO {
    private String date_booking;
    private String time_booking;
    @NotNull(message = "Thiếu id nhân viên")
    @Min(value = 1, message= "Thiếu id nhân viên")
    private int employee_id;

    @NotNull(message = "Thiếu id chi nhánh")
    @Min(value = 1, message= "Thiếu id chi nhánh")
    private int branch_id;
    private Integer status;
}

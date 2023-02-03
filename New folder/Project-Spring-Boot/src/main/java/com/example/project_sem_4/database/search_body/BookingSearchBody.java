package com.example.project_sem_4.database.search_body;

import com.example.project_sem_4.enum_project.RoleEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@AllArgsConstructor
@Getter
@Setter
public class BookingSearchBody {
    private String booking_id;
    private List<String> idsBooking;
    private Integer branch_id;
    private Integer employee_id;
    private String employee_name;
    private String role;
    private String date_booking;

    private String time_booking;

    private Integer status;
    private String start; // Ngày tạo bắt đầu
    private String end; // Ngày tạo kết thúc
    private Integer limit;
    private Integer page;
    private String sort;

    public BookingSearchBody() {
        this.limit = 1;
        this.page = 4;
        this.role = RoleEnum.STAFF.role;
    }
}

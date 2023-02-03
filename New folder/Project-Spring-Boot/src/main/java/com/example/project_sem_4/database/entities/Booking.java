package com.example.project_sem_4.database.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

@Getter
@Setter
@Entity
@AllArgsConstructor
@Builder
@Table(name = "bookings")
public class Booking extends BaseEntity {
    @Id
    @GeneratedValue(generator = "my_generator")
    @GenericGenerator(name = "my_generator", strategy = "com.example.project_sem_4.database.generator.MyGenerator")
    private String id; // Lịch chỉ tập trung đặt lịch cho nhân viên thôi còn service là đính kèm
    private String date;
    private String date_booking;// Kiểm tra ngày đặt lịch
    private String time_booking;// Thời gian đặt
    private String email;
    private String phone;
    private String name_booking;
    private int user_id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER, cascade =
            {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}
    )
    @JoinColumn(name = "employee_id")
    private Account employee;

    @Column(insertable = false, updatable = false)
    private int employee_id;

    @ManyToOne(fetch = FetchType.EAGER, cascade =
            {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}
    )
    @JoinColumn(name = "branch_id")
    private Branch branch;

    @Column(insertable = false, updatable = false)
    private int branch_id;

    public Booking() {
        super();
    }



}

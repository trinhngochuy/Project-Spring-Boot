package com.example.project_sem_4.database.dto.booking;

import com.example.project_sem_4.database.entities.Booking;
import com.example.project_sem_4.database.entities.Role;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class BookingSearchDTO {
    public BookingSearchDTO(){
    }
    @Getter
    @Setter
    public static class Employee{

        private String employee_name;
        private List<Role> roles;

        private List<Booking> BookingByTime_bookings;

        public Employee() {
        }
    }
    public Employee employee;
    public BookingSearchDTO(Employee emp){
        this.employee = emp;
    }
}

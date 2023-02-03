package com.example.project_sem_4.enum_project;

public enum RoleEnum {
    ADMIN("ADMIN"),
    RECEPTIONISTS("RECEPTIONISTS"),
    CUSTOMER_CARE("CUSTOMER_CARE"),
    STAFF("STAFF"),
    CUSTOMER("CUSTOMER"),
    ;
    public String role;

    RoleEnum(String role) {
        this.role = role;
    }
}

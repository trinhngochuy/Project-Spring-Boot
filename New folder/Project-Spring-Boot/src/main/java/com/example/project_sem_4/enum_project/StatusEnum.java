package com.example.project_sem_4.enum_project;

public enum StatusEnum {
    DELETE(-1),UN_ACTIVE(0),ACTIVE(1),IS_COME_SUCCESS(2);

    public Integer status;
    StatusEnum(Integer status) {
        this.status = status;
    }
}

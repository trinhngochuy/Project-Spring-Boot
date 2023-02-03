package com.example.project_sem_4.enum_project;

public enum FeedBackEnum {
    DELETE(-1),NOT_READED(0),READED(1);

    public Integer status;
    FeedBackEnum(Integer status) {
        this.status = status;
    }
}

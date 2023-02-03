package com.example.project_sem_4.enum_project;

public enum TimeBookingEnum {
    TIME_8H ("8","08:00:00"),
    TIME_9H ("9","09:00:00"),
    TIME_10H ("10","10:00:00"),
    TIME_11H ("11","11:00:00"),
    TIME_13H ("13","13:00:00"),
    TIME_14H ("14","14:00:00"),
    TIME_15H ("15","15:00:00"),
    TIME_16H ("16","16:00:00"),
    ;
    public String timeName;
    public String timeValue;
    TimeBookingEnum(String timeName,String timeValue) {
        this.timeName = timeName;
        this.timeValue = timeValue;
    }
}

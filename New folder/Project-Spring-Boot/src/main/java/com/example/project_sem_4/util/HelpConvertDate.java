package com.example.project_sem_4.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class HelpConvertDate {
    private String date;

    public static LocalDate convertStringToLocalDate(String date){
        DateTimeFormatter pattern =  DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try {
            return LocalDate.parse(date, pattern);
        }catch (DateTimeParseException e){
            e.printStackTrace();
            return null;
        }
    }
}

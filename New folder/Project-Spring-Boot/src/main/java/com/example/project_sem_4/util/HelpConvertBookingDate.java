package com.example.project_sem_4.util;

import lombok.extern.log4j.Log4j2;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;

@Log4j2
public class HelpConvertBookingDate {
    private String date;

    public static Date convertStringToDate(String date){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
        try {
//            log.info("dateString là " + dateString);
//            log.info("Thời gian parse thường là " + date);
//            log.info("Thời gian parse time là " + date.getTime());
//            log.info("Thời gian parse day là " + date.getDay());
//            log.info("Thời gian parse date là " + date.getDate());
//            log.info("Thời gian parse date là " + date.getHours());
//            Calendar cal = Calendar.getInstance();
//            cal.setTime(date);
//            log.info("Thời gian calendar là " + new SimpleDateFormat("EEEE").format(cal.getTime()));
        return dateFormat.parse(date);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}

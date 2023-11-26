package com.backend.blooming.goal.application.util;

import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
public class DateFormat {

    public Date dateFormatter(String date){
        SimpleDateFormat formatter;
        Date dateFormatted = new Date();

        try{
            formatter = new SimpleDateFormat("yyyy-MM-dd");
            dateFormatted = formatter.parse(date);

            return dateFormatted;
        } catch (ParseException e) {
            log.info("날짜 파싱 실패");
        }

        return dateFormatted;
    }
}

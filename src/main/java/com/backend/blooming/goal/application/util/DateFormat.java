package com.backend.blooming.goal.application.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormat {

    public Date dateFormatter(String date) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        Date dateFormatted = formatter.parse(date);

        return dateFormatted;
    }
}

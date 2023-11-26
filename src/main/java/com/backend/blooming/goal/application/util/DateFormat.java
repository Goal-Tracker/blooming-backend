package com.backend.blooming.goal.application.util;

import com.backend.blooming.goal.application.exception.DateFormatParseException;
import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
public class DateFormat {

    public static Date dateFormatter(final String date) {
        final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date dateFormatted;

        try {
            dateFormatted = formatter.parse(date);
            return dateFormatted;
        } catch (final ParseException exception) {
            throw new DateFormatParseException();
        }
    }
}

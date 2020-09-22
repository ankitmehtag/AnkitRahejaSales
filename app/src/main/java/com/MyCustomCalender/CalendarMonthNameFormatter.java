package com.MyCustomCalender;

import android.text.format.DateFormat;

import java.util.Calendar;

public class CalendarMonthNameFormatter {

    private boolean mShowYear;

    public CalendarMonthNameFormatter() {}

    public CalendarMonthNameFormatter(boolean showYear) {
        mShowYear = showYear;
    }

    public CharSequence format(Calendar calendar) {
        String format = "LLLL" + (mShowYear ? " yyyy" : "");
        String monthName = DateFormat.format(format, calendar.getTime()).toString();
        return Character.toUpperCase(monthName.charAt(0)) +
                monthName.substring(1, monthName.length());
    }

    void showYear(boolean showYear) {
        mShowYear = showYear;
    }
}

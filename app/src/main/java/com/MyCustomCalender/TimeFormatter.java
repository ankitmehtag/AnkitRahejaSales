package com.MyCustomCalender;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TimeFormatter {

    private Date mDate = new Date();
    private DateFormat mDateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

    public String format(long timeInMillis) {
        mDate.setTime(timeInMillis);
        return mDateFormat.format(mDate);
    }
}

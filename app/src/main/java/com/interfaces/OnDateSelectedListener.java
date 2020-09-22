package com.interfaces;

import androidx.annotation.Nullable;

import com.MyCustomCalender.CalendarEvent;

import java.util.List;

public interface OnDateSelectedListener {

    /**
     * Called after click on day of the month
     * @param dayCalendar Calendar of selected day
     * @param events Events of selected day
     */
    void onDateSelected(String dayCalendar, @Nullable List<CalendarEvent> events);
}
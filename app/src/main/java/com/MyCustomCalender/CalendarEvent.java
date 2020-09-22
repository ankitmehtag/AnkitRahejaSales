package com.MyCustomCalender;


import android.content.Context;

import com.model.AsmSalesModel;
import com.model.PreSalesAsmModel;

import java.util.ArrayList;

public abstract class CalendarEvent {

    private int mIndicatorColor;
    private long mStartTimeInMillis;
    private String text;
    private String title;
    ArrayList<AsmSalesModel> asmFollowUpLeads;
    Context mContext;

    public CalendarEvent(long startTimeInMillis, int indicatorColor, String txt, ArrayList<AsmSalesModel> asmFollowUpLeadList, Context context, String new_title) {
        mStartTimeInMillis = startTimeInMillis;
        mIndicatorColor = indicatorColor;
        asmFollowUpLeads = asmFollowUpLeadList;
        title=new_title;
        mContext = context;
        text = txt;
    }

    public String getTitle() {
        return title;
    }

    public ArrayList<AsmSalesModel> getAsmFollowUpLeads() {
        return asmFollowUpLeads;
    }

    public Context getmContext() {
        return mContext;
    }

    public long getTimeInMillis() {
        return mStartTimeInMillis;
    }

    public int getColor() {
        return mIndicatorColor;
    }

    public String getText() {
        return text;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof CalendarEvent)) {
            return false;
        }
        CalendarEvent eventObj = (CalendarEvent) obj;
        return eventObj.getText() == text && eventObj.getText() == text;
    }

    @Override
    public int hashCode() {
        return 37 * mIndicatorColor + (int) (mStartTimeInMillis ^ (mStartTimeInMillis >>> 32));
    }
}
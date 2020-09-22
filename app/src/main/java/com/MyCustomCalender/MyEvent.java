package com.MyCustomCalender;

import android.content.Context;
import android.widget.TextView;

import com.model.AsmSalesModel;
import com.model.PreSalesAsmModel;

import java.util.ArrayList;

public class MyEvent extends CalendarEvent {

    private String mTitle;

    public MyEvent(String title, long startTimeInMillis, int indicatorColor, String tv, ArrayList<AsmSalesModel> asmFollowUpLeads, Context context) {
        super(startTimeInMillis, indicatorColor, tv, asmFollowUpLeads,context,title);
        mTitle = title;
    }

    public String getTitle() {
        return mTitle;
    }
}

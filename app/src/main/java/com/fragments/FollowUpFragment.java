package com.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.MyCustomCalender.CalendarEvent;
import com.MyCustomCalender.CalendarView;
import com.MyCustomCalender.MyEvent;
import com.activities.AsmHistoryActivity;
import com.activities.FollowUpSalesDetailActivity;
import com.activities.SalesActivity;
import com.adapters.AsmSalesAdapter;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.appnetwork.WEBAPI;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.helper.BMHConstants;
import com.helper.ParamsConstants;
import com.interfaces.OnDateSelectedListener;
import com.interfaces.OnLoadEventsListener;
import com.interfaces.OnMonthChangedListener;
import com.model.AsmSalesLeadDetailModel;
import com.model.AsmSalesModel;
import com.model.Assign_To;
import com.model.LeadStatus;
import com.model.NotInterestedLead;
import com.model.Projects;
import com.model.SubStatus;
import com.sp.BMHApplication;
import com.sp.R;
import com.utils.Connectivity;
import com.utils.MyValueFormatter;
import com.utils.StringUtil;
import com.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;

import static com.utils.Utils.dateFormat_yyymmdd;
import static com.utils.Utils.dateFormat_yyymmdd_test;

public class FollowUpFragment extends Fragment  implements OnMonthChangedListener,
        OnDateSelectedListener, OnLoadEventsListener {
    private RecyclerView mRecyclerView;
    private static String TAB_NAME = null;
    private Context mContext;
    Bundle bundle;
    private Gson gson;
    private Type listType;
    private AsmSalesModel mAsmModel;
    private AsmSalesLeadDetailModel mDetailsModel;
    private ArrayList<AsmSalesModel> mAsmList;
    private ArrayList<AsmSalesModel> asmFollowUpLeads = new ArrayList<>();
    private ArrayList<AsmSalesModel> selectedDateList = new ArrayList<>();
    private ArrayList<AsmSalesModel> hotList = new ArrayList<>();
    private ArrayList<AsmSalesModel> coldList = new ArrayList<>();
    private ArrayList<AsmSalesModel> warmList = new ArrayList<>();
    ArrayList<Projects> projectsList = new ArrayList<>();
    ArrayList<LeadStatus> leadsList = new ArrayList<>();
    ArrayList<NotInterestedLead> notInterestedList = new ArrayList<>();
    ArrayList<NotInterestedLead> closureList = new ArrayList<>();
    ArrayList<NotInterestedLead> brokerList = new ArrayList<>();
    private ArrayList<AsmSalesLeadDetailModel> mDetailsList = new ArrayList<>();
    private SectionedRecyclerViewAdapter sectionAdapter;
    JSONArray jsonArray;
    Date mdate;
    String year, day, monthName, date;
    Map<String, List<AsmSalesModel>> myListMap;
    Map<String, List<AsmSalesModel>> myStatusListMap;
    Map<String, List<AsmSalesModel>> myCalenderHashMap;
    SortedSet<String> keys;
    SortedSet<String> statuskeys;
    SortedSet<String> calenderDateKeys;
    List<AsmSalesModel> preSalesAsmModelList;
    List<AsmSalesModel> statusAsmModelList;
    List<AsmSalesModel> calenderAsmModelList;
    private RecyclerView.LayoutManager layoutManager;
    public AsmSalesAdapter mAdapter = null;
    Date selectedDate;
    CalendarView mCalendarView;
    View view_calender;
    private int yData[];
    private String xData[];
    PieChart pieChart;
    NestedScrollView scroll_view_follow_up;
    int hotSize, coldSize, warmSize;
    TextView tv_months;
    private SearchView searchView;
    private ProgressDialog dialog;
    private int pageValue;
    String pageStrval;

    private boolean isLoading = true;
    boolean isScrolling = false;
    int visibleItemCount,totalItemCount,pastVisibleItems,previous_total = 0;
    private int view_threshold = 30;

    int currentItem,totalItem,scroloutitemm;
    private BMHApplication app;


    public FollowUpFragment() {
        gson = new Gson();
        listType = new TypeToken<ArrayList<AsmSalesModel>>() {
        }.getType();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        TAB_NAME = getActivity().getString(R.string.tab_follow_up);
        app = (BMHApplication)getActivity().getApplicationContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_follow_up, container, false);
        dialog = new ProgressDialog(getContext());
        mCalendarView = view.findViewById(R.id.calendarView);
        tv_months = view.findViewById(R.id.tv_months);
        view_calender = view.findViewById(R.id.view_calender);
        scroll_view_follow_up = view.findViewById(R.id.scroll_view_follow_up);
        mRecyclerView = view.findViewById(R.id.recycler_view_follow_up);
        mCalendarView.setVisibility(View.GONE);
        selectedDate = Calendar.getInstance().getTime();
        sectionAdapter = new SectionedRecyclerViewAdapter();
        pageValue = 1;
        pageStrval = String.valueOf(pageValue);



        if (Connectivity.isConnected(getContext())) {
            dialog.show();
            try {
                bundle = ((SalesActivity) getActivity()).masterBundleFollowup;
                if (bundle != null) {
                    String responseString = bundle.getString("JSON_STRING");
                    JSONObject jsonObject = new JSONObject(responseString);
                    if (jsonObject.getBoolean("success") && jsonObject != null) {
                        parseJson(jsonObject);

                    }

                    dialog.dismiss();
                }
                else {
                    initOfflineData();
                    dialog.dismiss();
                }

            } catch (JSONException e) {
                e.getMessage();
            }

         /*   mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    visibleItemCount = layoutManager.getChildCount();
                    totalItemCount = layoutManager.getItemCount();
                    pastVisibleItems = ((LinearLayoutManager)recyclerView.getLayoutManager()).findFirstVisibleItemPosition();

                    if (dy>0){
                        if (isLoading){

                            if (totalItemCount>previous_total){
                                isLoading = false;
                                previous_total = totalItemCount;
                            }
                        }
                        if (!isLoading&&(totalItemCount-visibleItemCount)<=(pastVisibleItems+view_threshold)){
                            Toast.makeText(getContext(), "Scrolled done", Toast.LENGTH_SHORT).show();
                            Log.e("Scrolled Done","Scrolled Done");

                        }
                    }

                }
            });*/

        } else {
            initOfflineData();
            dialog.dismiss();
        }
        mContext = getActivity();

        mCalendarView.setFirstDayOfWeek(Calendar.SUNDAY);
        mCalendarView.setWeekDaysNamesColor(getActivity().getResources().getColor(R.color.black));
        mCalendarView.setCurrentDayCircleColor(getActivity().getResources().getColor(R.color.blue_color));
        mCalendarView.setSelectedDayCircleColor(getActivity().getResources().getColor(R.color.blue_color));
        mCalendarView.setTextColor(getActivity().getResources().getColor(R.color.black));
       mCalendarView.setOnMonthChangedListener(this);
        mCalendarView.setOnLoadEventsListener(this);
        mCalendarView.setOnDateSelectedListener(this);
        Calendar calendar = Calendar.getInstance();
        calendar.set(2005, Calendar.JANUARY, 1);
        //  mCalendarView.setMinimumDate(calendar.getTimeInMillis());

        calendar.set(2035, Calendar.OCTOBER, 1);
        //    mCalendarView.setMaximumDate(calendar.getTimeInMillis());
        pieChart = view.findViewById(R.id.pieChart);
        pieChart.getDescription().setText("");
        pieChart.getDescription().setTextSize(13);
        pieChart.setRotationEnabled(true);
        pieChart.setDrawHoleEnabled(false);
        pieChart.animateY(3000);
        yData = new int[3];
        yData[0] = hotSize;
        yData[1] = coldSize;
        yData[2] = warmSize;
        xData = new String[3];
        if (hotSize > 0) {
            xData[0] = "Hot";
        } else {
            xData[0] = "";
        }
        if (coldSize > 0) {
            xData[1] = "Cold";
        } else {
            xData[1] = "";
        }
        if (warmSize > 0) {
            xData[2] = "Warm";
        } else {
            xData[2] = "";
        }

        dialog.dismiss();



        return view;
    }

    private void initOfflineData() {
        asmFollowUpLeads = ((SalesActivity) getActivity()).mSpFollowUpList;
        mDetailsList = ((SalesActivity) getActivity()).mDetailsList;
        projectsList = ((SalesActivity) getActivity()).projectsMasterList;
        notInterestedList = ((SalesActivity) getActivity()).spNotInterestedList;
        closureList = ((SalesActivity) getActivity()).spClosureList;
        leadsList = ((SalesActivity) getActivity()).spLeadStatusList;
        brokerList = ((SalesActivity) getActivity()).spBrokersList;

        if (asmFollowUpLeads != null) {
            for (int i = 0; i < asmFollowUpLeads.size(); i++) {
                if (mDetailsList.get(i).getLead_type().equalsIgnoreCase("hot"))
                    hotList.add(asmFollowUpLeads.get(i));

                if (mDetailsList.get(i).getLead_type().equalsIgnoreCase("cold"))
                    coldList.add(asmFollowUpLeads.get(i));

                if (mDetailsList.get(i).getLead_type().equalsIgnoreCase("warm"))
                    warmList.add(asmFollowUpLeads.get(i));
            }
            hotSize = hotList.size();
            coldSize = coldList.size();
            warmSize = warmList.size();
        }
        setRecyclerViewAllLeadData();
    }


    @Override
    public void onMonthChanged(Calendar monthCalendar) {
        String year = String.valueOf(monthCalendar.get(Calendar.YEAR));
        String month = getMonthForIntNew(monthCalendar.get(Calendar.MONTH));
        tv_months.setText(month + " " + year);

        if (mCalendarView.isMonthScroll) {
            mCalendarView.setSelectedDayCircleColor(Color.TRANSPARENT);
            int day = monthCalendar.get(Calendar.DAY_OF_MONTH);
            int monthInt = (monthCalendar.get(Calendar.MONTH));
            int finalM = monthInt + 1;
            String stringDate;
            String stringMonth;
            if (String.valueOf(day).length() == 1) {
                stringDate = "0" + day;
            } else {
                stringDate = String.valueOf(day);
            }
            if (String.valueOf(finalM).length() == 1) {
                stringMonth = "0" + finalM;
            } else {
                stringMonth = String.valueOf(finalM);
            }
            String calendarDate = year + "-" + stringMonth + "-" + stringDate;
            if (calendarDate.length() <= 10) {
                sectionAdapter.removeAllSections();
                mCalendarView.mSelectedDayCirclePaint.setColor(getActivity().getResources().getColor(R.color.blue_color));
                setRecyclerViewCalenderDateData(calendarDate);
            }
            mCalendarView.isMonthScroll = false;
        }
    }
    String getMonthForIntNew(int num) {
        String month = "wrong";
        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] months = dfs.getMonths();

        if (num >= 0 && num <= 11) {
            month = months[num];
        }
        return month;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }


    @Override
    public void onDateSelected(String dayCalendar, @Nullable List<CalendarEvent> events) {
        if (dayCalendar.length() <= 10) {
            sectionAdapter.removeAllSections();
            //   mCalendarView.setCurrentDayCircleColor(Color.LTGRAY);
            mCalendarView.setSelectedDayCircleColor(getActivity().getResources().getColor(R.color.blue_color));
            setRecyclerViewCalenderDateData(dayCalendar);
        } else {
            setRecyclerViewAllLeadData();
        }
    }
    public void scroll() {
        mRecyclerView.getLayoutManager().scrollToPosition(2);
    }



@Override
    public List<? extends CalendarEvent> onLoadEvents(int year, int month) {
        // Fill by random events
        List<MyEvent> events = new ArrayList<>();
        Set<String> mFollowDateSet = new HashSet<>();
        String mFollowUpDate = "";
        ArrayList<AsmSalesModel> dateList = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);

        // Events count
        if (asmFollowUpLeads != null) {
            for (int i = 0; i < asmFollowUpLeads.size(); i++) {
                mFollowUpDate = asmFollowUpLeads.get(i).getDetails().getDate();
                mFollowDateSet.add(mFollowUpDate);
            }
            if (myCalenderHashMap != null && !myCalenderHashMap.isEmpty())
                myCalenderHashMap.clear();
            myCalenderHashMap = new ConcurrentHashMap<>();
            for (int i = 0; i < asmFollowUpLeads.size(); i++) {
                AsmSalesModel asmSalesModel = asmFollowUpLeads.get(i);
                String date = asmSalesModel.getDetails().getDate();
                List<AsmSalesModel> myObjectList = myCalenderHashMap.get(date);
                if (myObjectList == null) {
                    myObjectList = new ArrayList();
                    myCalenderHashMap.put(date, myObjectList);
                }
                myObjectList.add(asmSalesModel);
            }
            for (AsmSalesModel AsmSalesModel : asmFollowUpLeads) {
                String date = AsmSalesModel.getDetails().getDate();
                List<AsmSalesModel> myObjectList = myCalenderHashMap.get(date);
                if (myObjectList == null) {
                    myObjectList = new ArrayList();
                    myCalenderHashMap.put(date, myObjectList);
                }
                myObjectList.add(AsmSalesModel);
            }
            calenderDateKeys = new TreeSet<>(myCalenderHashMap.keySet());
            for (String key : calenderDateKeys) {
                calenderAsmModelList = myCalenderHashMap.get(key);
                ArrayList<AsmSalesModel> brokersList = new ArrayList<>();
                ArrayList<AsmSalesModel> customerList = new ArrayList<>();
                for (int i = 0; i < calenderAsmModelList.size(); i++) {
                    String isLeadType = calenderAsmModelList.get(i).getIs_lead_type();
                    if (isLeadType != null && isLeadType.equalsIgnoreCase("1")) {
                        brokersList.add(calenderAsmModelList.get(i));
                    } else {
                        customerList.add(calenderAsmModelList.get(i));
                    }
                }
                String[] strDateMonth = key.split(",");
                String dateMonth = strDateMonth[1];
                String[] dateMonthYear = dateMonth.trim().split(" ");

                String mMonth = dateMonthYear[0];
                String mDate = dateMonthYear[1];
                String mYear = dateMonthYear[2];

                calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(mDate));
                calendar.set(Calendar.MONTH, Utils.convertStringToInt(mMonth) - 1);
                calendar.set(Calendar.YEAR, Integer.parseInt(mYear));
                MyEvent event = new MyEvent(String.valueOf(customerList.size()), calendar.getTimeInMillis(), getRandomColor(), String.valueOf(brokersList.size()), asmFollowUpLeads, getActivity());
                events.add(event);
            }
        }
        return events;
    }

    public Comparator<AsmSalesModel> statusTime = new Comparator<AsmSalesModel>() {

        public int compare(AsmSalesModel s1, AsmSalesModel s2) {

            String date1 = s1.getScheduledatetime();
            String date2 = s2.getScheduledatetime();

            /*For ascending order*/
            return date1.compareTo(date2);
        }
    };

    private int getRandomColor() {
        Resources resources = getResources();

        int rand = (int) (Math.random() * 2);
        switch (rand) {
            case 0:
                return resources.getColor(R.color.red);
            case 1:
                return resources.getColor(R.color.blue);
        }

        return 0x000000;
    }

    public void setRecyclerViewCalenderDateData(String dayCalendar) {
        selectedDateList = new ArrayList<>();
        for (int i = 0; i < asmFollowUpLeads.size(); i++) {
            String completeDate = asmFollowUpLeads.get(i).getScheduledatetime();
            String keyDate[] = completeDate.split(" ");
            String newDate = keyDate[0];
            if (newDate.equalsIgnoreCase(dayCalendar))
                selectedDateList.add(asmFollowUpLeads.get(i));
        }

        if (selectedDateList.size() > 0) {
            myCalenderHashMap = new ConcurrentHashMap<>();
            for (AsmSalesModel AsmSalesModel : selectedDateList) {
                String status = AsmSalesModel.getIs_lead_type();
                List<AsmSalesModel> myObjectList = myCalenderHashMap.get(status);
                if (myObjectList == null) {
                    myObjectList = new ArrayList();
                    myCalenderHashMap.put(status, myObjectList);
                }
                myObjectList.add(AsmSalesModel);
            }
            calenderDateKeys = new TreeSet<>(myCalenderHashMap.keySet());
            for (String key : calenderDateKeys) {
                calenderAsmModelList = myCalenderHashMap.get(key);
                Collections.sort(calenderAsmModelList, statusTime);
                Collections.reverse(calenderAsmModelList);
                String status;
                if (key.equalsIgnoreCase("1")) {

                    status = "Broker";
                } else {
                    status = "Customer";
                }

                if (calenderAsmModelList.size() > 0) {

                    sectionAdapter.addSection(new RecyclerDataAdapter("", preSalesAsmModelList, status, calenderAsmModelList, mDetailsList, "status", projectsList, notInterestedList, closureList, leadsList, brokerList));
                }
            }
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            mRecyclerView.setAdapter(sectionAdapter);

        //    mRecyclerView.addOnScrollListener(new PaginationScrollListener);

        } else {
            sectionAdapter.notifyDataSetChanged();
            mRecyclerView.setAdapter(null);

        }

    }




    private void setRecyclerViewAllLeadData() {
        if (asmFollowUpLeads != null && asmFollowUpLeads.size() > 0) {
            //  Collections.sort(asmFollowUpLeads, Utils.lastUpdatedTime);
            layoutManager = new LinearLayoutManager(getActivity());
            mAdapter = new AsmSalesAdapter(getActivity(), asmFollowUpLeads, mDetailsList, projectsList, notInterestedList, closureList, leadsList, TAB_NAME, brokerList);
            mRecyclerView.setAdapter(mAdapter);
            mRecyclerView.setLayoutManager(layoutManager);









        } else {

            mRecyclerView.setBackgroundResource(R.drawable.no_lead_assigned);
        }




    }

    private void setRecyclerViewDateTimeData() {
        myListMap = new ConcurrentHashMap();
        for (AsmSalesModel preSalesAsmModel : asmFollowUpLeads) {
            String completeDate = preSalesAsmModel.getScheduledatetime();
            String keyDate[] = completeDate.split(" ");
            String newDate = keyDate[0];
            List<AsmSalesModel> myObjectList = myListMap.get(newDate);
            if (myObjectList == null) {
                myObjectList = new ArrayList();
                myListMap.put(newDate, myObjectList);
            }
            myObjectList.add(preSalesAsmModel);
        }

        keys = new TreeSet<>(myListMap.keySet());
        for (String key : keys) {

            preSalesAsmModelList = myListMap.get(key);
            Collections.sort(preSalesAsmModelList, statusTime);
            Collections.reverse(preSalesAsmModelList);
            String date = key;

            if (preSalesAsmModelList.size() > 0) {

                sectionAdapter.addSection(new RecyclerDataAdapter(String.valueOf(date), preSalesAsmModelList, "", statusAsmModelList, mDetailsList, "dateTime", projectsList, notInterestedList, closureList, leadsList, brokerList));

            }

        }

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(sectionAdapter);
    }

    private void setRecyclerViewStatusData() {

        myStatusListMap = new ConcurrentHashMap<>();
        for (AsmSalesModel preSalesAsmModel : asmFollowUpLeads) {
            String status = preSalesAsmModel.getStatus();
            List<AsmSalesModel> myObjectList = myStatusListMap.get(status);
            if (myObjectList == null) {

                myObjectList = new ArrayList();

                myStatusListMap.put(status, myObjectList);

            }

            myObjectList.add(preSalesAsmModel);

        }

        statuskeys = new TreeSet<>(myStatusListMap.keySet());
        for (String key : statuskeys) {
            statusAsmModelList = myStatusListMap.get(key);
            Collections.sort(statusAsmModelList, statusTime);
            Collections.reverse(statusAsmModelList);
            String status = key;

            if (statusAsmModelList.size() > 0) {
                sectionAdapter.addSection(new RecyclerDataAdapter("", preSalesAsmModelList, status, statusAsmModelList, mDetailsList, "status", projectsList, notInterestedList, closureList, leadsList, brokerList));
            }
        }
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mRecyclerView.setAdapter(sectionAdapter);

    }

    private void parseJson(JSONObject jsonObject) {

        jsonArray = jsonObject.optJSONArray("data");
        JSONArray projectArray = jsonObject.optJSONArray("projects");
        if (projectArray != null) {
            int projectLength = projectArray.length();
            for (int j = 0; j < projectLength; j++)
            {
                JSONObject obj = projectArray.optJSONObject(j);
                projectsList.add(new Projects(obj.optString("project_id"), obj.optString("project_name")));
            }
        }


        JSONArray arrayLead = jsonObject.optJSONArray("lead_status");
        if (arrayLead != null) {
            int leadLength = arrayLead.length();
            for (int j = 0; j < leadLength; j++) {
                JSONObject obj = arrayLead.optJSONObject(j);
                leadsList.add(new LeadStatus(obj.optString("disposition_id"), obj.optString("title")));
            }
        }

        JSONArray notInterestedLead = jsonObject.optJSONArray("not_interested");
        if (notInterestedLead != null) {
            int leadLength = notInterestedLead.length();
            for (int j = 0; j < leadLength; j++) {
                JSONObject obj = notInterestedLead.optJSONObject(j);
                notInterestedList.add(new NotInterestedLead(obj.optString("id"), obj.optString("title"), obj.optString("address")));
            }
        }

        JSONArray closureLead = jsonObject.optJSONArray("closure");
        if (closureLead != null) {
            int leadLength = closureLead.length();
            for (int j = 0; j < leadLength; j++) {
                JSONObject obj = closureLead.optJSONObject(j);
                closureList.add(new NotInterestedLead(obj.optString("id"), obj.optString("title"), obj.optString("address")));
            }
        }
        JSONArray brokersList = jsonObject.optJSONArray("brokers");
        if (brokersList != null) {
            int leadLength = brokersList.length();
            for (int j = 0; j < leadLength; j++) {
                JSONObject obj = brokersList.optJSONObject(j);
                brokerList.add(new NotInterestedLead(obj.optString("id"), obj.optString("title"), obj.optString("address")));
            }
        }
        if (asmFollowUpLeads != null && asmFollowUpLeads.size() > 0)
            asmFollowUpLeads.clear();
        if (jsonArray != null) {
            int arrayLength = jsonArray.length();
            for (int i = 0; i < arrayLength; i++) {
                String isLeadType = jsonArray.optJSONObject(i).optString("is_lead_type");
                //  int isLeadAccepted = jsonArray.optJSONObject(i).optInt("isLeadAccepted");
                String lastUpdatedOn = jsonArray.optJSONObject(i).optString("lastupdatedon");
                String scheduledDateTime = jsonArray.optJSONObject(i).optString("scheduledatetime");
                JSONObject object = jsonArray.optJSONObject(i).optJSONObject("details");
                ArrayList<Assign_To> assignList = new ArrayList<>();
                ArrayList<Projects> selectedProjList = new ArrayList<>();
                ArrayList<SubStatus> subStatusList = new ArrayList<>();

                String custMob = object.optString("Mobile_No");
                String custAlternateMob = object.optString("Alternate_Mobile_No");
                String enquiryId = object.optString("Enquiry_ID");
                String custName = object.optString("Name");
                String custEmail = object.optString("Email_ID");
                String campaignName = object.optString("Campaign");
                String campaignDate = object.optString("Campaign_Date");
                String budget = object.optString("Budget");
                String currentStatus = object.optString("Current_Status");
                String date = object.optString("date");
                String time = object.optString("time");
                String address = object.optString("Address");
                String projectName = object.optString("Project_Name");
                String remark = object.optString("remark");
                //     String addressType = object.optString("address_type");
                String leadType = object.optString("lead_type");
                String budget_min = object.optString("budget_min");
                String budget_max = object.optString("budget_max");
                String broker_name = object.optString("broker_name");
                int noOfPersons = object.optInt("no_of_persons");

                JSONArray selectedList = object.optJSONArray("selected_project");
                if (selectedList != null) {
                    int selectedLength = selectedList.length();
                    for (int j = 0; j < selectedLength; j++) {
                        JSONObject obj = selectedList.optJSONObject(j);
                        selectedProjList.add(new Projects(obj.optString("project_id"), obj.optString("project_name")));
                    }
                }
                JSONArray subStatus = object.optJSONArray("subStatus");
                if (subStatus != null) {
                    int selectedLength = subStatus.length();
                    for (int j = 0; j < selectedLength; j++) {
                        JSONObject obj = subStatus.optJSONObject(j);
                        subStatusList.add(new SubStatus(obj.optString("id"), obj.optString("title")));
                    }
                }

                JSONArray array = object.optJSONArray("Assign_To");
                if (array != null) {
                    int assignLength = array.length();
                    for (int j = 0; j < assignLength; j++) {
                        JSONObject obj = array.optJSONObject(j);
                        assignList.add(new Assign_To(obj.optString("salesperson_id"), obj.optString("salesperson_name")));
                    }
                }
                mDetailsList.add(new AsmSalesLeadDetailModel(enquiryId, campaignName, campaignDate, custName, custEmail, custMob, custAlternateMob,
                        budget, currentStatus, scheduledDateTime, date, time, "", "", "", address,
                        selectedProjList, subStatusList, remark, leadType
                        , noOfPersons, broker_name, isLeadType, "", "", "", "", "", ""
                        , "", lastUpdatedOn));
            }
            mAsmList = gson.fromJson(jsonArray.toString(), listType);
            for (int i = 0; i < mAsmList.size(); i++) {
                if (mAsmList.get(i).getIsAssigned() == 1)
                    asmFollowUpLeads.add(mAsmList.get(i));
                if (mAsmList.get(i).getDetails().getLead_type().equalsIgnoreCase("hot"))
                    hotList.add(mAsmList.get(i));
                if (mAsmList.get(i).getDetails().getLead_type().equalsIgnoreCase("cold"))
                    coldList.add(mAsmList.get(i));
                if (mAsmList.get(i).getDetails().getLead_type().equalsIgnoreCase("warm"))
                    warmList.add(mAsmList.get(i));
            }
            hotSize = hotList.size();
            coldSize = coldList.size();
            warmSize = warmList.size();
         //   setRecyclerViewAllLeadData();

           // if (asmFollowUpLeads != null && asmFollowUpLeads.size() > 0) {
                //  Collections.sort(asmFollowUpLeads, Utils.lastUpdatedTime);
                layoutManager = new LinearLayoutManager(getActivity());
                mAdapter = new AsmSalesAdapter(getActivity(), asmFollowUpLeads, mDetailsList, projectsList, notInterestedList, closureList, leadsList, TAB_NAME, brokerList);
                mRecyclerView.setAdapter(mAdapter);
                mRecyclerView.setLayoutManager(layoutManager);




                mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);


                        visibleItemCount = layoutManager.getChildCount();
                        totalItemCount = layoutManager.getItemCount();
                        pastVisibleItems = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();


                        if (dy>0){

                            if (isLoading){

                                if (totalItemCount>previous_total){
                                    isLoading = false;
                                    previous_total = totalItemCount;
                                }
                            }

                            if ((visibleItemCount  + pastVisibleItems) >= totalItemCount){
                                isLoading = false;
                                Toast.makeText(getContext(), "Scrolled Done...Data Loading", Toast.LENGTH_SHORT).show();
                                Log.e("Scrolled Done", "Scrolled Done");
                                pageValue++;

                                performPaggination();
                                dialog.dismiss();
                            }
                        }

                    }
                });


            } else {
            Utils.showToast(getActivity(), getActivity().getResources().getString(R.string.txt_no_data_found));
        }
    }

   /* @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        if(requestCode==2)
        {
            //String message=data.getStringExtra("MESSAGE");
           // textView1.setText(message);
        }
    }*/



    private void parseJsonWithList(JSONObject jsonObject) {
        jsonArray = jsonObject.optJSONArray("data");
        JSONArray projectArray = jsonObject.optJSONArray("projects");
        if (projectArray != null) {
            int projectLength = projectArray.length();
            for (int j = 0; j < projectLength; j++)
            {
                JSONObject obj = projectArray.optJSONObject(j);
                projectsList.add(new Projects(obj.optString("project_id"), obj.optString("project_name")));

            }
        }


        JSONArray arrayLead = jsonObject.optJSONArray("lead_status");
        if (arrayLead != null) {
            int leadLength = arrayLead.length();
            for (int j = 0; j < leadLength; j++) {
                JSONObject obj = arrayLead.optJSONObject(j);
                leadsList.add(new LeadStatus(obj.optString("disposition_id"), obj.optString("title")));
            }
        }
        JSONArray notInterestedLead = jsonObject.optJSONArray("not_interested");
        if (notInterestedLead != null) {
            int leadLength = notInterestedLead.length();
            for (int j = 0; j < leadLength; j++) {
                JSONObject obj = notInterestedLead.optJSONObject(j);
                notInterestedList.add(new NotInterestedLead(obj.optString("id"), obj.optString("title"), obj.optString("address")));
            }
        }

        JSONArray closureLead = jsonObject.optJSONArray("closure");
        if (closureLead != null) {
            int leadLength = closureLead.length();
            for (int j = 0; j < leadLength; j++) {
                JSONObject obj = closureLead.optJSONObject(j);
                closureList.add(new NotInterestedLead(obj.optString("id"), obj.optString("title"), obj.optString("address")));
            }
        }
        JSONArray brokersList = jsonObject.optJSONArray("brokers");
        if (brokersList != null) {
            int leadLength = brokersList.length();
            for (int j = 0; j < leadLength; j++) {
                JSONObject obj = brokersList.optJSONObject(j);
                brokerList.add(new NotInterestedLead(obj.optString("id"), obj.optString("title"), obj.optString("address")));
            }
        }
        if (asmFollowUpLeads != null && asmFollowUpLeads.size() > 0)
            asmFollowUpLeads.clear();
        if (jsonArray != null) {
            int arrayLength = jsonArray.length();
            for (int i = 0; i < arrayLength; i++) {
                String isLeadType = jsonArray.optJSONObject(i).optString("is_lead_type");
                //  int isLeadAccepted = jsonArray.optJSONObject(i).optInt("isLeadAccepted");
                String lastUpdatedOn = jsonArray.optJSONObject(i).optString("lastupdatedon");
                String scheduledDateTime = jsonArray.optJSONObject(i).optString("scheduledatetime");
                JSONObject object = jsonArray.optJSONObject(i).optJSONObject("details");
                ArrayList<Assign_To> assignList = new ArrayList<>();
                ArrayList<Projects> selectedProjList = new ArrayList<>();
                ArrayList<SubStatus> subStatusList = new ArrayList<>();

                String custMob = object.optString("Mobile_No");
                String custAlternateMob = object.optString("Alternate_Mobile_No");
                String enquiryId = object.optString("Enquiry_ID");
                String custName = object.optString("Name");
                String custEmail = object.optString("Email_ID");
                String campaignName = object.optString("Campaign");
                String campaignDate = object.optString("Campaign_Date");
                String budget = object.optString("Budget");
                String currentStatus = object.optString("Current_Status");
                String date = object.optString("date");
                String time = object.optString("time");
                String address = object.optString("Address");
                String projectName = object.optString("Project_Name");
                String remark = object.optString("remark");
                //     String addressType = object.optString("address_type");
                String leadType = object.optString("lead_type");
                String budget_min = object.optString("budget_min");
                String budget_max = object.optString("budget_max");
                String broker_name = object.optString("broker_name");
                int noOfPersons = object.optInt("no_of_persons");

                JSONArray selectedList = object.optJSONArray("selected_project");
                if (selectedList != null) {
                    int selectedLength = selectedList.length();
                    for (int j = 0; j < selectedLength; j++) {
                        JSONObject obj = selectedList.optJSONObject(j);
                        selectedProjList.add(new Projects(obj.optString("project_id"), obj.optString("project_name")));
                    }
                }
                JSONArray subStatus = object.optJSONArray("subStatus");
                if (subStatus != null) {
                    int selectedLength = subStatus.length();
                    for (int j = 0; j < selectedLength; j++) {
                        JSONObject obj = subStatus.optJSONObject(j);
                        subStatusList.add(new SubStatus(obj.optString("id"), obj.optString("title")));
                    }
                }

                JSONArray array = object.optJSONArray("Assign_To");
                if (array != null) {
                    int assignLength = array.length();
                    for (int j = 0; j < assignLength; j++) {
                        JSONObject obj = array.optJSONObject(j);
                        assignList.add(new Assign_To(obj.optString("salesperson_id"), obj.optString("salesperson_name")));
                    }
                }
                mDetailsList.add(new AsmSalesLeadDetailModel(enquiryId, campaignName, campaignDate, custName, custEmail, custMob, custAlternateMob,
                        budget, currentStatus, scheduledDateTime, date, time, "", "", "", address,
                        selectedProjList, subStatusList, remark, leadType
                        , noOfPersons, broker_name, isLeadType, "", "", "", "", "", ""
                        , "", lastUpdatedOn));
            }
            mAsmList = gson.fromJson(jsonArray.toString(), listType);
            for (int i = 0; i < mAsmList.size(); i++) {
                if (mAsmList.get(i).getIsAssigned() == 1)
                    asmFollowUpLeads.add(mAsmList.get(i));
                if (mAsmList.get(i).getDetails().getLead_type().equalsIgnoreCase("hot"))
                    hotList.add(mAsmList.get(i));
                if (mAsmList.get(i).getDetails().getLead_type().equalsIgnoreCase("cold"))
                    coldList.add(mAsmList.get(i));
                if (mAsmList.get(i).getDetails().getLead_type().equalsIgnoreCase("warm"))
                    warmList.add(mAsmList.get(i));
            }
            hotSize = hotList.size();
            coldSize = coldList.size();
            warmSize = warmList.size();
          //  setRecyclerViewAllLeadData();

           /// if (asmFollowUpLeads != null && asmFollowUpLeads.size() > 0) {
                //  Collections.sort(asmFollowUpLeads, Utils.lastUpdatedTime);
               // layoutManager = new LinearLayoutManager(getActivity());
              //  mRecyclerView.setLayoutManager(layoutManager);
              //  mAdapter = new AsmSalesAdapter(getActivity(), asmFollowUpLeads, mDetailsList, projectsList, notInterestedList, closureList, leadsList, TAB_NAME, brokerList);
                mAdapter.addSalesData(asmFollowUpLeads, mDetailsList, projectsList, notInterestedList, closureList, leadsList, TAB_NAME, brokerList);

//            }

            } else {
            Utils.showToast(getActivity(), getActivity().getResources().getString(R.string.txt_no_data_found));
        }
    }

    @Override
    public void setHasOptionsMenu(boolean hasMenu) {
        super.setHasOptionsMenu(hasMenu);
    }

    @Override
    public void onDestroyOptionsMenu() {
        super.onDestroyOptionsMenu();
        if (searchView != null &&
                !searchView.getQuery().toString().isEmpty()) {

            searchView.setIconified(true);
            searchView.setIconified(true);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.sales_menu_item, menu);
        MenuItem search = menu.findItem(R.id.action_search);
        searchView = (SearchView) search.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                searchView.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                final List<AsmSalesModel> filteredModelList = Utils.filter(asmFollowUpLeads, s.toLowerCase());
                mAdapter.setFilter(filteredModelList);
                return true;
            }
        });

        search.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem menuItem) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                mAdapter.setFilter(asmFollowUpLeads);
                return true;
            }
        });
        if (!search.isActionViewExpanded()) {
            if (asmFollowUpLeads != null) {
                if (mAdapter == null)
                    mAdapter = new AsmSalesAdapter(getActivity(), asmFollowUpLeads, mDetailsList, projectsList, notInterestedList, closureList, leadsList, TAB_NAME, brokerList);
                Collections.sort(asmFollowUpLeads, Utils.lastUpdatedTime);
                mAdapter.setFilter(asmFollowUpLeads);

            }
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.menu_default:
                scroll_view_follow_up.setVisibility(View.VISIBLE);
                tv_months.setVisibility(View.GONE);
                mCalendarView.setVisibility(View.GONE);
                view_calender.setVisibility(View.GONE);
                pieChart.setVisibility(View.GONE);
                sectionAdapter.removeAllSections();
                ((SalesActivity) Objects.requireNonNull(getActivity())).getSupportActionBar().setTitle(getString(R.string.txt_toolbar_sales, "All Leads"));
                setRecyclerViewAllLeadData();
                break;
            case R.id.menu_date_time:
                tv_months.setVisibility(View.GONE);
                scroll_view_follow_up.setVisibility(View.VISIBLE);
                mCalendarView.setVisibility(View.GONE);
                view_calender.setVisibility(View.GONE);
                pieChart.setVisibility(View.GONE);
                sectionAdapter.removeAllSections();
                ((SalesActivity) Objects.requireNonNull(getActivity())).getSupportActionBar().setTitle(getString(R.string.txt_toolbar_sales, "Date and Time"));
                item.setIcon(R.drawable.app_icon);
                setRecyclerViewDateTimeData();
                break;
            case R.id.menu_status:
                tv_months.setVisibility(View.GONE);
                scroll_view_follow_up.setVisibility(View.VISIBLE);
                mCalendarView.setVisibility(View.GONE);
                view_calender.setVisibility(View.GONE);
                pieChart.setVisibility(View.GONE);
                sectionAdapter.removeAllSections();
                ((SalesActivity) Objects.requireNonNull(getActivity())).getSupportActionBar().setTitle(getString(R.string.txt_toolbar_sales, "Status"));
                setRecyclerViewStatusData();
                break;
            case R.id.menu_item_calender:
                tv_months.setVisibility(View.VISIBLE);
                scroll_view_follow_up.setVisibility(View.VISIBLE);
                mCalendarView.setVisibility(View.VISIBLE);
                view_calender.setVisibility(View.VISIBLE);
                pieChart.setVisibility(View.GONE);
                sectionAdapter.removeAllSections();
                Date todayDate = Calendar.getInstance().getTime();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                String todayString = formatter.format(todayDate);
                //mCalendarView.setDate(Calendar.getInstance().getTimeInMillis(), false, true);
                setRecyclerViewCalenderDateData(todayString);
                break;
            case R.id.heat_map:
                tv_months.setVisibility(View.GONE);
                scroll_view_follow_up.setVisibility(View.GONE);
                pieChart.setVisibility(View.VISIBLE);
                sectionAdapter.removeAllSections();
                addPieData();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

   /* == if (menu_status.equalsIgnoreCase("1")){

        setRecyclerViewStatusData();

          }*/

    /* private void setRecyclerViewCalenderDateData(String dayCalendar) {
         selectedDateList = new ArrayList<>();
         for (int i = 0; i < mAsmList.size(); i++) {
             String completeDate = mAsmList.get(i).getDate_And_Time();
             String keyDate[] = completeDate.split(" ");
             String newDate = keyDate[0];
             if (newDate.equalsIgnoreCase(dayCalendar))
                 selectedDateList.add(mAsmList.get(i));
         }
         myCalenderHashMap = new HashMap<>();
         for (AsmSalesModel preSalesAsmModel : selectedDateList) {
             String status = preSalesAsmModel.getIs_lead_type();
             List<AsmSalesModel> myObjectList = myCalenderHashMap.get(status);
             if (myObje  ctList == null) {
                 myObjectList = new ArrayList();
                 myCalenderHashMap.put(status, myObjectList);
             }
             myObjectList.add(preSalesAsmModel);
         }
         calenderDateKeys = new TreeSet<>(myCalenderHashMap.keySet());
         for (String key : calenderDateKeys) {
             calenderAsmModelList = myCalenderHashMap.get(key);
             Collections.sort(calenderAsmModelList, statusTime);
             String status;
             if (key.equalsIgnoreCase("0")) {
                 status = "Customer";


             } else {
                 status = "Broker";
             }

             if (calenderAsmModelList.size() > 0) {
               //  sectionAdapter.addSection(new RecyclerDataAdapter("", preSalesAsmModelList, status, calenderAsmModelList, mDetailsList, "status", projectsList, notInterestedList, closureList, leadsList));
             }
         }
         mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
         mRecyclerView.setAdapter(sectionAdapter);

             }
 */
    private class RecyclerDataAdapter extends StatelessSection {

        private String parentDate;
        private List<AsmSalesModel> preSalesAsmModelList;
        private String status;
        private List<AsmSalesModel> statusAsmModelList;
        private AsmSalesModel mAsmModel;
        boolean expanded = false;
        private AsmSalesLeadDetailModel mDetailsModel;
        private ArrayList<AsmSalesLeadDetailModel> mDetailsList;
        private Calendar cal;
        int pos = 0;
        String groupBy;
        ArrayList<NotInterestedLead> mNotInterestedLeadsList;
        ArrayList<NotInterestedLead> mClosureList;
        ArrayList<Projects> mProjectsList;
        ArrayList<LeadStatus> mLeadsList;
        ArrayList<NotInterestedLead> mBrokerList;

        RecyclerDataAdapter(String newDate, List<AsmSalesModel> list, String status,
                            List<AsmSalesModel> statusList, ArrayList<AsmSalesLeadDetailModel> detailsList,
                            String groupBy, ArrayList<Projects> projectsList,
                            ArrayList<NotInterestedLead> notInterestedLeadsList,
                            ArrayList<NotInterestedLead> closureList,
                            ArrayList<LeadStatus> leadsList, ArrayList<NotInterestedLead> brokerList) {
            super(SectionParameters.builder()
                    .itemResourceId(R.layout.lead_list_item_child)
                    .headerResourceId(R.layout.item_parent_child_listing)
                    .build());

            this.parentDate = newDate;

           // this.parentDate = mAsmModel.getScheduledatetime();
            this.status = status;
            this.groupBy = groupBy;
            mProjectsList = projectsList;
            mLeadsList = leadsList;
            mNotInterestedLeadsList = notInterestedLeadsList;
            mClosureList = closureList;
            this.mBrokerList = brokerList;
            this.preSalesAsmModelList = list;
            this.statusAsmModelList = statusList;
            this.mDetailsList = detailsList;
        }

        @Override
        public int getContentItemsTotal()

        {
            if (groupBy.equalsIgnoreCase("dateTime")) {
                return expanded ? preSalesAsmModelList.size() : 0;

            } else {
                return expanded ? statusAsmModelList.size() : 0;

            }

        }

        @Override
        public RecyclerView.ViewHolder getItemViewHolder(View view) {
            return new ItemViewHolder(view);
        }

        @Override
        public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
            final ItemViewHolder itemHolder = (ItemViewHolder) holder;
            if (groupBy.equalsIgnoreCase("dateTime")) {
                mAsmModel = preSalesAsmModelList.get(position);
            } else {
                mAsmModel = statusAsmModelList.get(position);
            }
            StringUtil.createColoredProfileName(mAsmModel.getCustomer_name(), itemHolder.iv_user_img, mAsmModel);
            String campaignName = mAsmModel.getCampaign_name();
            String projectName = mAsmModel.getProject_name();
            if ((!TextUtils.isEmpty(campaignName) || !campaignName.equals("")) && (!TextUtils.isEmpty(projectName) || !projectName.equals(""))) {
                itemHolder.tv_campaign.setText(mContext.getString(R.string.txt_campaign_project, campaignName, projectName));
            } else if ((!TextUtils.isEmpty(campaignName) || !campaignName.equals("")) && (TextUtils.isEmpty(projectName) || projectName.equals(""))) {
                itemHolder.tv_campaign.setText(mContext.getString(R.string.txt_campaign_name, campaignName));
            } else {

                itemHolder.tv_campaign.setVisibility(View.GONE);
            }
            itemHolder.tv_sales_person.setVisibility(View.GONE);
            itemHolder.tv_enquiry_id.setText(mContext.getString(R.string.txt_enquiry_id, mAsmModel.getEnquiry_id()));
            itemHolder.tv_customer_name.setText(mAsmModel.getCustomer_name());
            itemHolder.tv_mobile_no.setText(mAsmModel.getCustomer_mobile());

        /*
          UN-ASSIGNED TAB ITEM LIST
         */

            if (mAsmModel.getIsAssigned() == 0) {
                itemHolder.assigned_view.setVisibility(View.GONE);
                itemHolder.unassigned_view.setVisibility(View.VISIBLE);
                if (TextUtils.isEmpty(mAsmModel.getCustomer_email())) {
                    itemHolder.tv_email_id.setVisibility(View.GONE);
                } else {

                    itemHolder.tv_email_id.setText(mAsmModel.getCustomer_email());
                    itemHolder.tv_date_time.setText(mAsmModel.getScheduledatetime());
                }

            }

        /*
          ASSIGNED TAB ITEM LIST
         */
            else {
                itemHolder.assigned_view.setVisibility(View.VISIBLE);
                itemHolder.unassigned_view.setVisibility(View.GONE);
                itemHolder.tv_current_status.setText(mContext.getString(R.string.tx_status, mAsmModel.getStatus()));

                if (mAsmModel.getStatus().equalsIgnoreCase("meeting")) {
                    itemHolder.tv_last_updated_on.setText(mContext.getString(R.string.txt_meeting_date_time, mAsmModel.getScheduledatetime()));
                } else if (mAsmModel.getStatus().equalsIgnoreCase("site visit")) {
                    itemHolder.tv_last_updated_on.setText(mContext.getString(R.string.txt_site_visit_date_time, mAsmModel.getScheduledatetime()));
                } else {
                    itemHolder.tv_last_updated_on.setText(mContext.getString(R.string.txt_call_back_date_time, mAsmModel.getScheduledatetime()));
                }
            }

            if (mAsmModel.getIs_lead_type().equalsIgnoreCase("1"))
                          {
                itemHolder.imageView_history.setVisibility(View.GONE);
                itemHolder.button_overdue.setVisibility(View.GONE);
                      }

            itemHolder.tv_mobile_no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (((SalesActivity) mContext).checkPermissions(mAsmModel.getCustomer_mobile())) {
                            ((SalesActivity) mContext).actionCall(mAsmModel.getCustomer_mobile());
                        } else {
                            ((SalesActivity) mContext).requestPermissions();
                        }
                    } else {
                        ((SalesActivity) mContext).actionCall(mAsmModel.getCustomer_mobile());
                    }
                }


            });


            itemHolder.imageView_history.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    Intent historyIntent = new Intent(mContext, AsmHistoryActivity.class);
                    historyIntent.putExtra(BMHConstants.ENQUIRY_ID, mAsmModel.getEnquiry_id());
                    mContext.startActivity(historyIntent);
                  ////  startActivityForResult(historyIntent,2);

                }


            });



            itemHolder.rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)

                {

                    int position = sectionAdapter
                            .getPositionInSection(itemHolder.getAdapterPosition()); // gets item position
                    if (position != RecyclerView.NO_POSITION) { // Check if an item was deleted, but the user clicked it before the UI removed it
                        if (groupBy.equalsIgnoreCase("dateTime")) {
                            mAsmModel = preSalesAsmModelList.get(position);
                        } else {
                            mAsmModel = statusAsmModelList.get(position);
                        }
                        for (int i = 0; i < mDetailsList.size(); i++) {
                            if (mDetailsList.get(i).getEnquiry_ID().equals(mAsmModel.getEnquiry_id())) {
                                mDetailsModel = mDetailsList.get(i);
                                break;
                            }

                        }

                        Intent intent = new Intent(mContext, FollowUpSalesDetailActivity.class);
                        intent.putExtra(BMHConstants.ASM_MODEL_DATA, mAsmModel);
                        intent.putExtra(BMHConstants.ASM_DETAIL_DATA, mDetailsModel);
                        intent.putExtra(BMHConstants.SELECTED_TAB_NAME, mContext.getResources().getString(R.string.tab_follow_up));
                        intent.putParcelableArrayListExtra(BMHConstants.PROJECT_LIST, mProjectsList);
                        intent.putParcelableArrayListExtra(BMHConstants.LEAD_LIST, mLeadsList);
                        intent.putParcelableArrayListExtra(BMHConstants.NOT_INTERESTED_LIST, mNotInterestedLeadsList);
                        intent.putParcelableArrayListExtra(BMHConstants.CLOSURE_LIST, mClosureList);
                        intent.putParcelableArrayListExtra(BMHConstants.BROKER_LIST, mBrokerList);
                        intent.putExtra("path", "adapter");
                        mContext.startActivity(intent);
                    }

                }
            });
        }

        @Override
        public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
            return new HeaderViewHolder(view);
        }

        @Override

        public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder) {
            final HeaderViewHolder headerHolder = (HeaderViewHolder) holder;
            if (groupBy.equalsIgnoreCase("dateTime")) {
//                SimpleDateFormat formatter = new SimpleDateFormat(dateFormat_yyymmdd);
                SimpleDateFormat formatter = new SimpleDateFormat(dateFormat_yyymmdd_test);
                //String dtStart = "11/08/2013 08:48:10";
                try {
                             mdate = formatter.parse(parentDate);

                    // mdate = formatter.parse(dtStart);

                    cal = Calendar.getInstance();
                    cal.setTime(mdate);
                    year = String.valueOf(cal.get(Calendar.YEAR));
                    monthName = Utils.getMonthFromInt(cal.get(Calendar.MONTH));
                    date = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
                    day = Utils.getDayName(cal.get(Calendar.DAY_OF_WEEK));

                    headerHolder.tvTitle.setText(day + ", " + date + " " + monthName + " " +
                            year + "(" + preSalesAsmModelList.size() + ")");
                   // headerHolder.tvTitle.setText("12-04-20");


                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else {
                headerHolder.tvTitle.setText(status + "(" + statusAsmModelList.size() + ")");
            }
            headerHolder.rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    expanded = !expanded;
                    headerHolder.imgArrow.setImageResource(
                            expanded ? R.drawable.minus : R.drawable.plus
                    );
                    scroll();
                    sectionAdapter.notifyDataSetChanged();
                }
            });
        }
    }

    private class HeaderViewHolder extends RecyclerView.ViewHolder {

        private TextView tvTitle;
        private View rootView;
        private ImageView imgArrow;

        HeaderViewHolder(View view) {
            super(view);
            rootView = view;
            tvTitle = view.findViewById(R.id.tv_parentName);
            imgArrow = view.findViewById(R.id.iv_up_down);
        }
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

             ConstraintLayout layout;
           LinearLayout assigned_view, unassigned_view;
          ImageView iv_user_img;
        TextView tv_enquiry_id;
        TextView tv_campaign;
        TextView tv_customer_name;
        TextView tv_mobile_no;
        TextView tv_email_id;
        TextView tv_date_time;
        TextView tv_current_status;
        TextView tv_sales_person;
        TextView tv_last_updated_on;
        Button button_overdue;
        ImageView imageView_history;
        //    ImageView imageView_mike;
        private View rootView;

        ItemViewHolder(View convertView) {
            super(convertView);
            rootView = convertView;
            layout = convertView.findViewById(R.id.sp_asm_row);
            assigned_view = convertView.findViewById(R.id.assigned_view);
            unassigned_view = convertView.findViewById(R.id.unassigned_view);
            iv_user_img = convertView.findViewById(R.id.iv_user_img);
            tv_enquiry_id = convertView.findViewById(R.id.tv_enquiry_id);
            tv_campaign = convertView.findViewById(R.id.tv_campaign);
            tv_customer_name = convertView.findViewById(R.id.tv_customer_name);
            tv_mobile_no = convertView.findViewById(R.id.tv_mobile_no);
            tv_email_id = convertView.findViewById(R.id.tv_email_id);
            tv_date_time = convertView.findViewById(R.id.tv_date_time);
            tv_current_status = convertView.findViewById(R.id.tv_current_status);
            tv_sales_person = convertView.findViewById(R.id.tv_sales_person);
            tv_last_updated_on = convertView.findViewById(R.id.tv_last_updated_on);
            button_overdue = convertView.findViewById(R.id.button_overdue);
            imageView_history = convertView.findViewById(R.id.imageView_history);

            imageView_history.setOnClickListener(this);


            //   imageView_mike = convertView.findViewById(R.id.imageView_mike);

        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition(); // gets item position
            if (position != RecyclerView.NO_POSITION) { // Check if an item was deleted, but the user clicked it before the UI removed it
                mAsmModel = mAsmList.get(position);

                for (int i = 0; i < mDetailsList.size(); i++) {

                    if (mDetailsList.get(i).getEnquiry_ID().equalsIgnoreCase(mAsmModel.getEnquiry_id())) {
                        mDetailsModel = mDetailsList.get(i);
                        break;

                    }

                }


                switch (view.getId()) {
                    case R.id.button_overdue:
                        break;
                    case R.id.imageView_history:
                        Intent historyIntent = new Intent(mContext, AsmHistoryActivity.class);
                        historyIntent.putExtra(BMHConstants.ENQUIRY_ID, mAsmModel.getEnquiry_id());
                        mContext.startActivity(historyIntent);
                        break;
                    case R.id.tv_mobile_no:
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (((SalesActivity) mContext).checkPermissions(mAsmModel.getCustomer_mobile())) {
                                ((SalesActivity) mContext).actionCall(mAsmModel.getCustomer_mobile());
                            } else {
                                ((SalesActivity) mContext).requestPermissions();
                            }
                        } else {
                            ((SalesActivity) mContext).actionCall(mAsmModel.getCustomer_mobile());
                        }

                        break;
                    default:

                        Toast.makeText(getContext(), "gjhgkjhgk", Toast.LENGTH_SHORT).show();
                }


            }
        }
    }


    private void addPieData() {
        ArrayList<PieEntry> yEntry = new ArrayList<>();
        for (int i = 0; i < yData.length; i++) {
            yEntry.add(new PieEntry(yData[i], xData[i]));
        }

        PieDataSet pieDataSet = new PieDataSet(yEntry, "");
        pieDataSet.setValueTextSize(15);
        pieDataSet.setValueFormatter(new MyValueFormatter());
        pieDataSet.setValueTextColor(Objects.requireNonNull(getActivity()).getResources().getColor(R.color.white));


        ArrayList<Integer> color = new ArrayList<>();
        color.add(Color.BLUE);
        color.add(Color.GREEN);
        color.add(Color.RED);

        pieDataSet.setColors(color);
        pieChart.getLegend().setEnabled(false);


        /*Legend legend = pieChart.getLegend();
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setPosition(Legend.LegendPosition.ABOVE_CHART_LEFT);
        legend.setDrawInside(false);*/

        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.invalidate();
    }








    private void performPaggination()
    {
        pageStrval = String.valueOf(pageValue);
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);
      //  if (!isFinish())
        dialog.show();
        String basee_url = "http://services.bookmyhouse.com/saas_api/android/salesperson/api_v.1.2/sales/getFollowupLeads.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, basee_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response != null) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.getBoolean("success")) {
                                    if (jsonObject.optString("message").equalsIgnoreCase("No Data Found")) {
                                        //Toast.makeText(getContext(), jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    parseJsonWithList(jsonObject);

                                    Toast.makeText(getContext(), "The Page Number"+pageStrval, Toast.LENGTH_SHORT).show();


                                  /*  if (customerDb != null)
                                        insertMastersToDb(jsonObject);
                                    // List<CustomerInfoEntity> mList= mDbInstance.getCustomerDao().getAll();
                                    masterBundle = new Bundle();
                                    masterBundle.putString("JSON_STRING", response);
                                    Utils.showToast(SalesActivity.this, getString(R.string.txt_server_data_synced));
                               */   //  prepareTabs();
                                    dialog.dismiss();
                                }
                                else {
                                    Toast.makeText(getContext(), jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {

                            Toast.makeText(getContext(), getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (dialog != null && dialog.isShowing())
                        {
                            dialog.dismiss();
                        }
                        Log.d("TAG", "VolleyError " + error);
                    }
                }
        )

        {
            @Override












            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(ParamsConstants.BUILDER_ID, BMHConstants.CURRENT_BUILDER_ID);
                 params.put(ParamsConstants.USER_ID, app.getFromPrefs(BMHConstants.USERID_KEY));
                params.put(ParamsConstants.USER_DESIGNATION, "0");
                params.put("page", pageStrval);
                //params.put("searchkey", "kirti");
                return params;
            }

            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap();
                headers.put("Content-Type", WEBAPI.contentTypeFormData);
                return headers;
            }
        };
        BMHApplication.getInstance().addToRequestQueue(stringRequest, "headerRequest");


    }


}

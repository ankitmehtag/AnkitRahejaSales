package com.fragments;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.activities.SalesActivity;
import com.adapters.AsmSalesAdapter;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.appnetwork.WEBAPI;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.helper.BMHConstants;
import com.helper.ParamsConstants;
import com.interfaces.OnMenuItemClickListener;
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
import com.utils.MyDividerItemDecoration;
import com.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class AssignedFragment extends Fragment {
    public static final String TAG = AssignedFragment.class.getSimpleName();
    private RecyclerView mRecyclerView;
    private static String TAB_NAME = null;
    Bundle bundle;
    private Gson gson;
    private Type listType;
    private ArrayList<AsmSalesModel> asmAssignedLeadsList = new ArrayList<>();
    private JSONArray jsonArray;
    ArrayList<Projects> projectsList = new ArrayList<>();
    ArrayList<LeadStatus> leadsList = new ArrayList<>();
    ArrayList<NotInterestedLead> notInterestedList = new ArrayList<>();
    ArrayList<NotInterestedLead> closureList = new ArrayList<>();
    private ArrayList<AsmSalesLeadDetailModel> mDetailsList = new ArrayList<>();
    private ArrayList<AsmSalesModel> mAsmList;
    private RecyclerView.LayoutManager layoutManager;
    public AsmSalesAdapter mAdapter = null;
    private SearchView searchView;
    OnMenuItemClickListener clickListener;
    private Context context;
    MyListAdapter myListAdapter;
    private ProgressDialog dialog;
    private BMHApplication app;
    private int pageValue =1;
    String pageStrval;
    ProgressBar progressBar;


    private boolean isLoading = true;
    int visibleItemCount,totalItemCount,pastVisibleItems,previous_total = 0;
    private int view_threshold = 10;


    public AssignedFragment() {
        gson = new Gson();
        listType = new TypeToken<ArrayList<AsmSalesModel>>() {
         }.getType();
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        clickListener = (OnMenuItemClickListener) context;
        this.context = context;
    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        TAB_NAME = getActivity().getString(R.string.tab_assigned);

        app = (BMHApplication)getActivity().getApplicationContext();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_assigned, container, false);
        mRecyclerView = rootView.findViewById(R.id.recyclerViewAssigned);
        pageValue =1;
        dialog = new ProgressDialog(getContext());
        progressBar = (ProgressBar)rootView.findViewById(R.id.simpleProgressBar);


           return rootView;

    }

    @Override
    public void onPause() {
        super.onPause();
        Bundle bundle = new Bundle();
        onSaveInstanceState(bundle
        );



    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (Connectivity.isConnected(getContext())) {
            try {
                bundle = ((SalesActivity) Objects.requireNonNull(getActivity())).masterBundleAssigned;
                if (bundle != null) {
                    String responseString = bundle.getString("JSON_STRING");
                    JSONObject jsonObject = new JSONObject(responseString);
                    if (jsonObject.getBoolean("success") && jsonObject != null) {
                        parseJson(jsonObject);
                    }
                }
                else {
                    initOfflineData();
                }
            }
            catch (JSONException e) {
                e.getMessage();
            }
        } else {
            initOfflineData();
        }
        if (asmAssignedLeadsList != null && asmAssignedLeadsList.size() > 0) {
            mAdapter = new AsmSalesAdapter(getActivity(), asmAssignedLeadsList, mDetailsList, projectsList, notInterestedList, closureList, leadsList, TAB_NAME, null);

            mRecyclerView.setAdapter(mAdapter);
            mRecyclerView.addItemDecoration(new MyDividerItemDecoration(Objects.requireNonNull(context), LinearLayoutManager.VERTICAL, 5));
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
            layoutManager = new LinearLayoutManager(getActivity());
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

                        if (((visibleItemCount  + pastVisibleItems) >= totalItemCount)){
                            /// dialog.show();
                         //   isLoading = true;
                            isLoading = false;
                            // Toast.makeText(getContext(), "Scrolled Done...Data Loading", Toast.LENGTH_SHORT).show();
                            Log.e("Scrolled Done", "Scrolled Done");
                            pageValue++;

                            performPaggination();
                            //  dialog.dismiss();

                        }
                    }
                }
            });
        } else {
            mRecyclerView.setBackgroundResource(R.drawable.no_lead_assigned);

        }


    }

    private void initOfflineData() {
        asmAssignedLeadsList = ((SalesActivity) getActivity()).mSpAssignedList;
        mDetailsList = ((SalesActivity) getActivity()).mDetailsList;
        projectsList = ((SalesActivity) getActivity()).projectsMasterList;
        notInterestedList = ((SalesActivity) getActivity()).spNotInterestedList;
        closureList = ((SalesActivity) getActivity()).spClosureList;
        leadsList = ((SalesActivity) getActivity()).spLeadStatusList;
        //  brokerList = ((SalesActivity) getActivity()).spBrokersList;
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
    public void onPrepareOptionsMenu(Menu menu) {

        MenuItem syncMenu = menu.findItem(R.id.action_sync);
        if (Connectivity.isConnected(getContext())) {
            //  syncMenu.setVisible(true);
            syncMenu.setIcon(R.drawable.ic_action_sync);
        } else {
            //  syncMenu.setVisible(false);
            syncMenu.setIcon(R.drawable.ic_signal_cellular_off);
        }
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_pre_sales, menu);
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
                final List<AsmSalesModel> filteredModelList = Utils.filter(asmAssignedLeadsList, s.toLowerCase());
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
                mAdapter.setFilter(asmAssignedLeadsList);
                return true;
            }
        });
        if (!search.isActionViewExpanded()) {
            if (asmAssignedLeadsList != null) {
                if (mAdapter == null)
                    mAdapter = new AsmSalesAdapter(getActivity(), asmAssignedLeadsList, mDetailsList, projectsList, notInterestedList, closureList, leadsList, TAB_NAME, null);
                mAdapter.setFilter(asmAssignedLeadsList);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_sync) {
            clickListener.onMenuClick(item, TAG);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void parseJson(JSONObject jsonObject) {
        jsonArray = jsonObject.optJSONArray("data");
        JSONArray projectArray = jsonObject.optJSONArray("projects");
        if (projectArray != null) {
            int projectLength = projectArray.length();
            for (int j = 0; j < projectLength; j++) {
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
        if (asmAssignedLeadsList != null && asmAssignedLeadsList.size() > 0)
            asmAssignedLeadsList.clear();
        if (jsonArray != null) {
            int arrayLength = jsonArray.length();
            for (int i = 0; i < arrayLength; i++) {

                String isLeadType = jsonArray.optJSONObject(i).optString("is_lead_type");
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
                int noOfPersons = object.optInt("no_of_persons");
                String broker_name = object.optString("broker_name");
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
                mDetailsList.add(new AsmSalesLeadDetailModel(enquiryId, campaignName, campaignDate, custName, custEmail, custMob,
                        custAlternateMob, budget, currentStatus, scheduledDateTime, date, time, "", "",
                        "", address, selectedProjList, subStatusList, remark, leadType, noOfPersons,
                        broker_name, isLeadType, "", "", "", "", "",
                        "", "", lastUpdatedOn));

                /// TODO TOMORROW
            }
            mAsmList = gson.fromJson(jsonArray.toString(), listType);
            for (int i = 0; i < mAsmList.size(); i++) {
                if (mAsmList.get(i).getIsAssigned() == 0)
                    asmAssignedLeadsList.add(mAsmList.get(i));
            }
        } else {
            Utils.showToast(getActivity(), getActivity().getResources().getString(R.string.txt_no_data_found));
        }
    }

    /*private void setRecyclerViewClosureData() {
          Collections.sort(asmAssignedLeadsList, statusTime);
          Collections.reverse(asmAssignedLeadsList);
        mAdapter = new AsmSalesAdapter(getActivity(), asmAssignedLeadsList, mDetailsList, projectsList, notInterestedList, closureList, leadsList, TAB_NAME, null);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new MyDividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL, 5));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
    }

    public Comparator<AsmSalesModel> statusTime = new Comparator<AsmSalesModel>() {

        public int compare(AsmSalesModel s1, AsmSalesModel s2) {

            String date1 = s1.getUpdate_date();
            String date2 = s2.getUpdate_date();

            *//*For ascending order*//*
            return date1.compareTo(date2);
        }
    };*/





    public class MyListAdapter  extends RecyclerView.Adapter<MyListAdapter.ViewHolder>{
      //  private MyListData[] listdata;
        ArrayList<String> listt;

        // RecyclerView recyclerView;
        public MyListAdapter( ArrayList<String> list) {
            this.listt = list;
        }
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View listItem= layoutInflater.inflate(R.layout.list_layout_test, parent, false);
            ViewHolder viewHolder = new ViewHolder(listItem);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            final  String myListData = listt.get(position);
          //  holder.textView.setText(listdata[position].getDescription());
           // holder.imageView.setImageResource(listdata[position].getImgId());
          /*  holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(view.getContext(),"click on item: "+myListData.getDescription(),Toast.LENGTH_LONG).show();
                }
            });*/
        }


        @Override
        public int getItemCount() {
            return listt.size();
        }

        public  class ViewHolder extends RecyclerView.ViewHolder {
       //     public ImageView imageView;
            public TextView textView;
          //  public RelativeLayout relativeLayout;
            public ViewHolder(View itemView) {
                super(itemView);
          //      this.imageView = (ImageView) itemView.findViewById(R.id.imageView);
                this.textView = (TextView) itemView.findViewById(R.id.textTest);
              //  relativeLayout = (RelativeLayout)itemView.findViewById(R.id.relative);
            }
        }

    }

     void performPaggination()
    {
        progressBar.setVisibility(View.VISIBLE);

        pageStrval = String.valueOf(pageValue);
        //dialog.setMessage("Please wait...");
       // dialog.setCancelable(false);
        //  if (!isFinish())
       // dialog.show();



        //  String basee_url = "http://services.bookmyhouse.com/saas_api/android/salesperson/api_v.1.2/sales/getFollowupLeads.php";
        String basee_url_assigned =   "http://services.bookmyhouse.com/saas_api/android/salesperson/api_v.1.2/sales/getAssignedLeads.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, basee_url_assigned,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response != null) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.getBoolean("success")) {
                                    if (jsonObject.optString("message").equalsIgnoreCase("No Data Found")) {
                                       // Toast.makeText(getContext(), jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
                                       // dialog.dismiss();

                                       // return;
                                        return;
                                    }
                                  //  parseJsonWithList(jsonObject);
                                    parseJsonWithpagedata(jsonObject);


                                 //   parseJson(jsonObject);

                                  //  Toast.makeText(getContext(), "The Page Number"+pageStrval, Toast.LENGTH_SHORT).show();
                                  /*  if (customerDb != null)
                                        insertMastersToDb(jsonObject);
                                    // List<CustomerInfoEntity> mList= mDbInstance.getCustomerDao().getAll();
                                    masterBundle = new Bundle();
                                    masterBundle.putString("JSON_STRING", response);
                                    Utils.showToast(SalesActivity.this, getString(R.string.txt_server_data_synced));
                               */   //  prepareTabs();
                                  //  dialog.dismiss();
                                    progressBar.setVisibility(View.GONE);
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
                        if (dialog != null && dialog.isShowing()) {
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

    private void parseJsonWithpagedata(JSONObject jsonObject) {

     /*   progressDoalog = new ProgressDialog(getContext());
        progressDoalog.setMax(100);
        progressDoalog.setMessage("Data loading....");
        // progressDoalog.setTitle("ProgressDialog bar example");
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        progressDoalog.show();*/

        jsonArray = jsonObject.optJSONArray("data");
        JSONArray projectArray = jsonObject.optJSONArray("projects");
        if (projectArray != null) {
            int projectLength = projectArray.length();
            for (int j = 0; j < projectLength; j++) {
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
        if (asmAssignedLeadsList != null && asmAssignedLeadsList.size() > 0)
            asmAssignedLeadsList.clear();
        if (jsonArray != null) {
            int arrayLength = jsonArray.length();
            for (int i = 0; i < arrayLength; i++) {

                String isLeadType = jsonArray.optJSONObject(i).optString("is_lead_type");
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
                int noOfPersons = object.optInt("no_of_persons");
                String broker_name = object.optString("broker_name");
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

                mDetailsList.add(new AsmSalesLeadDetailModel(enquiryId, campaignName, campaignDate, custName, custEmail, custMob,
                        custAlternateMob, budget, currentStatus, scheduledDateTime, date, time, "", "",
                        "", address, selectedProjList, subStatusList, remark, leadType, noOfPersons,
                        broker_name, isLeadType, "", "", "", "", "",
                        "", "", lastUpdatedOn));
                /// TODO TOMORROW
            }
            mAsmList = gson.fromJson(jsonArray.toString(), listType);
            for (int i = 0; i < mAsmList.size(); i++) {
                if (mAsmList.get(i).getIsAssigned() == 0)
                    asmAssignedLeadsList.add(mAsmList.get(i));
            }

           mAdapter.addSalesData( asmAssignedLeadsList, mDetailsList, projectsList, notInterestedList, closureList, leadsList, TAB_NAME, null);

          //  progressDoalog.dismiss();

        } else {
            Utils.showToast(getActivity(), getActivity().getResources().getString(R.string.txt_no_data_found));
        }
    }




}

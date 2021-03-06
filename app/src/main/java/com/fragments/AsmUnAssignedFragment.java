package com.fragments;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.activities.PreSalesActivity;
import com.adapters.PreSalesAsmAdapter;
import com.database.entity.PreSalesLeadDetailsEntity;
import com.database.task.InsertPreSalesLeadDetailsTask;
import com.database.task.InsertMultiSelectTask;
import com.database.entity.SelectMultipleProjectsEntity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.interfaces.OnLeadSaveToDbListener;
import com.interfaces.OnMenuItemClickListener;
import com.model.Assign_To;
import com.model.Details;
import com.model.LeadStatus;
import com.model.PreSalesAsmModel;
import com.model.Projects;
import com.sp.R;
import com.utils.Connectivity;
import com.utils.MyDividerItemDecoration;
import com.utils.StringUtil;
import com.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class AsmUnAssignedFragment extends Fragment {

    public static final String TAG = AsmUnAssignedFragment.class.getSimpleName();
    private static String TAB_NAME = null;
    private ArrayList<PreSalesAsmModel> mAsmList;
    private ArrayList<PreSalesAsmModel> mAsmUnAssignedList = new ArrayList<>();
    private List<PreSalesLeadDetailsEntity> mPreSalesLeadDetailList = new ArrayList<>();
    private ArrayList<Details> mDetailsList = new ArrayList<>();
    private RecyclerView.LayoutManager layoutManager;
    public PreSalesAsmAdapter mAdapter = null;
    private Gson gson;
    private Type listType;
    private RecyclerView recyclerView;
    private SearchView searchView;
    Bundle mBundle;
    OnMenuItemClickListener clickListener;
    OnLeadSaveToDbListener leadSaveToDbListener;
    private Context context;

    public AsmUnAssignedFragment() {
        gson = new Gson();
        listType = new TypeToken<ArrayList<PreSalesAsmModel>>() {
        }.getType();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        clickListener = (OnMenuItemClickListener) context;
        leadSaveToDbListener = (OnLeadSaveToDbListener) context;
        this.context = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        TAB_NAME = getActivity().getString(R.string.tab_un_assigned);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_my_transactions, container, false);
        recyclerView = rootView.findViewById(R.id.recyclerView);
        if (Connectivity.isConnected(getContext()))
        {
            try {
                mBundle = ((PreSalesActivity) getActivity()).masterBundle;
                if (mBundle != null) {
                    String responseString = mBundle.getString("JSON_STRING");
                    JSONObject jsonObject = new JSONObject(responseString);
                    if (jsonObject.getBoolean("success") && jsonObject != null) {
                        parseJson(jsonObject);
                    }
                } else {
                    initOfflineData();
                }
            } catch (JSONException e) {
                e.getMessage();
            }
        } else {
            initOfflineData();
        }
        initRecyclerView();
        return rootView;
    }

    private void initOfflineData() {
        mDetailsList = ((PreSalesActivity) getActivity()).mDetailsList1;
        mAsmUnAssignedList = ((PreSalesActivity) getActivity()).mAsmUnAssignedList1;
    }

    private void initRecyclerView() {
        if (mAsmUnAssignedList != null && mAsmUnAssignedList.size() > 0) {
            Collections.sort(mAsmUnAssignedList, Utils.sortByLastUpdateAsm);
            mAdapter = new PreSalesAsmAdapter(getContext(), mAsmUnAssignedList, mDetailsList, TAB_NAME);
            recyclerView.setAdapter(mAdapter);
            recyclerView.addItemDecoration(new MyDividerItemDecoration(Objects.requireNonNull(context), LinearLayoutManager.VERTICAL, 5));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            layoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(layoutManager);
        } else {
            recyclerView.setBackgroundResource(R.drawable.no_unassigned_lead_available);
        }
    }

    private void parseJson(JSONObject jsonObject) throws JSONException {
        JSONArray jsonArray = jsonObject.optJSONArray("data");
        if (mAsmUnAssignedList != null && mAsmUnAssignedList.size() > 0)
            mAsmUnAssignedList.clear();
        JSONObject objLeads = null;
        if (jsonArray != null) {
            int arrayLength = jsonArray.length();
            for (int i = 0; i < arrayLength; i++) {
                int isAssigned = jsonArray.optJSONObject(i).optInt("isAssigned");
                String assignedTo = jsonArray.optJSONObject(i).optString("salesperson_name");
                String lastUpdatedOn = jsonArray.optJSONObject(i).optString("lastupdatedon");
                String scheduledDateTime = jsonArray.optJSONObject(i).optString("scheduledatetime");
                JSONObject object = jsonArray.optJSONObject(i).optJSONObject("details");
                ArrayList<Assign_To> assignList = new ArrayList<>();
                ArrayList<LeadStatus> leadsList = new ArrayList<>();
                ArrayList<Projects> projectsList = new ArrayList<>();
                ArrayList<Projects> selectedProjList = new ArrayList<>();

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
                String leadType = object.optString("lead_type");
                String budget_min = object.optString("budget_min");
                String budget_max = object.optString("budget_max");
                int noOfPersons = object.optInt("no_of_persons");


                JSONArray projectArray = object.optJSONArray("Project_List");
                if (projectArray != null) {
                    int projectLength = projectArray.length();
                    for (int j = 0; j < projectLength; j++) {
                        JSONObject obj = projectArray.optJSONObject(j);
                        projectsList.add(new Projects(obj.optString("project_id"), obj.optString("project_name")));
                    }
                }
                JSONArray selectedList = object.optJSONArray("selected_project");
                if (selectedList != null) {
                    int selectedLength = selectedList.length();
                    for (int j = 0; j < selectedLength; j++) {
                        JSONObject obj = selectedList.optJSONObject(j);
                        selectedProjList.add(new Projects(obj.optString("project_id"), obj.optString("project_name")));
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
                JSONArray arrayLead = object.optJSONArray("lead_status");
                if (arrayLead != null) {

                    int leadLength = arrayLead.length();
                    for (int j = 0; j < leadLength; j++) {
                        objLeads = arrayLead.optJSONObject(j);
                        leadsList.add(new LeadStatus(objLeads.optString("disposition_id"), objLeads.optString("title")));
                    }
                }
               /* JSONObject locObject = object.optJSONObject("location");
                locListValue.add(locObject.optString("office"));
                locListValue.add(locObject.optString("title"));
                locListValue.add(locObject.optString("other"));

                JSONArray locArray = locObject.names();
                for (int j = 0; j < locArray.length(); j++) {
                    locListKey.add(locArray.get(j).toString());
                }
              */
                mDetailsList.add(new Details(enquiryId, campaignName, campaignDate, custName, custEmail, custMob, custAlternateMob,
                        budget, currentStatus, scheduledDateTime, date, time, address, projectName, projectsList, selectedProjList,
                        assignList, leadsList, remark, leadType, budget_min, budget_max, noOfPersons, lastUpdatedOn));


              /*  if (currentStatus.equalsIgnoreCase(getResources().getStringArray(R.array.leads_array)[3])) {
                    CallbackEntity callbackEntity = new CallbackEntity(enquiryId, "",
                            scheduledDateTime, date, time, "", remark, leadType, 0,
                            "", lastUpdatedOn);
                    new InsertCallbackTask(callbackEntity).execute();

                }
                if (currentStatus.equalsIgnoreCase(getResources().getStringArray(R.array.leads_array)[4])) {
                    MeetingEntity meetingEntity = new MeetingEntity(Long.getLong(""), enquiryId, currentStatus, time,
                            date, address, leadType, remark, scheduledDateTime, Utils.getCurrentDateTime(), 0,
                            "", lastUpdatedOn);
                    new InsertMeetingTask(meetingEntity).execute();

                }
                if (currentStatus.equalsIgnoreCase(getResources().getStringArray(R.array.leads_array)[5])) {
                    SiteVisitEntity siteVisitEntity = new SiteVisitEntity(enquiryId, currentStatus, "",
                            date, time, address, leadType, String.valueOf(noOfPersons), remark, "",
                            "", scheduledDateTime, Utils.getCurrentDateTime(), "", 0,
                            "", lastUpdatedOn);
                    new InsertSiteVisitTask(siteVisitEntity).execute();
                }*/

                //     List<SelectMultipleProjectsEntity> multiList = new ArrayList<>();
                int size = selectedProjList.size();
                if (size > 0) {
                    StringBuilder builderProId = new StringBuilder();
                    StringBuilder builderProName = new StringBuilder();
                    for (int j = 0; j < size; j++) {
                        builderProId.append(selectedProjList.get(j).getProject_id()).append(",");
                        builderProName.append(selectedProjList.get(j).getProject_name()).append(",");
                    }
                    SelectMultipleProjectsEntity multiProjEntity = new SelectMultipleProjectsEntity(enquiryId,
                            StringUtil.removeLastComma(builderProId.toString()),
                            StringUtil.removeLastComma(builderProName.toString()), 0, "",
                            lastUpdatedOn);
                    new InsertMultiSelectTask(multiProjEntity, 0).execute();
                }

                PreSalesLeadDetailsEntity preSalesLeadDetailsEntity = new PreSalesLeadDetailsEntity(enquiryId, custName, custMob,
                        custEmail, custAlternateMob, Utils.getLeadStatusId(leadsList, currentStatus),
                        currentStatus, scheduledDateTime, Utils.getSelectedMultiProjectIds(selectedProjList), remark,
                        budget, date, time, budget_min, budget_max,
                        "", assignedTo, Utils.getAssignToId(assignList, assignedTo), isAssigned, campaignName, campaignDate, 0,
                        0, 0, lastUpdatedOn, getString(R.string.txt_asm), "");
                // new InsertPreSalesLeadDetailsTask(getActivity(), preSalesLeadDetailsEntity).execute();
                mPreSalesLeadDetailList.add(preSalesLeadDetailsEntity);
            }
            new InsertPreSalesLeadDetailsTask(getActivity(), mPreSalesLeadDetailList).execute();
            leadSaveToDbListener.callbackInsertLead(true);
            mAsmList = gson.fromJson(jsonArray.toString(), listType);
            for (int i = 0; i < mAsmList.size(); i++) {
                if (mAsmList.get(i).getIsAssigned() == 0) {
                    mAsmUnAssignedList.add(mAsmList.get(i));
                }
            }
        } else {
            Utils.showToast(getActivity(), getString(R.string.txt_no_data_found));
        }
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
        //  super.onPrepareOptionsMenu(menu);
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
                final List<PreSalesAsmModel> filteredModelList = filter(mAsmUnAssignedList, s.toLowerCase());
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
                mAdapter.setFilter(mAsmUnAssignedList);
                return true;
            }
        });
        if (!search.isActionViewExpanded()) {
            if (mAdapter != null)
                mAdapter.setFilter(mAsmUnAssignedList);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_sync) {
            // MySyncAdapter.initializeSyncAdapter(BMHApplication.getInstance());
            clickListener.onMenuClick(item, TAG);
            return true;
        }
        return super.onOptionsItemSelected(item);
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

    private List<PreSalesAsmModel> filter(List<PreSalesAsmModel> models, String query) {
        query = query.toLowerCase();
        final List<PreSalesAsmModel> filteredModelList = new ArrayList<>();
        for (PreSalesAsmModel model : models) {
            final String customerName = model.getCustomer_name();
            final String custName = (TextUtils.isEmpty(customerName) ? "" : customerName.toLowerCase());
            final String campaign = model.getCampaign_name();
            final String campaignName = (TextUtils.isEmpty(campaign) ? "" : campaign.toLowerCase());
            final String email = (model.getCustomer_email()).toLowerCase();
            final String mobile = (model.getCustomer_mobile()).toLowerCase();
            final String enquiryId = (model.getEnquiry_id()).toLowerCase();
            final String status = (model.getStatus()).toLowerCase();

            if (custName.contains(query) || campaignName.contains(query) || email.contains(query) ||
                    enquiryId.contains(query) || mobile.contains(query) || status.contains(query)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }
}

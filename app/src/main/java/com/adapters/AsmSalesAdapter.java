package com.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.activities.AsmHistoryActivity;
import com.activities.FollowUpSalesDetailActivity;
import com.activities.ClosureSalesDetailActivity;
import com.activities.SalesActivity;
import com.activities.AssignedSalesDetailActivity;
import com.helper.BMHConstants;
import com.model.AsmSalesLeadDetailModel;
import com.model.AsmSalesModel;
import com.model.LeadStatus;
import com.model.NotInterestedLead;
import com.model.Projects;
import com.sp.R;
import com.utils.Connectivity;
import com.utils.StringUtil;
import com.utils.Utils;
import com.utils.WordUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AsmSalesAdapter extends RecyclerView.Adapter<AsmSalesAdapter.ViewHolder> {

    private Context mContext;
    private String tabName;
    private ArrayList<AsmSalesModel> mAsmList;
    private ArrayList<AsmSalesLeadDetailModel> mDetailsList;
    ArrayList<NotInterestedLead> mNotInterestedLeadsList;
    ArrayList<NotInterestedLead> mClosureList;
    ArrayList<Projects> mProjectsList;
    ArrayList<LeadStatus> mLeadsList;
    private AsmSalesModel mAsmModel;
    private AsmSalesLeadDetailModel mDetailsModel;
    private SimpleDateFormat simpleDateFormat;
    private Date scheduledDate;
    private String dateInString;
    private ArrayList<NotInterestedLead> mBrokerList;

    public AsmSalesAdapter(Context context, ArrayList<AsmSalesModel> asmList,
                           ArrayList<AsmSalesLeadDetailModel> detailsList, ArrayList<Projects> projectsList,
                           ArrayList<NotInterestedLead> notInterestedLeadsList,
                           ArrayList<NotInterestedLead> closureList,
                           ArrayList<LeadStatus> leadsList, String tabName, ArrayList<NotInterestedLead> brokerList) {
        mContext = context;
        mAsmList = asmList;
        mDetailsList = detailsList;
        mProjectsList = projectsList;
        mLeadsList = leadsList;
        mNotInterestedLeadsList = notInterestedLeadsList;
        mClosureList = closureList;
        this.mBrokerList = brokerList;
        this.tabName = tabName;
        simpleDateFormat = new SimpleDateFormat(Utils.dateFormat1, Locale.getDefault());

    }


    @NonNull
    @Override
    public AsmSalesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.lead_list_item_child, parent, false);
       return new AsmSalesAdapter.ViewHolder(v);


                 }

    @Override
    public void onBindViewHolder(@NonNull AsmSalesAdapter.ViewHolder holder, int position) {
        if (getItemCount() <= 0)
            return;

        mAsmModel = mAsmList.get(position);
        //   Utils.imageUpdate(mContext, holder.iv_user_img, UrlFactory.IMG_BASEURL + mAsmModel.g());
        String campaignName = mAsmModel.getCampaign_name();
        String projectName = mAsmModel.getProject_name();
        if ((!TextUtils.isEmpty(campaignName) || !campaignName.equals("")) && (TextUtils.isEmpty(projectName) || projectName.equals(""))) {
            holder.tv_campaign.setText(mContext.getString(R.string.txt_campaign_name, campaignName));
        } else if ((!TextUtils.isEmpty(campaignName) || !campaignName.equals("")) && (!TextUtils.isEmpty(projectName) || !projectName.equals(""))) {
            holder.tv_campaign.setText(mContext.getString(R.string.txt_campaign_project, campaignName, projectName));
        } else
                  {
            holder.tv_campaign.setVisibility(View.GONE);

                        }

        holder.tv_enquiry_id.setText(mContext.getString(R.string.txt_enquiry_id, mAsmModel.getEnquiry_id()));
        holder.tv_customer_name.setText(WordUtils.capitalize(mAsmModel.getCustomer_name().toLowerCase()));
        StringUtil.createColoredProfileName(mAsmModel.getCustomer_name(), holder.iv_user_img, mAsmModel);

        holder.tv_mobile_no.setText(mAsmModel.getCustomer_mobile());
        holder.tv_current_status.setText(mContext.getString(R.string.tx_status, mAsmModel.getStatus()));
        if (Connectivity.isConnected(mContext))
            holder.imageView_history.setVisibility(View.VISIBLE);
        else
            holder.imageView_history.setVisibility(View.GONE);
        /*
          ASSIGNED TAB ITEM LIST
         */
        if (mAsmModel.getIsAssigned() == 0) {
            holder.assigned_view.setVisibility(View.VISIBLE);
            holder.unassigned_view.setVisibility(View.GONE);
            holder.tv_sales_person.setVisibility(View.GONE);
            holder.tv_last_updated_on.setText(mContext.getString(R.string.txt_last_updated_on, mAsmModel.getScheduledatetime()));
            holder.tv_current_status.setText(mContext.getString(R.string.tx_status, mAsmModel.getStatus()));
            if (mAsmModel.getStatus().equalsIgnoreCase("meeting")) {
                holder.tv_last_updated_on.setText(mContext.getString(R.string.txt_meeting_date_time, mAsmModel.getScheduledatetime()));
            }
                else if (mAsmModel.getStatus().equalsIgnoreCase("site visit")) {
                holder.tv_last_updated_on.setText(mContext.getString(R.string.txt_site_visit_date_time, mAsmModel.getScheduledatetime()));
            }
                 else {
                holder.tv_last_updated_on.setText(mContext.getString(R.string.txt_call_back_date_time, mAsmModel.getScheduledatetime()));
            }
        }
        /*
          FOLLOWUP TAB ITEM LIST
         */
        else {
            holder.assigned_view.setVisibility(View.VISIBLE);
            holder.unassigned_view.setVisibility(View.GONE);
            holder.tv_sales_person.setVisibility(View.GONE);
            holder.tv_last_updated_on.setText(mContext.getString(R.string.txt_last_updated_on, mAsmModel.getScheduledatetime()));
            holder.tv_current_status.setText(mContext.getString(R.string.tx_status, mAsmModel.getStatus()));
            if (mAsmModel.getStatus().equalsIgnoreCase("meeting")) {
                holder.tv_last_updated_on.setText(mContext.getString(R.string.txt_meeting_date_time, mAsmModel.getScheduledatetime()));
            } else if (mAsmModel.getStatus().equalsIgnoreCase("site visit")) {
                holder.tv_last_updated_on.setText(mContext.getString(R.string.txt_site_visit_date_time, mAsmModel.getScheduledatetime()));
            } else {
                holder.tv_last_updated_on.setText(mContext.getString(R.string.txt_call_back_date_time, mAsmModel.getScheduledatetime()));
            }
            if (mAsmModel.getIs_lead_type() != null && mAsmModel.getIs_lead_type().equalsIgnoreCase(BMHConstants.LEAD_TYPE_BROKER)) {
                holder.imageView_history.setVisibility(View.GONE);
                holder.button_overdue.setVisibility(View.GONE);
                holder.tv_mobile_no.setEnabled(false);
                return;
            }
            if (tabName.equalsIgnoreCase(mContext.getString(R.string.tab_closure))) {
                holder.tv_last_updated_on.setVisibility(View.GONE);
                holder.tv_current_status.setText(mAsmModel.getProject_name());
                holder.tv_mobile_no.setEnabled(true);
            } else {
                holder.tv_mobile_no.setEnabled(true);
            }
        }
        try {
            dateInString = mAsmModel.getScheduledatetime();
            if (!TextUtils.isEmpty(dateInString) || !dateInString.equals("") || !dateInString.equalsIgnoreCase("NA")) {
                scheduledDate = simpleDateFormat.parse(dateInString);
                if (System.currentTimeMillis() > scheduledDate.getTime()) {
                    holder.button_overdue.setVisibility(View.VISIBLE);
                } else {
                    holder.button_overdue.setVisibility(View.GONE);
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    @Override
    public int getItemCount() {
        if (mAsmList != null)
            return mAsmList.size();
        return 0;
    }

    public void setFilter(List<AsmSalesModel> model) {
        mAsmList = new ArrayList<>();
        mAsmList.addAll(model);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
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

        public ViewHolder(View convertView) {
            super(convertView);
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

            button_overdue.setOnClickListener(this);
            imageView_history.setOnClickListener(this);
            tv_mobile_no.setOnClickListener(this);
            layout.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition(); // gets item position
            if (position != RecyclerView.NO_POSITION) { // Check if an item was deleted, but the user clicked it before the UI removed it
                mAsmModel = mAsmList.get(position);

                for (int i = 0; i < mDetailsList.size(); i++) {

                    if (mDetailsList.get(i).getEnquiry_ID().equalsIgnoreCase(mAsmModel.getEnquiry_id())) {
                        mDetailsModel = mDetailsList.get(i);
                        break;

                           }

                            }

                    switch (v.getId()) {
                    case R.id.button_overdue:
                               break;

                    case R.id.imageView_history:
                        Intent historyIntent = new Intent(mContext, AsmHistoryActivity.class);
                        historyIntent.putExtra(BMHConstants.ENQUIRY_ID, mAsmModel.getEnquiry_id());
                       // Bundle b = new Bundle();
                       // b.putString("tababc",tabName);

                        historyIntent.putExtra("TabName", tabName);
                       // mContext.startActivity(historyIntent);
                        ((Activity) mContext).startActivityForResult(historyIntent,2);
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
                        if (tabName.equalsIgnoreCase(mContext.getString(R.string.tab_assigned))) {
                            Intent intent = new Intent(mContext, AssignedSalesDetailActivity.class);
                            intent.putExtra(BMHConstants.ASM_MODEL_DATA, mAsmModel);
                            intent.putExtra(BMHConstants.ASM_DETAIL_DATA, mDetailsModel);
                            intent.putExtra(BMHConstants.SELECTED_TAB_NAME, tabName);
                            intent.putExtra(BMHConstants.PATH, "adapter");
                            mContext.startActivity(intent);
                        } else if (tabName.equalsIgnoreCase(mContext.getString(R.string.tab_closure))) {
                            Intent intent = new Intent(mContext, ClosureSalesDetailActivity.class);
                            intent.putExtra(BMHConstants.SELECTED_TAB_NAME, tabName);
                            intent.putExtra(BMHConstants.ENQUIRY_ID, mAsmModel.getEnquiry_id());
                            mContext.startActivity(intent);
                        } else {
                            Intent intent = new Intent(mContext, FollowUpSalesDetailActivity.class);
                            intent.putExtra(BMHConstants.ASM_MODEL_DATA, mAsmModel);
                            intent.putExtra(BMHConstants.PATH, "adapter");
                            intent.putExtra(BMHConstants.ASM_DETAIL_DATA, mDetailsModel);
                            intent.putExtra(BMHConstants.SELECTED_TAB_NAME, tabName);
                            intent.putParcelableArrayListExtra(BMHConstants.PROJECT_LIST, mProjectsList);
                            intent.putParcelableArrayListExtra(BMHConstants.LEAD_LIST, mLeadsList);
                            intent.putParcelableArrayListExtra(BMHConstants.NOT_INTERESTED_LIST, mNotInterestedLeadsList);
                            intent.putParcelableArrayListExtra(BMHConstants.CLOSURE_LIST, mClosureList);
                            intent.putParcelableArrayListExtra(BMHConstants.BROKER_LIST, mBrokerList);
                            mContext.startActivity(intent);
                        }
                        break;
                }
            }
        }
    }

    public void addSalesData( ArrayList<AsmSalesModel> asmList,
                              ArrayList<AsmSalesLeadDetailModel> detailsList, ArrayList<Projects> projectsList,
                              ArrayList<NotInterestedLead> notInterestedLeadsList,
                              ArrayList<NotInterestedLead> closureList,
                              ArrayList<LeadStatus> leadsList, String tabName, ArrayList<NotInterestedLead> brokerList){


        //mAsmList = asmList;
        mAsmList.addAll(asmList);

        //mDetailsList = detailsList;
        mDetailsList.addAll(detailsList);

        //mProjectsList = projectsList;
        mProjectsList.addAll(projectsList);

        //mLeadsList = leadsList;
        mLeadsList.addAll(leadsList);

        //mNotInterestedLeadsList = notInterestedLeadsList;
        mNotInterestedLeadsList.addAll(notInterestedLeadsList);

       // mClosureList = closureList;
        mClosureList.addAll(closureList);

       // this.mBrokerList = brokerList;
        if(mBrokerList!=null)
        mBrokerList.addAll(brokerList);

        this.tabName = tabName;

        notifyDataSetChanged();






    }
}

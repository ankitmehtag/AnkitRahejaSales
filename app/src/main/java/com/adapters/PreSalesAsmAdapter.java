package com.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.activities.AsmDetailsActivity;
import com.activities.AsmHistoryActivity;
import com.activities.PreSalesActivity;
import com.helper.BMHConstants;
import com.model.Details;
import com.model.PreSalesAsmModel;
import com.sp.R;
import com.utils.Connectivity;
import com.utils.StringUtil;
import com.utils.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PreSalesAsmAdapter extends RecyclerView.Adapter<PreSalesAsmAdapter.ViewHolder> {

    private Context mContext;
    private String tabName;
    private ArrayList<PreSalesAsmModel> mAsmList;
    private ArrayList<Details> mDetailsList;
    private PreSalesAsmModel mAsmModel;
    private Details mDetailsModel;
    private SimpleDateFormat simpleDateFormat;
    private Calendar cal;
    private Date scheduledDate, currentDate;
    private String currString;
    private String dateInString;

    public PreSalesAsmAdapter(Context context, ArrayList<PreSalesAsmModel> asmList, ArrayList<Details> detailsList, String tabName) {
        mContext = context;
        mAsmList = asmList;
        mDetailsList = detailsList;
        this.tabName = tabName;
        simpleDateFormat = new SimpleDateFormat(Utils.dateFormat1, Locale.getDefault());
        cal = Calendar.getInstance();
        currString = simpleDateFormat.format(cal.getTime());
    }

    @NonNull
    @Override
    public PreSalesAsmAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.sp_asm_row_items, parent, false);
        return new PreSalesAsmAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PreSalesAsmAdapter.ViewHolder holder, int position) {
        mAsmModel = mAsmList.get(position);
        //  Utils.imageUpdate(mContext, holder.iv_user_img, UrlFactory.IMG_BASEURL + mAsmModel.getImage());
        String campaignName = mAsmModel.getCampaign_name();
        String projectName = mAsmModel.getProject_name();
        String customerName = mAsmModel.getCustomer_name();
        String dateandtime =mAsmModel.getScheduledatetime();
        if ((!TextUtils.isEmpty(campaignName) || !campaignName.equals("")) && (TextUtils.isEmpty(projectName) || projectName.equals(""))) {
            holder.tv_campaign.setText(mContext.getString(R.string.txt_campaign_name, campaignName));
        } else if ((!TextUtils.isEmpty(campaignName) || !campaignName.equals("")) && (!TextUtils.isEmpty(projectName) || !projectName.equals(""))) {
            holder.tv_campaign.setText(mContext.getString(R.string.txt_campaign_project, campaignName, projectName));
        } else {
            holder.tv_campaign.setVisibility(View.GONE);
        }
        holder.tv_enquiry_id.setText(mContext.getString(R.string.txt_enquiry_id, mAsmModel.getEnquiry_id()));

        if (!TextUtils.isEmpty(customerName) || !customerName.equals("")) {
            holder.tv_customer_name.setText(customerName);
            StringUtil.createColoredProfileName(customerName, holder.iv_user_img, mAsmModel);
        } else {
            holder.tv_customer_name.setVisibility(View.GONE);
        }

        holder.tv_mobile_no.setText(mAsmModel.getCustomer_mobile());
        holder.tv_current_status.setText(mContext.getString(R.string.tx_status, mAsmModel.getStatus()));
        holder.tv_last_updated_on.setText(mContext.getString(R.string.txt_last_updated_on, mAsmModel.getUpdate_date()));

       if(dateandtime.equals("")){
           holder.tv_last_updated_on.setVisibility(View.GONE);
        }
        else
       {
           holder.tv_last_updated_on.setVisibility(View.VISIBLE);

       }

     /*   if (mAsmModel.getStatus().equalsIgnoreCase("meeting")) {
            holder.tv_last_updated_on.setText(mContext.getString(R.string.txt_meeting_date_time, mAsmModel.getScheduledatetime()));
            holder.tv_current_status.setVisibility(View.GONE);
            }

            else if (mAsmModel.getStatus().equalsIgnoreCase("site visit")) {
            holder.tv_last_updated_on.setText(mContext.getString(R.string.txt_site_visit_date_time, mAsmModel.getScheduledatetime()));
            holder.tv_current_status.setVisibility(View.GONE);

       }

        else if (mAsmModel.getStatus().equalsIgnoreCase("callback")) {
           holder.tv_last_updated_on.setText(mContext.getString(R.string.txt_call_back_date_time, mAsmModel.getScheduledatetime()));
            holder.tv_current_status.setVisibility(View.GONE);

            }

            else {

               holder.tv_current_status.setVisibility(View.VISIBLE);

        }
*/
        /*
          UN-ASSIGNED TAB ITEM LIST
         */

        if (mAsmModel.getIsAssigned() == 0) {
            holder.assigned_view.setVisibility(View.VISIBLE);
            holder.unassigned_view.setVisibility(View.GONE);
            holder.tv_email_id.setVisibility(View.GONE);
            holder.tv_sales_person.setVisibility(View.GONE);
            holder.tv_last_updated_on.setText(mContext.getString(R.string.txt_last_updated_on, mAsmModel.getUpdate_date()));

        }

        /*
          ASSIGNED TAB ITEM LIST
         */

        else {
            holder.assigned_view.setVisibility(View.VISIBLE);
            holder.unassigned_view.setVisibility(View.GONE);
            holder.tv_sales_person.setText(mContext.getString(R.string.txt_salesperson_name, mAsmModel.getSalesperson_name()));
           holder.tv_last_updated_on.setText(mContext.getString(R.string.txt_last_updated_on, mAsmModel.getUpdate_date()));
        }

        if (Connectivity.isConnected(mContext))
            holder.imageView_history.setVisibility(View.VISIBLE);
        else
            holder.imageView_history.setVisibility(View.GONE);
        try {
            dateInString = mAsmModel.getScheduledatetime();
            if (!TextUtils.isEmpty(dateInString) || !dateInString.equals("") || !dateInString.equalsIgnoreCase("NA")) {
                scheduledDate = simpleDateFormat.parse(dateInString);
             //   currentDate = simpleDateFormat.parse(currString);
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
        if (mAsmList != null) {
            return mAsmList.size();
        } else
            return 0;
    }

    public void setFilter(List<PreSalesAsmModel> model) {
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
                if (Connectivity.isConnected(mContext)) {
                    for (int i = 0; i < mDetailsList.size(); i++) {
                        if (mDetailsList.get(i).getEnquiry_ID().equals(mAsmModel.getEnquiry_id())) {
                            mDetailsModel = mDetailsList.get(i);
                            break;
                        }
                    }
                }

                switch (v.getId()) {
                    case R.id.button_overdue:

                        break;
                    case R.id.imageView_history:
                        Intent historyIntent = new Intent(mContext, AsmHistoryActivity.class);
                        historyIntent.putExtra(BMHConstants.ENQUIRY_ID, mAsmModel.getEnquiry_id());
                        mContext.startActivity(historyIntent);
                        break;
                    case R.id.tv_mobile_no:
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (((PreSalesActivity) mContext).checkPermissions(mAsmModel.getCustomer_mobile())) {
                                ((PreSalesActivity) mContext).actionCall(mAsmModel.getCustomer_mobile());
                            } else {
                                ((PreSalesActivity) mContext).requestPermissions();
                            }
                        } else {
                            ((PreSalesActivity) mContext).actionCall(mAsmModel.getCustomer_mobile());
                        }
                        break;

                    default:

                        Intent intent = new Intent(mContext, AsmDetailsActivity.class);
                        intent.putExtra(BMHConstants.ASM_MODEL_DATA, mAsmModel);
                        intent.putExtra(BMHConstants.ASM_DETAIL_DATA, mDetailsModel);
                        intent.putExtra(BMHConstants.SELECTED_TAB_NAME, tabName);
                        intent.putExtra("path", "adapter");
                        mContext.startActivity(intent);
                        break;
                }
            }
        }
    }

}
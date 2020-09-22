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

import com.activities.AsmHistoryActivity;
import com.activities.PreSalesActivity;
import com.activities.SpDetailsActivity;
import com.helper.BMHConstants;
import com.model.Details;
import com.model.PreSalesSpModel;
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

public class PreSalesSpAdapter extends RecyclerView.Adapter<PreSalesSpAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<PreSalesSpModel> mSpList;
    private ArrayList<Details> mDetailsList;
    private PreSalesSpModel spModel;
    private String dateInString;
    private SimpleDateFormat simpleDateFormat;
    private Calendar cal;
    private Date scheduledDate, currentDate;
    private String currString, tabName;
    private Details mDetailsModel;

    public PreSalesSpAdapter(Context context, ArrayList<PreSalesSpModel> arrayList, ArrayList<Details> mDetailsList, String tabName) {
        mContext = context;
        this.tabName = tabName;
        this.mSpList = arrayList;
        this.mDetailsList = mDetailsList;
        simpleDateFormat = new SimpleDateFormat(Utils.dateFormat1, Locale.getDefault());
        cal = Calendar.getInstance();
        currString = simpleDateFormat.format(cal.getTime());
    }

    @NonNull
    @Override
    public PreSalesSpAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.sp_asm_row_items, parent, false);
        return new PreSalesSpAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PreSalesSpAdapter.ViewHolder holder, int position) {
        spModel = mSpList.get(position);
        /* Utils.imageUpdate(mContext, holder.iv_user_img, UrlFactory.IMG_BASEURL + spModel.getImage()); */
        String campaignName = spModel.getCampaign_name();
        String projectName = spModel.getProject_name();
        String customerName = spModel.getCustomer_name();
        if ((!TextUtils.isEmpty(campaignName) || !campaignName.equals("")) && (TextUtils.isEmpty(projectName) || projectName.equals(""))) {
            holder.tv_campaign.setText(mContext.getString(R.string.txt_campaign_name, campaignName));
        } else if ((!TextUtils.isEmpty(campaignName) || !campaignName.equals("")) && (!TextUtils.isEmpty(projectName) || !projectName.equals(""))) {
            holder.tv_campaign.setText(mContext.getString(R.string.txt_campaign_project, campaignName, projectName));
        } else {
            holder.tv_campaign.setVisibility(View.GONE);
        }
        holder.tv_enquiry_id.setText(mContext.getString(R.string.txt_enquiry_id, spModel.getEnquiry_id()));

        if (!TextUtils.isEmpty(customerName) || !customerName.equals("")) {
            holder.tv_customer_name.setText(customerName);
            StringUtil.createColoredProfileName(customerName, holder.iv_user_img, spModel);
        } else {
            holder.tv_customer_name.setVisibility(View.GONE);
        }
        holder.tv_mobile_no.setText(spModel.getCustomer_mobile());
        holder.tv_current_status.setText(mContext.getString(R.string.tx_status, spModel.getStatus()));
        /*
          UN-ASSIGNED TAB ITEM LIST
         */
        if (spModel.getIsAssigned() == 0) {
            holder.assigned_view.setVisibility(View.GONE);
            holder.unassigned_view.setVisibility(View.VISIBLE);
            if (TextUtils.isEmpty(spModel.getCustomer_email()))
                holder.tv_email_id.setVisibility(View.GONE);
            else
                holder.tv_email_id.setText(spModel.getCustomer_email());
            holder.tv_date_time.setText(spModel.getUpdate_date());
        }
        /*
          ASSIGNED TAB ITEM LIST
         */
        else {
            holder.assigned_view.setVisibility(View.VISIBLE);
            holder.unassigned_view.setVisibility(View.GONE);
            holder.tv_sales_person.setVisibility(View.GONE);
            /* holder.tv_sales_person.setText(mContext.getString(R.string.txt_salespersion_name,spModel.getSalesperson_name())); */
            holder.tv_last_updated_on.setText(mContext.getString(R.string.txt_last_updated_on, spModel.getUpdate_date()));
        }
        if (Connectivity.isConnected(mContext))
            holder.imageView_history.setVisibility(View.VISIBLE);
        else
            holder.imageView_history.setVisibility(View.GONE);
        try {
            dateInString = spModel.getScheduledatetime();
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
        return mSpList.size();
    }

    public void setFilter(List<PreSalesSpModel> model) {
        mSpList = new ArrayList<>();
        mSpList.addAll(model);
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
                spModel = mSpList.get(position);

                if (Connectivity.isConnected(mContext)) {
                    for (int i = 0; i < mDetailsList.size(); i++) {
                        if (mDetailsList.get(i).getEnquiry_ID().equals(spModel.getEnquiry_id())) {
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
                        historyIntent.putExtra(BMHConstants.ENQUIRY_ID, spModel.getEnquiry_id());
                        mContext.startActivity(historyIntent);

                        break;
                    case R.id.tv_mobile_no:
                        if (mContext instanceof PreSalesActivity) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                if (((PreSalesActivity) mContext).checkPermissions(spModel.getCustomer_mobile())) {
                                    ((PreSalesActivity) mContext).actionCall(spModel.getCustomer_mobile());
                                } else {
                                    ((PreSalesActivity) mContext).requestPermissions();
                                }
                            } else {
                                ((PreSalesActivity) mContext).actionCall(spModel.getCustomer_mobile());
                            }
                        }
                        break;
                    default:
                        Intent intent = new Intent(mContext, SpDetailsActivity.class);
                        intent.putExtra(BMHConstants.SP_MODEL, spModel);
                        intent.putExtra(BMHConstants.SP_DETAIL_DATA, mDetailsModel);
                        intent.putExtra(BMHConstants.SELECTED_TAB_NAME, tabName);
                        intent.putExtra("path", "adapter");
                        mContext.startActivity(intent);
                        break;
                }
            }
        }
    }
}

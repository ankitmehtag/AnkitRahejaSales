package com.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.model.BrokerLeadsAssignedModel;
import com.sp.R;

import java.util.ArrayList;

public class BrokersLeadsAssignedAdapter extends RecyclerView.Adapter<BrokersLeadsAssignedAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<BrokerLeadsAssignedModel> mLeadsAssignedList;

    public BrokersLeadsAssignedAdapter(Context context, ArrayList<BrokerLeadsAssignedModel> leadsList) {
        this.mContext = context;
        this.mLeadsAssignedList = leadsList;
    }

    @NonNull
    @Override
    public BrokersLeadsAssignedAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.leads_assigned_row, parent, false);
        return new BrokersLeadsAssignedAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull BrokersLeadsAssignedAdapter.ViewHolder holder, int position) {
        BrokerLeadsAssignedModel data = mLeadsAssignedList.get(position);
        holder.tv_customer_name_val.setText(mContext.getString(R.string.colon, data.getCustomer_name()));
        holder.tv_project_name_val.setText(mContext.getString(R.string.colon, data.getProject_name()));
        if (!TextUtils.isEmpty(data.getPrice_range())) {
            holder.tv_budget_val.setText(mContext.getString(R.string.colon, data.getPrice_range()));
        } else {
            holder.tv_budget_val.setText(mContext.getString(R.string.colon, "Not Available"));
        }
        if (!TextUtils.isEmpty(data.getUnit_type())) {
            holder.tv_property_type_val.setText(mContext.getString(R.string.colon, data.getUnit_type()));
        } else {
            holder.tv_property_type_val.setText(mContext.getString(R.string.colon, "Not Available"));
        }
        if (!TextUtils.isEmpty(data.getAddress())) {
            holder.tv_city_val.setText(mContext.getString(R.string.colon, data.getAddress()));
        } else {
            holder.tv_city_val.setText(mContext.getString(R.string.colon, "Not Available"));
        }
        holder.tv_mobile_no_val.setText(mContext.getString(R.string.colon, data.getMobile_number()));
    }

    @Override
    public int getItemCount() {
        return mLeadsAssignedList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_customer_name_val;
        TextView tv_project_name_val;
        TextView tv_budget_val;
        TextView tv_property_type_val;
        TextView tv_city_val;
        TextView tv_mobile_no_val;

        public ViewHolder(View convertView) {
            super(convertView);
            tv_customer_name_val = convertView.findViewById(R.id.tv_customer_name_val);
            tv_project_name_val = convertView.findViewById(R.id.tv_project_name_val);
            tv_budget_val = convertView.findViewById(R.id.tv_budget_val);
            tv_property_type_val = convertView.findViewById(R.id.tv_property_type_val);
            tv_city_val = convertView.findViewById(R.id.tv_city_val);
            tv_mobile_no_val = convertView.findViewById(R.id.tv_mobile_no_val);
        }
    }
}

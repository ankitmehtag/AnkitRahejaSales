package com.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.activities.TransactionDetailsActivity;
import com.helper.BMHConstants;
import com.sp.BMHApplication;
import com.sp.R;
import com.model.MyTransactionsRespModel;
import com.utils.Connectivity;
import com.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class MyTransactionsListAdapter extends RecyclerView.Adapter<MyTransactionsListAdapter.ViewHolder> {

    private Context mContext;
    private String tabName;
    private ArrayList<MyTransactionsRespModel.Data> mTransactionsList;
    private BMHApplication app;
    MyTransactionsRespModel.Data data;

    public MyTransactionsListAdapter(Context context, ArrayList<MyTransactionsRespModel.Data> arrayList, String tabName) {
        app = BMHApplication.getInstance();
        mContext = context;
        mTransactionsList = arrayList;
        this.tabName = tabName;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_transaction_row_items, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        data = mTransactionsList.get(position);
        holder.tv_project_name.setText(mContext.getString(R.string.txt_transaction_project_name, data.getProject_name(), data.getDisplay_name(), data.getUnit_no()));
        holder.tv_dt_val.setText(mContext.getString(R.string.colon, data.getTransaction_datetime()));
        Typeface rupeeTypeFace = Typeface.createFromAsset(mContext.getAssets(), "fonts/rupee_foradian.ttf");
        holder.tv_size_val.setText(mContext.getString(R.string.colon, data.getSize()));
        holder.tv_order_no_val.setText(mContext.getString(R.string.colon, data.getOrder_no()));
        holder.tv_trans_amt_val.setTypeface(rupeeTypeFace);
        if (TextUtils.isEmpty(data.getPaid_amount())) {
            holder.tv_trans_amt_val.setText(mContext.getString(R.string.colon, mContext.getString(R.string.txt_not_available)));
        } else {
            if (data.getPaid_amount().equalsIgnoreCase(mContext.getString(R.string.txt_not_available)))
                holder.tv_trans_amt_val.setText(mContext.getString(R.string.colon, data.getPaid_amount()));
            else
                holder.tv_trans_amt_val.setText(mContext.getString(R.string.colon_rs, StringUtil.amountCommaSeparate(data.getPaid_amount())));
        }
        holder.tv_cust_name_val.setText(mContext.getString(R.string.colon, data.getCustomer_Name()));
        if (tabName.equalsIgnoreCase(mContext.getString(R.string.tab_channel)) &&
                app.getFromPrefs(BMHConstants.USER_DESIGNATION).equalsIgnoreCase("0")) {
            holder.linearLayout_brokerCode.setVisibility(View.VISIBLE);
            holder.linearLayout_brokerName.setVisibility(View.VISIBLE);
            holder.tv_broker_name_val.setText(mContext.getString(R.string.colon, data.getBroker_name()));
            holder.tv_broker_code_val.setText(mContext.getString(R.string.colon, data.getBroker_code()));
        } else {
            holder.linearLayout_brokerCode.setVisibility(View.GONE);
            holder.linearLayout_brokerName.setVisibility(View.GONE);
        }
        holder.tv_payment_mode_val.setText(mContext.getString(R.string.txt_transaction_payment_mode, data.getPayment_mode(), data.getStatus()));
    }


    @Override
    public int getItemCount() {
        if (mTransactionsList != null) {
            return mTransactionsList.size();
        } else
            return 0;
    }

    public void setFilter(List<MyTransactionsRespModel.Data> model) {
        mTransactionsList = new ArrayList<>();
        mTransactionsList.addAll(model);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ConstraintLayout layout;
        LinearLayout linearLayout_brokerCode;
        LinearLayout linearLayout_brokerName;
        TextView tv_project_name;
        TextView tv_dt_val;
        TextView tv_size_val;
        TextView tv_order_no_val;
        TextView tv_trans_amt_val;
        TextView tv_cust_name_val;
        TextView tv_broker_name_val;
        TextView tv_broker_code_val;
        TextView tv_payment_mode_val;

        public ViewHolder(View convertView) {
            super(convertView);

            tv_project_name = convertView.findViewById(R.id.tv_project_name);
            tv_dt_val = convertView.findViewById(R.id.tv_dt_val);
            tv_size_val = convertView.findViewById(R.id.tv_size_val);
            tv_order_no_val = convertView.findViewById(R.id.tv_order_no_val);
            tv_trans_amt_val = convertView.findViewById(R.id.tv_trans_amt_val);
            tv_cust_name_val = convertView.findViewById(R.id.tv_cust_name_val);
            tv_broker_name_val = convertView.findViewById(R.id.tv_broker_name_val);
            tv_broker_code_val = convertView.findViewById(R.id.tv_broker_code_val);
            tv_payment_mode_val = convertView.findViewById(R.id.tv_payment_mode_val);
            linearLayout_brokerCode = convertView.findViewById(R.id.layout_broker_code);
            linearLayout_brokerName = convertView.findViewById(R.id.layout_broker_name);
            layout = convertView.findViewById(R.id.transaction_row);
            layout.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition(); // gets item position

            if (position != RecyclerView.NO_POSITION) { // Check if an item was deleted, but the user clicked it before the UI removed it
                data = mTransactionsList.get(position);
                if (Connectivity.isConnected(mContext)) {
                    for (int i = 0; i < mTransactionsList.size(); i++) {
                        if (mTransactionsList.get(i).getOrder_no().equals(data.getOrder_no())) {
                            data = mTransactionsList.get(i);
                            break;
                        }
                    }
                    if(view.getId()==R.id.transaction_row) {
                        Intent intent = new Intent(mContext, TransactionDetailsActivity.class);
                        intent.putExtra("TAG_NAME", tabName);
                        intent.putExtra("ChannelTransactionModel", data);
                        mContext.startActivity(intent);
                    }
                }


            }
        }
    }
}

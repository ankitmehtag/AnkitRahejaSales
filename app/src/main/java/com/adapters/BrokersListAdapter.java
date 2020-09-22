package com.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.activities.BrokerDetailsActivity;
import com.model.BrokersListModel;
import com.model.BrokersRespDataModel;
import com.sp.BrokersListActivity;
import com.sp.R;
import com.utils.StringUtil;
import com.utils.Utils;

import java.util.ArrayList;

public class BrokersListAdapter extends RecyclerView.Adapter<BrokersListAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<BrokersListModel> mBrokerList;
    BrokersListModel model;

    public BrokersListAdapter(Context context, ArrayList<BrokersListModel> brokerList) {
        this.mContext = context;
        this.mBrokerList = brokerList;
    }

    @NonNull
    @Override
    public BrokersListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.broker_data_row, parent, false);
        return new BrokersListAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        model = mBrokerList.get(position);
        // Utils.imageUpdate(mContext, holder.iv_user_img, model.getImage());

        holder.textView_broker_name.setText(model.getBroker_name());
        holder.tv_broker_code.setText(mContext.getString(R.string.txt_broker_code_colon, model.getBroker_code()));
        String statusString = getBrokerStatus(Integer.valueOf(model.getStatus()));
        if (TextUtils.isEmpty(statusString)) {
            holder.tv_broker_status.setVisibility(View.GONE);
        } else {
            holder.tv_broker_status.setVisibility(View.VISIBLE);
            holder.tv_broker_status.setText(mContext.getString(R.string.txt_broker_detalis_toolbar, statusString, model.getBroker_type()));
        }
        StringUtil.createColoredProfileName(model.getBroker_name(), holder.iv_user_img, model);
    }

    @Override
    public int getItemCount() {
        if (mBrokerList != null) {
            return mBrokerList.size();
        } else
            return 0;
    }

    public void setFilter(ArrayList<BrokersListModel> filteredList) {
        this.mBrokerList = filteredList;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ConstraintLayout layout;
        ImageView iv_user_img;
        TextView textView_broker_name;
        TextView tv_broker_code;
        TextView tv_broker_status;
        ImageView iv_call;
        ImageView iv_email;
        ImageView iv_whatsapp;

        public ViewHolder(View convertView) {
            super(convertView);
            layout = convertView.findViewById(R.id.broker_list_row);
            iv_user_img = convertView.findViewById(R.id.iv_user_img);
            textView_broker_name = convertView.findViewById(R.id.textView_broker_name);
            tv_broker_code = convertView.findViewById(R.id.tv_broker_code);
            tv_broker_status = convertView.findViewById(R.id.tv_broker_status);
            iv_call = convertView.findViewById(R.id.iv_call);
            iv_email = convertView.findViewById(R.id.iv_email);
            iv_whatsapp = convertView.findViewById(R.id.iv_whatsapp);
            layout.setOnClickListener(this);
            iv_call.setOnClickListener(this);
            iv_email.setOnClickListener(this);
            iv_whatsapp.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int id = v.getId();
            int position = getAdapterPosition(); // gets item position
            if (position != RecyclerView.NO_POSITION) { // Check if an item was deleted, but the user clicked it before the UI removed it
                BrokersListModel model = mBrokerList.get(position);
                switch (id) {
                    case R.id.iv_call:
                        if (TextUtils.isEmpty(model.getMobile_no())) {
                            showToast("Contact number not available");
                        } else {
                            String mobileNo = model.getMobile_no();
                            if (mContext instanceof BrokersListActivity) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    if (((BrokersListActivity) mContext).checkPermissions(mobileNo)) {
                                        ((BrokersListActivity) mContext).actionCall(mobileNo);
                                    } else {
                                        ((BrokersListActivity) mContext).requestPermissions();
                                    }
                                } else {
                                    ((BrokersListActivity) mContext).actionCall(mobileNo);
                                }
                            }
                        }
                        break;
                    case R.id.iv_email:
                        if (TextUtils.isEmpty(model.getEmail_id())) {
                            showToast("Email id not available");
                        } else {
                            String email = model.getEmail_id();
                            Utils.openMailClient(mContext, "", new String[]{email}, "");
                        }
                        break;
                    case R.id.iv_whatsapp:
                        if (TextUtils.isEmpty(model.getMobile_no())) {
                            showToast("Whatsapp number not available");
                        }
                        else {
                            String mobileNo = model.getMobile_no();
                            openWhatsApp(mContext, mobileNo);

                            }
                        break;
                    default:
                        Intent intent = new Intent(mContext, BrokerDetailsActivity.class);
                        intent.putExtra("BROKERS_MODEL", model);
                        intent.putExtra("tag", BrokersListActivity.TAG);
                        mContext.startActivity(intent);
                        break;
                }
            }
        }
    }

   /* private void openMailClient(Context ctx, String subject, String[] to, String bodyText) {
        if (ctx == null) return;
        try {
            Intent emailIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:"));
            emailIntent.putExtra(Intent.EXTRA_EMAIL, to);
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
            emailIntent.putExtra(Intent.EXTRA_TEXT, bodyText);
            ctx.startActivity(emailIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    private void openWhatsApp(Context ctx, String whatsAppNo) {
        String toNumber = whatsAppNo.replace("+", "").replace(" ", "");
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        String text = "Hey";
        intent.setPackage(ctx.getString(R.string.txt_whatsApp_package));
        intent.putExtra("", toNumber + "@s.whatsapp.net");
        if (appInstalledOrNot(ctx.getString(R.string.txt_whatsApp_package))) {

            intent.putExtra(Intent.EXTRA_TEXT, text);//
            ctx.startActivity(Intent.createChooser(intent, text));
        } else {
            Toast.makeText(ctx, "WhatsApp not Installed", Toast.LENGTH_SHORT)
                    .show();

            final String appPackageName = ctx.getString(R.string.txt_whatsApp_package); // getPackageName() from Context or Activity object
            try {
                ctx.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
            } catch (android.content.ActivityNotFoundException anfe) {
                ctx.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
            }
        }
    }

    private boolean appInstalledOrNot(String uri) {
        PackageManager pm = mContext.getPackageManager();
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            e.getMessage();

             }

        return false;
    }

    private void showToast(String message) {
        if (mContext == null) return;
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }

    private String getBrokerStatus(int status) {
        String statusValue = "";
        if (status == BrokersRespDataModel.ACTIVE) statusValue = "Active";
        if (status == BrokersRespDataModel.INACTIVE) statusValue = "Inactive";
        if (status == BrokersRespDataModel.SUSPENDED) statusValue = "Suspended";
        return statusValue;
    }


}

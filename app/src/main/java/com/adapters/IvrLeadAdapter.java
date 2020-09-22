package com.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.activities.S1;
import com.google.gson.Gson;
import com.model.BrokersRespDataModel;
import com.model.IvrLeadModel;
import com.model.Projects;
import com.sp.IVRLeads;
import com.sp.R;

import java.util.ArrayList;

  public class IvrLeadAdapter extends RecyclerView.Adapter<IvrLeadAdapter.ViewHolder> {
      public static String TAG = IvrLeadAdapter.class.getSimpleName();
    private Context mContext;
    private ArrayList<IvrLeadModel> mIvrList;
    IvrLeadModel model;
    public ArrayList<Projects> projectsMasterList;



    public IvrLeadAdapter(Context mContext, ArrayList<IvrLeadModel> mIvrList, ArrayList<Projects> projectsMasterList) {
        this.mContext = mContext;
        this.mIvrList = mIvrList;
        this.projectsMasterList = projectsMasterList;

             }

    @NonNull
    @Override
    public IvrLeadAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.ivr, parent, false);
        return new IvrLeadAdapter.ViewHolder(v);
    }





    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        model = mIvrList.get(position);
        // Utils.imageUpdate(mContext, holder.iv_user_img, model.getImage());

        holder.mobile_no.setText(model.getmobile_no());
        holder.campaign_title.setText(model.getcampaign_title());
        holder.datetime.setText(model.getdatetime());
        holder.dialstatus.setText(model.getdialstatus());
        holder.project.setText(model.getproject());

      //  String statusString = getBrokerStatus(Integer.valueOf(model.getStatus()));


       /* if (TextUtils.isEmpty(statusString)) {
            holder.tv_broker_status.setVisibility(View.GONE);
        } else {
            holder.tv_broker_status.setVisibility(View.VISIBLE);
            holder.tv_broker_status.setText(mContext.getString(R.string.txt_broker_detalis_toolbar, statusString, model.getBroker_type()));
        }
        StringUtil.createColoredProfileName(model.getBroker_name(), holder.iv_user_img, model);*/
    }

    @Override
    public int getItemCount() {
        if (mIvrList != null) {
            return mIvrList.size();
        }
           else
            return 0;
    }

    public void setFilter(ArrayList<IvrLeadModel> filteredList) {
        this.mIvrList = filteredList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        LinearLayout layout;
        public TextView mobile_no;
        public TextView campaign_title;
       public TextView datetime;
        public TextView dialstatus;
        TextView blanktext;
       public TextView project;
        ImageView imageView_history;


        public ViewHolder(View convertView) {
            super(convertView);

            layout = convertView.findViewById(R.id.ivr_list_row);
            mobile_no = convertView.findViewById(R.id.mobile_no);
            campaign_title = convertView.findViewById(R.id.campaign_title);
            datetime = convertView.findViewById(R.id.datetime);
            project = convertView.findViewById(R.id.project);

            dialstatus = convertView.findViewById(R.id.dialstatus);
            imageView_history= convertView.findViewById(R.id.imageView_history);
            blanktext= convertView.findViewById(R.id.blanktext);

            blanktext.setOnClickListener(this);
            datetime.setOnClickListener(this);
            imageView_history.setOnClickListener(this);
            mobile_no.setOnClickListener(this);
            campaign_title.setOnClickListener(this);
            project.setOnClickListener(this);
        }



        @Override
        public void onClick(View v) {
            int id = v.getId();
            int position = getAdapterPosition();
            IvrLeadModel model = mIvrList.get(position);   // gets item position
            if (position != RecyclerView.NO_POSITION) {   // Check if an item was deleted, but the user clicked it before the UI removed it
                // IvrLeadModel model = mIvrList.get(position);
                switch (id) {
                    case R.id.mobile_no:
                        if (TextUtils.isEmpty(model.getmobile_no())) {
                            showToast("Contact number not available");
                        } else {
                            String mobileNo = model.getmobile_no();
                            if (mContext instanceof IVRLeads) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    if (((IVRLeads) mContext).checkPermissions(mobileNo)) {
                                        ((IVRLeads) mContext).actionCall(mobileNo);
                                    } else {
                                        ((IVRLeads) mContext).requestPermissions();
                                    }
                                } else {
                                    ((IVRLeads) mContext).actionCall(mobileNo);
                                }
                            }
                        }
                        break;
                    case R.id.campaign_title:
                    case R.id.datetime:
                    case R.id.blanktext:
                    case R.id.project:

                        {
                        String campaign_title = model.getcampaign_title();
                        String mobile_no=model.getmobile_no();
                        String IsLead=model.getIsLead();
                        String project_name=model.getproject();
                        Intent intent = new Intent(mContext, S1.class);



                         intent.putExtra("campaign_title", campaign_title);
                         intent.putExtra("mobile_no",mobile_no);
                         intent.putExtra("IsLead",IsLead);
                            intent.putExtra("project",project_name);
                        intent.putExtra("master_project_list",new Gson().toJson(projectsMasterList));



                        mContext.startActivity(intent);

                    }

                        break;

                    case R.id.imageView_history: {
                        String mobile=model.getmobile_no();
                        String history_url=model.gethistory_url();
                        Intent historyIntent = new Intent(mContext, Webviewhistory.class);
                        historyIntent.putExtra("history_url",history_url);
                        historyIntent.putExtra("mobile",mobile);
                        mContext.startActivity(historyIntent);
                    }
                    break;



                    default:



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

package com.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.AppEnums.UIEventType;
import com.VO.UnitDetailVO;
import com.activities.ChatScreen;
import com.appnetwork.CustomAsyncTask;
import com.bumptech.glide.Glide;
import com.exception.BMHException;
import com.helper.BMHConstants;
import com.helper.IntentDataObject;
import com.helper.ParamsConstants;
import com.helper.UrlFactory;
import com.model.MyChatUnitsRespModel;
import com.model.NetworkErrorObject;
import com.model.PropertyModel;
import com.sp.BMHApplication;
import com.sp.ConnectivityReceiver;
import com.sp.R;
import com.utils.Utils;

import java.util.ArrayList;

/**
 * @author Sanjaya Verma
 */

public class MyChatUnitsListAdapter extends RecyclerView.Adapter<MyChatUnitsListAdapter.ViewHolder> {

    private Context mContext;
    private BMHApplication app;
    private ArrayList<MyChatUnitsRespModel.Data> chatList;
    MyChatUnitsRespModel.Data data;
    private UnitDetailVO unitDetailVO;
    IntentDataObject mIntentDataObject;
    private NetworkErrorObject mNetworkErrorObject = null;

    public MyChatUnitsListAdapter(Context context, ArrayList<MyChatUnitsRespModel.Data> dataList) {
        app = BMHApplication.getInstance();
        mContext = context;
        chatList = dataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_unit_row, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        data = chatList.get(position);
        holder.iv_unit_img.setImageResource(R.drawable.no_image);
        String url = UrlFactory.IMG_BASEURL + data.getUnit_image();//TODO: assign url
        if (url != null && !url.equalsIgnoreCase("")) {
            Glide.with(mContext.getApplicationContext()).load(UrlFactory.getShortImageByWidthUrl((int) Utils.dp2px(120, mContext), url))
                    .placeholder(BMHConstants.PLACE_HOLDER).error(BMHConstants.NO_IMAGE).into(holder.iv_unit_img);
        }
        Integer is_reserved = Integer.valueOf(data.getIsUnitReserved());
        if (is_reserved == 1) {
            holder.isReserved.setText("Reserved");
        } else {
            holder.isReserved.setVisibility(View.GONE);
        }

        holder.tv_unit_name.setText(Html.fromHtml(Html.fromHtml((String) data.getProject_name()).toString()));
        holder.tv_user_name.setText(data.getCustomer_name());
        holder.tv_chat_id.setText("Chat Id:" + data.getUser_chat_id());
        holder.tv_unit_no.setText("Unit No: " + data.getUnit_number());
        holder.tv_unit_type.setText(data.getUnit_name());
        String unReadCount = data.getUnread_count();
        if (TextUtils.isEmpty(unReadCount)) {
            holder.chat_count.setVisibility(View.GONE);
        } else {
            holder.chat_count.setVisibility(View.VISIBLE);
            holder.chat_count.setText(data.getUnread_count());
        }
    }

    @Override
    public int getItemCount() {
        if (chatList != null) {
            return chatList.size();
        } else
            return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        RelativeLayout layout;
        ImageView iv_unit_img, iv_next;
        TextView tv_chat_id, tv_unit_no, tv_unit_name, tv_unit_type, chat_count,
                tv_last_update_title, tv_last_update_time, tv_user_name, isReserved;
        private boolean clicked;

        public ViewHolder(View convertView) {
            super(convertView);

            layout = convertView.findViewById(R.id.layout_chat_row);
            iv_unit_img = convertView.findViewById(R.id.iv_unit_img);
            tv_user_name = convertView.findViewById(R.id.tv_user_name);
            tv_chat_id = convertView.findViewById(R.id.tv_chat_id);
            tv_unit_no = convertView.findViewById(R.id.tv_unit_no);
            tv_unit_name = convertView.findViewById(R.id.tv_unit_name);
            tv_unit_type = convertView.findViewById(R.id.tv_unit_type);
            isReserved = convertView.findViewById(R.id.tv_reserved);
            chat_count = convertView.findViewById(R.id.chat_count);
            layout.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.layout_chat_row) {
                try {
                    if (clicked) {
                        return;
                    }
                    clicked = true;
                    int position = getAdapterPosition(); // gets item position
                    if (position != RecyclerView.NO_POSITION) { // Check if an item was deleted, but the user clicked it before the UI removed it
                        data = chatList.get(position);
                        if (data != null) {

                            mIntentDataObject = new IntentDataObject();
                            mIntentDataObject.putData(ParamsConstants.UNIT_ID, data.getUnit_id());
                            mIntentDataObject.putData(ParamsConstants.UNIT_NO, data.getUnit_number());
                            mIntentDataObject.putData(ParamsConstants.UNIT_TITLE, data.getProject_name());
                            mIntentDataObject.putData(ParamsConstants.PAYMENT_PLAN, data.getPlan_name());
                            mIntentDataObject.putData(ParamsConstants.BHK_TYPE, data.getUnit_name());
                            mIntentDataObject.putData(ParamsConstants.UNIT_IMAGE, data.getUnit_image());
                            mIntentDataObject.putData(ParamsConstants.UNIT_RESERVED, data.getIsUnitReserved());

                            CustomAsyncTask loadingTask = new CustomAsyncTask((Activity) mContext, new CustomAsyncTask.AsyncListner() {
                                @Override
                                public void OnBackgroundTaskCompleted() {
                                    if (unitDetailVO == null) {
                                        if (!ConnectivityReceiver.isConnected()) {
                                            //TODO: network call
                                            //Do nothing
                                        } else {
                                            mNetworkErrorObject = Utils.showNetworkErrorDialog(mContext, UIEventType.RETRY_UNIT_DETAILS,
                                                    new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            if (ConnectivityReceiver.isConnected()) {
                                                                //TODO: network call
                                                                mNetworkErrorObject.getAlertDialog().dismiss();
                                                                mNetworkErrorObject = null;
                                                            } else {
                                                                Utils.showToast(mContext, mContext.getString(R.string.check_your_internet_connection));
                                                            }
                                                        }
                                                    });
                                        }

                                    } else {
                                        if (unitDetailVO.isSuccess()) {
                                            clicked = false;
                                            Intent mIntent = new Intent(mContext, ChatScreen.class);
                                            mIntent.putExtra(IntentDataObject.OBJ, mIntentDataObject);
                                            mIntent.putExtra("unitvo", unitDetailVO);
                                            mContext.startActivity(mIntent);
                                        } else {
                                            //Toast.makeText(MyChat.this, unitDetailVO.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }

                                @Override
                                public void DoBackgroundTask(String[] url) {
                                    PropertyModel model = new PropertyModel();
                                    try {
                                        String uesrid = app.getFromPrefs(BMHConstants.USERID_KEY);
                                        unitDetailVO = model.getUnitDetail(data.getUnit_id(), uesrid);
                                    } catch (BMHException e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void OnPreExec() {
                                }
                            });
                            loadingTask.execute("");

                        } else {
                            Toast.makeText(mContext, mContext.getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                        }

                    }
                } catch (Exception e) {
                    System.out.println(e.toString());
                }
            }
        }
    }
}

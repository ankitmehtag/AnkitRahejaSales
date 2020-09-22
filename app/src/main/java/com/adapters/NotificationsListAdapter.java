package com.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.AppEnums.APIType;
import com.activities.BlogDetailsActivity;
import com.activities.NotificationsActivity;
import com.activities.TermsAndConditionActivity;
import com.activities.TransactionDetailsActivity;
import com.appnetwork.VolleyRequestApi;
import com.appnetwork.WEBAPI;
import com.bumptech.glide.Glide;
import com.helper.BMHConstants;
import com.helper.IntentDataObject;
import com.helper.ParamsConstants;
import com.helper.UrlFactory;
import com.model.MyTransactionsRespModel;
import com.model.NotificationsListRespModel;
import com.sp.BMHApplication;
import com.sp.FAQActivity;
import com.sp.GetProjectListNotification;
import com.sp.PaymentStatusActivity;
import com.sp.ProjectDetailActivity;
import com.sp.R;
import com.sp.UnitMapActivity;
import com.utils.Utils;

import java.util.ArrayList;

import static com.fragments.MyTransChannelFragment.TAG_NAME;

public class NotificationsListAdapter extends RecyclerView.Adapter<NotificationsListAdapter.ViewHolder> {
    private Context context;

    ArrayList<NotificationsListRespModel.Data> arrComments;
    private BMHApplication app;

    public NotificationsListAdapter(Activity c, ArrayList<NotificationsListRespModel.Data> arr) {
        context = c;
        arrComments = arr;
        app = (BMHApplication) context.getApplicationContext();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_row, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        v.setTag(R.integer.tag);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        NotificationsListRespModel.Data item = arrComments.get(position);
        Integer is_read = Integer.valueOf(arrComments.get(position).getIsRead());
        String notificationType = getNotificationType(item.getType());
        if (is_read == 1) {
            holder.notification_dot.setVisibility(View.VISIBLE);
            if (notificationType.equalsIgnoreCase(BMHConstants.NOTIFICAYION_TYPE_BLOG))
                holder.notification_dot.setBackground(context.getResources().getDrawable(R.drawable.blog));
            if (notificationType.equalsIgnoreCase(BMHConstants.NOTIFICAYION_TYPE_FAQ))
                holder.notification_dot.setBackground(context.getResources().getDrawable(R.drawable.notification));
            if (notificationType.equalsIgnoreCase(BMHConstants.NOTIFICAYION_TYPE_PROJECT_LIST))
                holder.notification_dot.setBackground(context.getResources().getDrawable(R.drawable.project));
            if (notificationType.equalsIgnoreCase(BMHConstants.NOTIFICAYION_TYPE_PROJECT_DETAIL))
                holder.notification_dot.setBackground(context.getResources().getDrawable(R.drawable.project));
            if (notificationType.equalsIgnoreCase(BMHConstants.NOTIFICAYION_TYPE_UNIT_Details))
                holder.notification_dot.setBackground(context.getResources().getDrawable(R.drawable.project));
            if (notificationType.equalsIgnoreCase(BMHConstants.NOTIFICAYION_TYPE_UNIT_LIST))
                holder.notification_dot.setBackground(context.getResources().getDrawable(R.drawable.project));
            if (notificationType.equalsIgnoreCase(BMHConstants.NOTIFICAYION_TYPE_PAYMENT))
                holder.notification_dot.setBackground(context.getResources().getDrawable(R.drawable.transaction));
            if (notificationType.equalsIgnoreCase(BMHConstants.NOTIFICAYION_TYPE_BROKER_PAYMENT))
                holder.notification_dot.setBackground(context.getResources().getDrawable(R.drawable.transaction));

        } else {
            holder.notification_dot.setVisibility(View.VISIBLE);
            if (notificationType.equalsIgnoreCase(BMHConstants.NOTIFICAYION_TYPE_BLOG))
                holder.notification_dot.setBackground(context.getResources().getDrawable(R.drawable.blog_select));
            if (notificationType.equalsIgnoreCase(BMHConstants.NOTIFICAYION_TYPE_FAQ))
                holder.notification_dot.setBackground(context.getResources().getDrawable(R.drawable.notification_select));
            if (notificationType.equalsIgnoreCase(BMHConstants.NOTIFICAYION_TYPE_PROJECT_LIST))
                holder.notification_dot.setBackground(context.getResources().getDrawable(R.drawable.project_select));
            if (notificationType.equalsIgnoreCase(BMHConstants.NOTIFICAYION_TYPE_PROJECT_DETAIL))
                holder.notification_dot.setBackground(context.getResources().getDrawable(R.drawable.project_select));
            if (notificationType.equalsIgnoreCase(BMHConstants.NOTIFICAYION_TYPE_UNIT_Details))
                holder.notification_dot.setBackground(context.getResources().getDrawable(R.drawable.project_select));
            if (notificationType.equalsIgnoreCase(BMHConstants.NOTIFICAYION_TYPE_UNIT_LIST))
                holder.notification_dot.setBackground(context.getResources().getDrawable(R.drawable.project_select));
            if (notificationType.equalsIgnoreCase(BMHConstants.NOTIFICAYION_TYPE_PAYMENT))
                holder.notification_dot.setBackground(context.getResources().getDrawable(R.drawable.transaction_select));
            if (notificationType.equalsIgnoreCase(BMHConstants.NOTIFICAYION_TYPE_BROKER_PAYMENT))
                holder.notification_dot.setBackground(context.getResources().getDrawable(R.drawable.transaction_select));
        }
        String image = arrComments.get(position).getImage();//TODO: assign url
        if (image == "") {
            holder.iv_img.setVisibility(View.GONE);
        } else {
            String url = UrlFactory.IMG_BASEURL + arrComments.get(position).getImage();
            if (url != null && !url.equalsIgnoreCase("")) {
                Glide.with(context.getApplicationContext()).load(UrlFactory.getShortImageByWidthUrl((int) Utils.dp2px(120, context), url))
                        .placeholder(BMHConstants.PLACE_HOLDER).error(BMHConstants.NO_IMAGE).into(holder.iv_img);
            }
        }
        if (item.getTitle() != null && !item.getTitle().isEmpty())
            holder.tv_title.setText(item.getTitle());
        if (item.getMessage() != null && !item.getMessage().isEmpty())
            holder.tv_message.setText(item.getMessage());
        holder.tv_last_update_time.setText(Utils.getFormattedDate(item.getTimestamp()));
    }


    @Override
    public int getItemCount() {
        if (arrComments != null) {
            return arrComments.size();
        } else
            return 0;
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_img;
        TextView tv_title;
        TextView tv_message;
        TextView tv_last_update_time;
        TextView notification_dot;

        public ViewHolder(View convertView) {
            super(convertView);

            tv_title = convertView.findViewById(R.id.tv_title);
            tv_message = convertView.findViewById(R.id.tv_message);
            tv_last_update_time = convertView.findViewById(R.id.tv_last_update_time);
            iv_img = convertView.findViewById(R.id.iv_img);
            notification_dot = convertView.findViewById(R.id.notification_dot);

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition(); // gets item position
                    NotificationsListRespModel.Data mData = arrComments.get(position);
                    NotificationsListRespModel.Data.TargetData mTargetInfo = mData.getTarget_info();
                    VolleyRequestApi requestApi;
                    if (mData != null) {
                        String notificationId;
                        switch (mData.getType()) {
                            case NotificationsListRespModel.TERMS_AND_CONDITION:
                                notificationId = mData.getNotification_id();

                                requestApi = new VolleyRequestApi(notificationId, WEBAPI.getWEBAPI(APIType.READ_STATUS), context);
                                requestApi.updateNotificationReadStatus();

                                String targetInfo = mTargetInfo.getUrl();
                                Intent intentError = new Intent(context, TermsAndConditionActivity.class);
                                intentError.putExtra("target_info", targetInfo);
                                context.startActivity(intentError);
                                //    finish();
                                break;
                            case NotificationsListRespModel.BLOG:
                                notificationId = mData.getNotification_id();
                                requestApi = new VolleyRequestApi(notificationId, WEBAPI.getWEBAPI(APIType.READ_STATUS), context);
                                requestApi.updateNotificationReadStatus();

                                BMHConstants.BLOG_URL = mTargetInfo.getUrl();
                                Intent intentBLOG = new Intent(context, BlogDetailsActivity.class);
                                intentBLOG.putExtra("target_info", BMHConstants.BLOG_URL);
                                context.startActivity(intentBLOG);
                                //((SearchPropertyActivity)getActivity()).addFragment(new BlogFragment(), true);
                                break;
                            case NotificationsListRespModel.FAQ:
                                notificationId = mData.getNotification_id();
                                requestApi = new VolleyRequestApi(notificationId, WEBAPI.getWEBAPI(APIType.READ_STATUS), context);
                                requestApi.updateNotificationReadStatus();

                                BMHConstants.BLOG_URL = mTargetInfo.getUrl();
                                Intent intentFAQ = new Intent(context, FAQActivity.class);
                                intentFAQ.putExtra("target_info", BMHConstants.BLOG_URL);
                                context.startActivity(intentFAQ);
                                break;
                            case NotificationsListRespModel.PROJECT_LIST:
                                notificationId = mData.getNotification_id();
                                requestApi = new VolleyRequestApi(notificationId, WEBAPI.getWEBAPI(APIType.READ_STATUS), context);
                                requestApi.updateNotificationReadStatus();

                                IntentDataObject mIntentDataObjecProjectList = new IntentDataObject();
                                mIntentDataObjecProjectList.putData(ParamsConstants.ID, mTargetInfo.getProject_id());
                                if (app.getFromPrefs(BMHConstants.USERID_KEY) != null && app.getFromPrefs(BMHConstants.USERID_KEY).length() > 0) {
                                    mIntentDataObjecProjectList.putData(ParamsConstants.USER_ID, app.getFromPrefs(BMHConstants.USERID_KEY));
                                }
                                Intent mIntentPL = new Intent(context, GetProjectListNotification.class);
                                mIntentPL.putExtra(IntentDataObject.OBJ, mIntentDataObjecProjectList);
                                context.startActivity(mIntentPL);
                                break;
                            case NotificationsListRespModel.UNIT_LIST:
                                notificationId = mData.getNotification_id();
                                requestApi = new VolleyRequestApi(notificationId, WEBAPI.getWEBAPI(APIType.READ_STATUS), context);
                                requestApi.updateNotificationReadStatus();
                                Intent resultIntent = new Intent(context, UnitMapActivity.class);
                                resultIntent.putExtra("unitIds", mData.getTarget_info().getUnit_id());
                                resultIntent.putExtra("projectId", mData.getTarget_info().getProject_id());
                                resultIntent.putExtra("se", mData.getTarget_info().getCords().getSe());
                                resultIntent.putExtra("nw", mData.getTarget_info().getCords().getNw());
                                resultIntent.putExtra("pro_plan_img", mData.getTarget_info().getProject_plan_img());
                                resultIntent.putExtra("project_type", mData.getTarget_info().getProject_type());
                                context.startActivity(resultIntent);
                                break;
                            case NotificationsListRespModel.PAYMENT_TYPE:
                                notificationId = mData.getNotification_id();
                                requestApi = new VolleyRequestApi(notificationId, WEBAPI.getWEBAPI(APIType.READ_STATUS), context);
                                requestApi.updateNotificationReadStatus();

                                MyTransactionsRespModel.Data mIntentDataObject = new MyTransactionsRespModel.Data();
                                mIntentDataObject.setCustomer_Name(mTargetInfo.getCustomer_Name());
                                mIntentDataObject.setCustomer_Mobile_no(mTargetInfo.getCustomer_Mobile_no());
                                mIntentDataObject.setOrder_no(mTargetInfo.getOrder_no());
                                mIntentDataObject.setTransaction_no(mTargetInfo.getTransaction_no());
                                mIntentDataObject.setUnit_no(mTargetInfo.getUnit_no());
                                mIntentDataObject.setProject_name(mTargetInfo.getProject_name());
                                mIntentDataObject.setSize(mTargetInfo.getSize());
                                mIntentDataObject.setDisplay_name(mTargetInfo.getDisplay_name());
                                mIntentDataObject.setTransaction_datetime(mTargetInfo.getTransaction_datetime());
                                mIntentDataObject.setPaid_amount(mTargetInfo.getPaid_amount());
                                mIntentDataObject.setCoapplicant(mTargetInfo.getCoapplicant());
                                mIntentDataObject.setPayment_plan(mTargetInfo.getPayment_plan());
                                mIntentDataObject.setCoupen_code(mTargetInfo.getCoupen_code());
                                mIntentDataObject.setStatus(mTargetInfo.getStatus());
                                mIntentDataObject.setPayment_mode(mTargetInfo.getPayment_mode());
                                mIntentDataObject.setBroker_code(mTargetInfo.getBroker_code());
                                mIntentDataObject.setBroker_name(mTargetInfo.getBroker_name());
                                Intent mIntent = new Intent(context, TransactionDetailsActivity.class);
                                mIntent.putExtra("ChannelTransactionModel", mIntentDataObject);
                                mIntent.putExtra("TAG_NAME", TAG_NAME);
                                context.startActivity(mIntent);
                                break;
                            case NotificationsListRespModel.PROJECT_DETAIL:
                                notificationId = mData.getNotification_id();
                                requestApi = new VolleyRequestApi(notificationId, WEBAPI.getWEBAPI(APIType.READ_STATUS), context);
                                requestApi.updateNotificationReadStatus();
                                IntentDataObject mIntentDataObjectProjectDetail = new IntentDataObject();
                                mIntentDataObjectProjectDetail.putData(ParamsConstants.ID, mData.getTarget_info().getProject_id());
                                Intent mIntentProjectDetail = new Intent(context, ProjectDetailActivity.class);
                                mIntentProjectDetail.putExtra(IntentDataObject.OBJ, mIntentDataObjectProjectDetail);
                                context.startActivity(mIntentProjectDetail);
                                break;
                            case NotificationsListRespModel.PAYMENT:
                                notificationId = mData.getNotification_id();
                                requestApi = new VolleyRequestApi(notificationId, WEBAPI.getWEBAPI(APIType.READ_STATUS), context);
                                requestApi.updateNotificationReadStatus();

                                IntentDataObject mIntentDataObjectPayment = new IntentDataObject();
                                //mIntentDataObjectPayment.putData("PaymentMode","Net banking");
                                if (mTargetInfo != null) {
                                    mIntentDataObjectPayment.putData(context.getString(R.string.txt_order_no), mTargetInfo.getOrder_id());
                                    mIntentDataObjectPayment.putData(context.getString(R.string.txt_unit_no), mTargetInfo.getUnit_id());
                                    mIntentDataObjectPayment.putData(context.getString(R.string.txt_project_name), mTargetInfo.getProject_name());
                                    mIntentDataObjectPayment.putData(context.getString(R.string.payment_status), mTargetInfo.getStatus_message());
                                    mIntentDataObjectPayment.putData(context.getString(R.string.txt_payment_mode), mTargetInfo.getStatus_message());
                                    mIntentDataObjectPayment.putData(context.getString(R.string.txt_cheque_no), mTargetInfo.getTransaction_no());

                                    //mIntentDataObject.putData("Transaction Date",mNotificationPayLoadData.getPayload().getT);
                                    if (!TextUtils.isEmpty(mTargetInfo.getPayment_mode()) && mTargetInfo.getPayment_mode() != null) {
                                        if (mTargetInfo.getPayment_mode().equalsIgnoreCase("CC")) {
                                            mIntentDataObjectPayment.putData(context.getString(R.string.txt_payment_mode), context.getString(R.string.txt_credit_card));
                                        } else if (mTargetInfo.getPayment_mode().equalsIgnoreCase("DC")) {
                                            mIntentDataObjectPayment.putData(context.getString(R.string.txt_payment_mode), context.getString(R.string.txt_debit_card));
                                        } else if (mTargetInfo.getPayment_mode().equalsIgnoreCase("NB")) {
                                            mIntentDataObjectPayment.putData(context.getString(R.string.txt_payment_mode), context.getString(R.string.txt_net_banking));
                                        } else {
                                            mIntentDataObjectPayment.putData(context.getString(R.string.txt_payment_mode), mTargetInfo.getPayment_mode());
                                        }
                                    }
                                    mIntentDataObjectPayment.putData(context.getString(R.string.txt_cheque_amount), mTargetInfo.getAmount());
                                    mIntentDataObjectPayment.putData(context.getString(R.string.txt_cheque_date), mTargetInfo.getOrder_date());
                                    mIntentDataObjectPayment.putData(context.getString(R.string.txt_customer_name), mTargetInfo.getCustomer_name());
                                    mIntentDataObjectPayment.putData(context.getString(R.string.txt_customer_email), mTargetInfo.getCustomer_email());
                                    mIntentDataObjectPayment.putData(context.getString(R.string.txt_customer_mobile_no), mTargetInfo.getCustomer_telephone());
                                    mIntentDataObjectPayment.putData(context.getString(R.string.txt_pan_aadhar_no), mTargetInfo.getPan_no());
                                    mIntentDataObjectPayment.putData(context.getString(R.string.txt_coupon_code), mTargetInfo.getCoupen_code());
                                    mIntentDataObjectPayment.putData(context.getString(R.string.txt_payment_plan_key), mTargetInfo.getPayment_plan());
                                    mIntentDataObjectPayment.putData(context.getString(R.string.txt_payment_plan_desc_key), mTargetInfo.getPaymentPlanDesc());

                                    Intent intentPayment = new Intent(context, PaymentStatusActivity.class);
                                    intentPayment.putExtra(IntentDataObject.OBJ, mIntentDataObjectPayment);
                                    intentPayment.putExtra(NotificationsActivity.TAG, NotificationsActivity.TAG);
                                    context.startActivity(intentPayment);
                                } else {
                                    Utils.showToast(context, context.getString(R.string.something_went_wrong));
                                }
                                break;
                            default:
                                break;
                        }
                    } else {
                        Utils.showToast(context, context.getString(R.string.something_went_wrong));
                    }
                }
            });
            /*itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    NotificationsListRespModel mData = (NotificationsListRespModel) view.getTag(R.integer.tag);

                    //Toast.makeText(view.getContext(), cpu.getPersonFirstName()+" "+cpu.getPersonLastName()+" is "+ cpu.getJobProfile(), Toast.LENGTH_SHORT).show();

                }
            });*/

        }

    }

    private String getNotificationType(int type) {
        String statusValue = "";
        switch (type) {
            case BMHConstants.BLOG:
                statusValue = BMHConstants.NOTIFICAYION_TYPE_BLOG;
                break;
            case BMHConstants.FAQ:
                statusValue = BMHConstants.NOTIFICAYION_TYPE_FAQ;
                break;
            case BMHConstants.PROJECT_LIST_:
                statusValue = BMHConstants.NOTIFICAYION_TYPE_PROJECT_LIST;
                break;
            case BMHConstants.PROJECT_DETAIL:
                statusValue = BMHConstants.NOTIFICAYION_TYPE_PROJECT_DETAIL;
                break;
            case BMHConstants.UNIT_Details:
                statusValue = BMHConstants.NOTIFICAYION_TYPE_UNIT_Details;
                break;
            case BMHConstants.UNIT_LIST:
                statusValue = BMHConstants.NOTIFICAYION_TYPE_UNIT_LIST;
                break;

            case BMHConstants.PAYMENT:
                statusValue = BMHConstants.NOTIFICAYION_TYPE_PAYMENT;
                break;
            case BMHConstants.BROKER_PAYMENT:
                statusValue = BMHConstants.NOTIFICAYION_TYPE_BROKER_PAYMENT;
                break;
        }

        return statusValue;
    }

    private void addReadMore(final String text, final TextView textView) {
        if (text.length() > 65) {
            SpannableString ss = new SpannableString(text.substring(0, 65) + context.getString(R.string.txt_read_more));
            ClickableSpan clickableSpan = new ClickableSpan() {
                @Override
                public void onClick(View view) {
                    addReadLess(text, textView);
                }

                @Override
                public void updateDrawState(TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setUnderlineText(false);
                    ds.setColor(context.getResources().getColor(R.color.blue_color));
                }

            };
            ss.setSpan(clickableSpan, ss.length() - 10, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            textView.setText(ss);
            textView.setMovementMethod(LinkMovementMethod.getInstance());
        }
        else {
            textView.setText(text);
        }

    }

           private void addReadLess(final String text, final TextView textView) {
        SpannableString ss = new SpannableString(text + context.getString(R.string.txt_read_less));
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                addReadMore(text, textView);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
                ds.setColor(context.getResources().getColor(R.color.blue_color));
            }
        };
        ss.setSpan(clickableSpan, ss.length() - 10, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(ss);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
    }
}

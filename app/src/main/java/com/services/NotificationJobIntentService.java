package com.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.JobIntentService;
import androidx.core.app.NotificationCompat;
import android.text.TextUtils;

import com.activities.AsmDetailsActivity;
import com.activities.AssignedSalesDetailActivity;
import com.activities.FollowUpSalesDetailActivity;
import com.activities.SpDetailsActivity;
import com.database.task.AlarmServicesAsyncTask;
import com.database.entity.CustomerInfoEntity;
import com.helper.BMHConstants;
import com.sp.BMHApplication;
import com.sp.R;
import com.utils.StringUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class NotificationJobIntentService extends JobIntentService implements AlarmServicesAsyncTask.ICustomerEntityInfoCommunicator {
    private static final int JobId = 1001;
    String currentStatus, enquiryId, customerName, mobileNo, isAssign, lead_phase, designation;
    int index;
    private Uri alarmUri;
    private Ringtone ringtone;

    public static void enqueueWork(Context context, Intent work) {
        enqueueWork(context, NotificationJobIntentService.class, JobId, work);
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        enquiryId = intent.getStringExtra("enquiry_id");
        customerName = intent.getStringExtra(BMHConstants.CUSTOMER_NAME);
        mobileNo = intent.getStringExtra(BMHConstants.MOBILE_NO);
        currentStatus = intent.getStringExtra("current_status");
        index = intent.getIntExtra(BMHConstants.ALARM_INDEX, 0);
        designation = BMHApplication.getInstance().getFromPrefs((BMHConstants.USER_DESIGNATION));
        new AlarmServicesAsyncTask(this, enquiryId).execute();
        //   sendNotification(currentStatus, enquiryId);
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    private void sendNotification(String description, String title) {
        if (designation.equalsIgnoreCase("0")) {
            if (lead_phase.equalsIgnoreCase("sales")) {
                if (isAssign.equalsIgnoreCase("0")) {
                    Intent intent = new Intent(this, AssignedSalesDetailActivity.class);
                    intent.putExtra(BMHConstants.ENQUIRY_ID, title);
                    intent.putExtra(BMHConstants.ALARM_INDEX, index);
                    intent.putExtra(BMHConstants.CUSTOMER_NAME, customerName);
                    intent.putExtra(BMHConstants.MOBILE_NO, mobileNo);
                    intent.putExtra("path", "notification");
                    showNotification(intent, description, customerName, mobileNo);
                } else if (isAssign.equalsIgnoreCase("1")) {
                    Intent intent = new Intent(this, FollowUpSalesDetailActivity.class);
                    intent.putExtra(BMHConstants.ENQUIRY_ID, title);
                    intent.putExtra(BMHConstants.ALARM_INDEX, index);
                    intent.putExtra(BMHConstants.CUSTOMER_NAME, customerName);
                    intent.putExtra(BMHConstants.MOBILE_NO, mobileNo);
                    intent.putExtra("path", "notification");
                    showNotification(intent, description, customerName, mobileNo);
                }
            } else {
                //for Sp User pre sales data
                Intent intent = new Intent(this, SpDetailsActivity.class);
                intent.putExtra(BMHConstants.ENQUIRY_ID, title);
                intent.putExtra(BMHConstants.ALARM_INDEX, index);
                intent.putExtra(BMHConstants.CUSTOMER_NAME, customerName);
                intent.putExtra(BMHConstants.MOBILE_NO, mobileNo);
                showNotification(intent, description, customerName, mobileNo);
            }
        } else {
            //for ASm user
            if (lead_phase.equalsIgnoreCase("sales")) {
                if (isAssign.equalsIgnoreCase("1")) {
                    Intent intent = new Intent(this, FollowUpSalesDetailActivity.class);
                    intent.putExtra(BMHConstants.ENQUIRY_ID, title);
                    intent.putExtra(BMHConstants.ALARM_INDEX, index);
                    intent.putExtra(BMHConstants.CUSTOMER_NAME, customerName);
                    intent.putExtra(BMHConstants.MOBILE_NO, mobileNo);
                    intent.putExtra("path", "notification");
                    showNotification(intent, description, customerName, mobileNo);
                }
            } else {
                //For Asm user pre sales data
                Intent intent = new Intent(this, AsmDetailsActivity.class);
                intent.putExtra(BMHConstants.ENQUIRY_ID, title);
                intent.putExtra(BMHConstants.ALARM_INDEX, index);
                intent.putExtra(BMHConstants.CUSTOMER_NAME, customerName);
                intent.putExtra(BMHConstants.MOBILE_NO, mobileNo);
                intent.putExtra("path", "notification");
                showNotification(intent, description, customerName, mobileNo);
            }
        }
        startAlarm();
    }

    public int createID() {
        Date now = new Date();
        int id = Integer.parseInt(new SimpleDateFormat("ddHHmmss", Locale.getDefault()).format(now));
        return id;
    }

    private void showNotification(Intent intent, String currentStatus, String customerName, String mobileNo) {

        String heading = "Your" + " " + currentStatus + " " + "is Schedule";
        String message = new StringBuilder().append(StringUtil.getCamelCase(customerName)).append(" (").append(mobileNo).append(")").toString();

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        int notificationId = createID();
        String channelId = "channel-01";
        String channelName = "Channel Name";
        int importance = NotificationManager.IMPORTANCE_HIGH;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    channelId, channelName, importance);
            notificationManager.createNotificationChannel(mChannel);
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_alarm)
                .setContentTitle(heading)
                .setAutoCancel(true)
                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.mipmap.app_icon_round))
                .setContentText(message)
                .setPriority(Notification.PRIORITY_HIGH);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntent(intent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(
                0,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
        mBuilder.setContentIntent(resultPendingIntent);
        Objects.requireNonNull(notificationManager).notify(notificationId, mBuilder.build());
    }


   /* private void stopAlarm() {
        Intent intentstop = new Intent(NotificationJobIntentService.this, AlarmJobServices.class);
        PendingIntent senderstop = PendingIntent.getBroadcast(NotificationJobIntentService.this, id, intentstop, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager alarmManagerstop = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManagerstop.cancel(senderstop);
    }*/

    public void startAlarm() {
        alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alarmUri == null) {
            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        }
        ringtone = RingtoneManager.getRingtone(this, alarmUri);
        if (!ringtone.isPlaying())
            ringtone.play();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (ringtone.isPlaying())
                    ringtone.stop();
            }
        }, 1000 * BMHConstants.ALARM_DURATION);
    }



    @Override
    public void getCustomerInfoEntity(CustomerInfoEntity customerInfoEntity) {
        if (customerInfoEntity != null) {
            isAssign = customerInfoEntity.getAssignType();
            lead_phase = customerInfoEntity.getLeadPhase();
            if (!TextUtils.isEmpty(isAssign) && !TextUtils.isEmpty(lead_phase)) {
                sendNotification(currentStatus, enquiryId);
            }
        }
    }
}

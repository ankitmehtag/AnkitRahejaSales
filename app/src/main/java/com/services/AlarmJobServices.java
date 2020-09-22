package com.services;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import android.text.TextUtils;

import com.database.entity.PreSalesLeadDetailsEntity;
import com.database.task.GetPreSalesLeadDetailsTask;
import com.database.task.GetSalesLeadDetailTask;
import com.database.entity.SalesLeadDetailEntity;
import com.helper.BMHConstants;
import com.model.NotificationDataModel;
import com.receivers.NotificationReceiver;
import com.sp.BMHApplication;
import com.sp.R;
import com.utils.Utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class AlarmJobServices extends Service implements GetPreSalesLeadDetailsTask.IPreSalesLeadDetailsCommunicator
        , GetSalesLeadDetailTask.ISalesLeadCommunicator {
    private static final int ID_SERVICE = 10001;
    private String alarmTime, currentStatus, alarmDate, enquiryId, customerName, mobileNo;
    private ArrayList<NotificationDataModel> notificationDataModelList;
    PendingIntent pendingIntent;
    private AlarmManager alarmManager;
    private Timer mTimer = null;
    Handler mHandler = new Handler();
    public static final long NOTIFY_INTERVAL = 10000; // 10 seconds
    private String alertDate = "";
    private String alertTime = "";
    private String alertStatus = "";
    private int isUpdated;
    Intent notifyIntent;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        alarmManager = (AlarmManager) this.getSystemService(ALARM_SERVICE);
        startTimer();

        // Create the Foreground Service
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String channelId = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ? createNotificationChannel(notificationManager) : "";
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, channelId);
        Notification notification = notificationBuilder.setAutoCancel(true)
                .setSmallIcon(R.mipmap.icon_sales)
                .setPriority(Notification.PRIORITY_MIN)
                .setCategory(NotificationCompat.CATEGORY_SERVICE)
                .build();
        startForeground(ID_SERVICE, notification);
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private String createNotificationChannel(NotificationManager notificationManager) {
        String channelId = "my_service_channelid";
        String channelName = "My Foreground Service";
        NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
        // omitted the LED color
        channel.setImportance(NotificationManager.IMPORTANCE_NONE);
        channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        notificationManager.createNotificationChannel(channel);
        return channelId;
    }

    private void startTimer() {
        if (mTimer == null) {
            mTimer = new Timer();
        }
        // schedule task
        mTimer.scheduleAtFixedRate(new ScheduleTimerTask(), 0, NOTIFY_INTERVAL);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        if (intent != null) {
            notificationDataModelList = intent.getParcelableArrayListExtra("notification_data_list");
        }
        return START_STICKY;
    }

    private void notifySp(ArrayList<NotificationDataModel> notificationDataModelList) throws InterruptedException {
        Calendar cal = Calendar.getInstance();
        notifyIntent = new Intent(this, NotificationReceiver.class);
        for (int i = 0; i < notificationDataModelList.size(); i++) {
            alarmTime = notificationDataModelList.get(i).getAlert_time();
            alarmDate = notificationDataModelList.get(i).getAlert_date();
            currentStatus = notificationDataModelList.get(i).getStatus();
            enquiryId = notificationDataModelList.get(i).getEnquiry_id();
            customerName = notificationDataModelList.get(i).getCustomerName();
            mobileNo = notificationDataModelList.get(i).getMobileNo();
            isUpdated = notificationDataModelList.get(i).getIsUpdated();
            if (!TextUtils.isEmpty(alarmTime) && !TextUtils.isEmpty(alarmDate)) {
                String time[] = alarmTime.split(":");
                int hour = Integer.parseInt(time[0]);
                int minute = Integer.parseInt(time[1]);
                String dateString[] = alarmDate.split(",");
                // String day = dateString[0];
                String date = String.valueOf(dateString[1]);
                String newDate[] = date.split(" ");
                int month = Utils.convertStringToInt(newDate[1]);
                int alarmDate = Integer.parseInt(newDate[2]);
                int year = Integer.parseInt(newDate[3]);
                cal.set(year, month - 1, alarmDate, hour, minute);
                if (System.currentTimeMillis() == cal.getTimeInMillis() - BMHConstants.ALARM_BEFORE_ONE_HOUR) {
                    notifyIntent.putExtra(BMHConstants.ENQUIRY_ID, enquiryId);
                    notifyIntent.putExtra(BMHConstants.CUSTOMER_NAME, customerName);
                    notifyIntent.putExtra(BMHConstants.MOBILE_NO, mobileNo);
                    notifyIntent.putExtra("current_status", currentStatus);
                    notifyIntent.putExtra(BMHConstants.ALARM_INDEX, i);
                    pendingIntent = PendingIntent.getBroadcast
                            (this, i, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    ///  alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis() - 60 * 60 * 1000, pendingIntent);
                    alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis() - BMHConstants.ALARM_BEFORE_ONE_HOUR, pendingIntent);
                    if (isUpdated == 0) {
                        notificationDataModelList.remove(i);
                    }
                   /*\\ AlarmManager.AlarmClockInfo ac=
                            new AlarmManager.AlarmClockInfo(System.currentTimeMillis()- BMHConstants.ALARM_BEFORE_ONE_HOUR,
                                    pendingIntent);
                    alarmManager.setAlarmClock(ac, pendingIntent);*/
                } else if (System.currentTimeMillis() == cal.getTimeInMillis() + BMHConstants.FIVE_MINUTES_DURATION) {
                    notifyIntent.putExtra(BMHConstants.ENQUIRY_ID, enquiryId);
                    notifyIntent.putExtra(BMHConstants.CUSTOMER_NAME, customerName);
                    notifyIntent.putExtra(BMHConstants.MOBILE_NO, mobileNo);
                    notifyIntent.putExtra("current_status", currentStatus);
                    notifyIntent.putExtra(BMHConstants.ALARM_INDEX, i);
                    pendingIntent = PendingIntent.getBroadcast
                            (this, i, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis() - 60 * 60 * 1000, pendingIntent);
                    if (isUpdated == 0) {
                        // TODO CHANGE REPEAT ALARM DURATION 3 HOURS AFTER TESTING
                        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis() + BMHConstants.FIVE_MINUTES_DURATION,
                                BMHConstants.ALARM_REPEAT_DURATION, pendingIntent);
                         }
                       else {
                       // alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis() + BMHConstants.FIVE_MINUTES_DURATION, pendingIntent);
                        notificationDataModelList.remove(i);
                        }
                }
                       else if (System.currentTimeMillis() == cal.getTimeInMillis() - BMHConstants.ALARM_BEFORE_24_HOUR) {
                    notifyIntent.putExtra(BMHConstants.ENQUIRY_ID, enquiryId);
                    notifyIntent.putExtra(BMHConstants.CUSTOMER_NAME, customerName);
                    notifyIntent.putExtra(BMHConstants.MOBILE_NO, mobileNo);
                    notifyIntent.putExtra("current_status", currentStatus);
                    notifyIntent.putExtra(BMHConstants.ALARM_INDEX, i);
                    pendingIntent = PendingIntent.getBroadcast(this, i, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis() - BMHConstants.ALARM_BEFORE_24_HOUR, pendingIntent);
                    notificationDataModelList.remove(i);
                }
                /*NotificationReceiver myreceiver = new NotificationReceiver();
                IntentFilter intentFilter = new IntentFilter();
                intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
                registerReceiver(myreceiver, intentFilter);*/
            }
            //alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis() - 24 * 60 * 60 * 1000, pendingIntent);
        }
    }

    @Override
    public void getPreSalesLeadsDetail(List<PreSalesLeadDetailsEntity> alarmList) {
        Calendar cal = Calendar.getInstance();
        if (alarmList != null) {
            if (notificationDataModelList != null && notificationDataModelList.size() > 0)
                notificationDataModelList.clear();
            for (int i = 0; i < alarmList.size(); i++) {
                alertTime = alarmList.get(i).getTime();
                alertDate = alarmList.get(i).getDate();
                alertStatus = alarmList.get(i).getCurrentStatus();
                enquiryId = alarmList.get(i).getEnquiryId();
                customerName = alarmList.get(i).getCustomerName();
                mobileNo = alarmList.get(i).getCustomerMobile();
                isUpdated = alarmList.get(i).getIsUpdated();
                filterAlarmList(cal, isUpdated);
            }
            startTimer();
        }
    }

    @Override
    public void getAllSpDetails(List<SalesLeadDetailEntity> alarmList) {
        Calendar cal = Calendar.getInstance();
        if (alarmList != null) {
            if (notificationDataModelList != null && notificationDataModelList.size() > 0)
                notificationDataModelList.clear();
            for (int i = 0; i < alarmList.size(); i++) {
                alertTime = alarmList.get(i).getTime();
                alertDate = alarmList.get(i).getDate();
                alertStatus = alarmList.get(i).getCurrentStatus();
                enquiryId = alarmList.get(i).getEnquiryId();
                customerName = alarmList.get(i).getCustomerName();
                mobileNo = alarmList.get(i).getCustomerMobile();
                isUpdated = alarmList.get(i).getIsUpdated();
                filterAlarmList(cal, isUpdated);
            }
            startTimer();
        }
    }

    private void filterAlarmList(Calendar cal, int isUpdated) {
        if (!alertTime.equalsIgnoreCase("null") && (!TextUtils.isEmpty(alertTime) && !TextUtils.isEmpty(alertDate))) {
            String time[] = alertTime.split(":");
            int hour = Integer.parseInt(time[0]);
            int minute = Integer.parseInt(time[1]);
            String dateString[] = alertDate.split(",");
            String day = dateString[0];
            String date = String.valueOf(dateString[1]);
            String newDate[] = date.split(" ");
            int month = Utils.convertStringToInt(newDate[1]);
            int alarmDate = Integer.parseInt(newDate[2]);
            int year = Integer.parseInt(newDate[3]);
            cal.set(year, month - 1, alarmDate, hour, minute);
            if (System.currentTimeMillis() < cal.getTimeInMillis() - BMHConstants.ALARM_BEFORE_ONE_HOUR) {
                if (notificationDataModelList == null) {
                    notificationDataModelList = new ArrayList<>();
                }
                notificationDataModelList.add(new NotificationDataModel(alertTime, alertDate, alertStatus, enquiryId, customerName, mobileNo, isUpdated));
            }
        }
    }

    class ScheduleTimerTask extends TimerTask {
        @Override
        public void run() {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (notificationDataModelList != null && notificationDataModelList.size() > 0) {
                        try {
                            notifySp(notificationDataModelList);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else {
                        if (BMHApplication.getInstance().getFromPrefs((BMHConstants.USER_DESIGNATION)).equalsIgnoreCase(BMHConstants.ASM_DESIGNATION))
                            new GetPreSalesLeadDetailsTask(AlarmJobServices.this).execute();
                        else
                            new GetSalesLeadDetailTask(AlarmJobServices.this).execute();
                    }
                }
            });
        }
    }

    public void stopRepeatingAlarm(int alarmIndex, Context context) {
        notifyIntent = new Intent(context, NotificationReceiver.class);
        notifyIntent.putExtra("Index", alarmIndex);
        pendingIntent = PendingIntent.getBroadcast(context, alarmIndex, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        if (alarmManager != null)
            alarmManager.cancel(pendingIntent);
    }
}

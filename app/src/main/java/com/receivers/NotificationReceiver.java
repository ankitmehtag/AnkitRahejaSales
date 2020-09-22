package com.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.services.NotificationJobIntentService;

public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //this will sound the alarm tone
        //this will sound the alarm once, if you wish to
        //raise alarm in loop continuously then use MediaPlayer and setLooping(true)
        //this will send a notification message
       /* ComponentName comp = new ComponentName(context.getPackageName(),
                NotificationJobIntentService.class.getName());
        startWakefulService(context, (intent.setComponent(comp)));
        setResultCode(Activity.RESULT_OK);*/
        NotificationJobIntentService.enqueueWork(context, new Intent(context, NotificationJobIntentService.class)
                .putExtras(intent));
    }
}

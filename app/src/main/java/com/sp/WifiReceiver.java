package com.sp;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

/**
 * Created by Naresh on 05-Apr-17.
 */

public class WifiReceiver extends BroadcastReceiver {

    private final String TAG = WifiReceiver.class.getSimpleName();
    private AlertDialog alertDialog;
    private Context context = null;
    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        ConnectivityManager conMan = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMan.getActiveNetworkInfo();
        if (netInfo != null && netInfo.getType() == ConnectivityManager.TYPE_WIFI)
            Log.d(TAG, "Have Wifi Connection");
        if(alertDialog != null){
            alertDialog.dismiss();
        }
        else
            Log.d(TAG, "Don't have Wifi Connection");
        alertDialog = showNetworkErrorDialog(context,onClickListener);
    }


    public static AlertDialog showNetworkErrorDialog (Context context,View.OnClickListener mOnClickListener){
        View mView = null;
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater)context.getSystemService (Context.LAYOUT_INFLATER_SERVICE);
        mView = inflater.inflate(R.layout.network, null);
        alertDialogBuilder.setView(mView);
        alertDialogBuilder.setCancelable(false);
        final AlertDialog alertDialog = alertDialogBuilder.create();
        mView.findViewById(R.id.btn_try_again).setOnClickListener(mOnClickListener);
        alertDialog.show();
        return alertDialog;
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            /*if(alertDialog != null){
                alertDialog.dismiss();
            }*/
            showToast("Network not available.");
        }
    };

    private void showToast(String msg){
        if(context != null) {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
        }
    }
}

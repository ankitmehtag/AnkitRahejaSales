package com.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.sp.R;
import com.helper.BMHConstants;


public class SharePrefUtil {

    public static SharedPreferences getSharePref(Context ctx){
        return ctx.getSharedPreferences(ctx.getResources().getString(R.string.app_name), Context.MODE_PRIVATE);
    }

    public static boolean clearSharePref(Context ctx){
        SharedPreferences mSharedPreferences = ctx.getSharedPreferences(ctx.getResources().getString(R.string.app_name), Context.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.clear();
        return mEditor.commit();
    }


    public static String getEnquiry(Context ctx){
        SharedPreferences mSharedPreferences = ctx.getSharedPreferences(ctx.getResources().getString(R.string.app_name), Context.MODE_PRIVATE);
        return mSharedPreferences.getString(BMHConstants.ADD_ENQUIRY, "");
    }

    public static boolean setEnquiry(Context ctx,String dataList){
        SharedPreferences mSharedPreferences = ctx.getSharedPreferences(ctx.getResources().getString(R.string.app_name), Context.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putString(BMHConstants.ADD_ENQUIRY, dataList);
        return mEditor.commit();
    }


}

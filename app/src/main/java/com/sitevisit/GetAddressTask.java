package com.sitevisit;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.gms.maps.model.LatLng;
import com.sp.R;
import com.appnetwork.ReqRespBean;
import com.helper.ParamsConstants;
import com.utils.BlurBuilder;
import com.views.CustomProgressDialog;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class GetAddressTask extends  AsyncTask<ReqRespBean, Integer, ReqRespBean> {

    private static final String TAG = GetAddressTask.class.getSimpleName();
    private CustomProgressDialog mProgressDialog;
    private Context ctx;

    @Override
    protected void onPreExecute() {
        try {
            if (mProgressDialog != null)
                mProgressDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onPreExecute();
    }
    public GetAddressTask(Context ctx){
        this.ctx = ctx;
    }
    @Override
    protected ReqRespBean doInBackground(ReqRespBean... params) {
        ReqRespBean mReqRespBean = params[0];
        if(mReqRespBean != null && mReqRespBean.getHeader() != null) {
            try {
                double lat = Double.valueOf(mReqRespBean.getHeader().get(ParamsConstants.LAT));
                double lng = Double.valueOf(mReqRespBean.getHeader().get(ParamsConstants.LNG));
                LatLng mLatLng = new LatLng(lat,lng);
                String response = getGeoCoding(ctx,mLatLng);
                mReqRespBean.setJson(response);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        return mReqRespBean;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onCancelled(ReqRespBean reqRespBean) {
        super.onCancelled(reqRespBean);
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }

    @Override
    protected void onPostExecute(ReqRespBean result) {
        try {
            if (mProgressDialog != null) {
                mProgressDialog.dismiss();
                mProgressDialog = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (result != null && result.getmHandler() != null) {
            Message message = new Message();
            message.obj = result;
            result.getmHandler().sendMessage(message);
        } else {
            //TODO: Set data globally or insert into db.
        }
        super.onPostExecute(result);
    }

    public void initProgressDialog(Context context, DialogInterface.OnCancelListener mOnCancelListener) {
        ctx = context;
        // mProgressDialog = new ProgressDialog(context);
        // mProgressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        mProgressDialog = new CustomProgressDialog(context, R.style.progress_dialog_theme);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setOnCancelListener(mOnCancelListener);
        Bitmap map = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.progress_bg);
        Bitmap fast = BlurBuilder.fastblur(map, 0.2f,90);
        final Drawable draw = new BitmapDrawable(ctx.getResources(), fast);
        Window window = mProgressDialog.getWindow();
        window.setBackgroundDrawable(draw);
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        window.setGravity(Gravity.CENTER);

        //mProgressDialog.setMessage("Please wait...");
        //ProgressBar spinner = new android.widget.ProgressBar(context,null,android.R.attr.progressBarStyle);
        //spinner.getIndeterminateDrawable().setColorFilter(0xFFFF0000, android.graphics.PorterDuff.Mode.MULTIPLY);

    }


    private String getGeoCoding(Context ctx,LatLng position ){
        StringBuilder strReturnedAddress = new StringBuilder(" ");
        if(ctx == null || position == null)return strReturnedAddress.toString();
        final Geocoder geocoder = new Geocoder(ctx, Locale.ENGLISH);
        if(geocoder == null || !geocoder.isPresent()){
           return  strReturnedAddress.toString();
        }
        try {
            List<Address> addresses = geocoder.getFromLocation(position.latitude, position.longitude, 1);
            if (addresses != null && addresses.size()>0) {
                Address returnedAddress = addresses.get(0);
                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append(" ");
                }
                return strReturnedAddress.toString();
            } else {
                Log.i(TAG,"No Address returned!");
                return strReturnedAddress.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.i(TAG,"Could not retrive address");
            return strReturnedAddress.toString();
        }
    }

}
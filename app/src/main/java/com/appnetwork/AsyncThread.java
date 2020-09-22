package com.appnetwork;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Message;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.sp.R;
import com.utils.BlurBuilder;

public class AsyncThread extends AsyncTask<ReqRespBean, Integer, ReqRespBean> {

    private ProgressDialog mProgressDialog;
    private Context ctx;

    @Override
    protected void onPreExecute() {

        try {
            if (mProgressDialog != null)
                // new BlurAsyncTask().execute();
                mProgressDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onPreExecute();
    }

    @Override
    protected ReqRespBean doInBackground(ReqRespBean... params) {
        ReqRespBean mReqRespBean = params[0];
        AppNetwork.getInstance().init(mReqRespBean.getCtx());
        if (mReqRespBean.getJson() != null) {
            mReqRespBean.setJson(mReqRespBean.getJson());
        }
        if (!isCancelled()) {
            mReqRespBean = AppNetwork.getInstance().sendHttpRequest(mReqRespBean);
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
        mProgressDialog=new ProgressDialog(context);
        mProgressDialog.setMessage("Please wait...");
        mProgressDialog.show();
    }


    class BlurAsyncTask extends AsyncTask<Void, Integer, Bitmap> {


        protected Bitmap doInBackground(Void... arg0) {
            Bitmap map = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.progress_bg);
            //Bitmap map =  Utils.takeScreenShot((Activity) ctx);
            // Bitmap fast = BlurBuilder.fastblur(map, 100);
            Bitmap fast = BlurBuilder.fastblur(map, 0.2f,90);
            return fast;
        }


        protected void onPostExecute(Bitmap result) {
            if (result != null) {
                final Drawable draw = new BitmapDrawable(ctx.getResources(), result);
                Window window = mProgressDialog.getWindow();
                window.setBackgroundDrawable(draw);
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
                window.setGravity(Gravity.CENTER);
                mProgressDialog.show();
            }
        }
    }
}

package com.sp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Parcelable;

import com.github.mrengineer13.snackbar.SnackBar;
import com.github.mrengineer13.snackbar.SnackBar.OnMessageClickListener;


public class CustomAsyncTask extends AsyncTask<String, Void, Void> {
    private Activity currentContext = null;
    //	private Activity acti = null;
    private AsyncListner bgListner;
    private ProgressDialog dialog;
    private boolean showProgressDialog = true;
    private boolean shouldShowNetworkErrorDialog = true;
    private String logingMsg = "Loading...";
    private SnackBar mSnackBar;

    public String getLogingMsg() {
        return logingMsg;
    }

    public void setLogingMsg(String logingMsg) {
        this.logingMsg = logingMsg;
    }


    public interface AsyncListner {
        public void DoBackgroundTask(String[] url);

        public void OnBackgroundTaskCompleted();

        public void OnPreExec();
    }

    public CustomAsyncTask(Activity context, AsyncListner asyncListner) {
        currentContext = context;
        bgListner = asyncListner;
    }


    public void dontShowProgressDialog() {
        showProgressDialog = false;
    }

    @Override
    protected Void doInBackground(final String... args) {
        //if(getConnectivityStatus()){
        bgListner.DoBackgroundTask(args);

        //}
        return null;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPreExecute() {
        try {
            //if(getConnectivityStatus()){
            bgListner.OnPreExec();
            if (showProgressDialog) {
                dialog = new ProgressDialog(currentContext);
                dialog.setMessage("Please wait...");
                dialog.show();
            }
        } catch (Exception e) {
            // TODO: handle exception
            bgListner.OnPreExec();
        }

    }

    @Override
    public void onCancelled() {

        super.onCancelled();
        if (dialog != null && dialog.isShowing())
            dialog.dismiss();
    }

    protected void onPostExecute(final Void unused) {
        if (!isCancelled()) {
            if (dialog != null && dialog.isShowing())
                dialog.dismiss();
            //if(getConnectivityStatus()){
            bgListner.OnBackgroundTaskCompleted();
            //}
        }
    }

    private void showNetworkSnackBar() {
        mSnackBar = new SnackBar.Builder(currentContext)
                .withOnClickListener(new OnMessageClickListener() {
                    @Override
                    public void onMessageClick(Parcelable token) {
//				mSnackBar.clear(true);
                    }
                })
                .withMessage("Network not available. Check your connection and try again.      ")
                .withActionMessage("OK")
                .withStyle(SnackBar.Style.INFO)
                .withBackgroundColorId(R.color.sb__snack_bkgnd)
                .withDuration(SnackBar.PERMANENT_SNACK)
                .show();
    }


    public void execute(String params) {
//			if(Build.VERSION.SDK_INT >= 11)
        super.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");
//			else
//				super.execute("");
    }

}

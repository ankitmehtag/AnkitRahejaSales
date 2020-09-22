package com.appnetwork;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Parcelable;

import com.github.mrengineer13.snackbar.SnackBar;
import com.github.mrengineer13.snackbar.SnackBar.OnMessageClickListener;
import com.sp.R;


public class CustomAsyncTask extends AsyncTask<String, Void, Void> {
    private Activity currentContext = null;
    //	private Activity acti = null;
    private AsyncListner bgListner;
    private ProgressDialog mProgressDialog = null;
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
				/*	mProgressDialog = ProgressDialog.show(currentContext, "",getLogingMsg()+"\t\t\t\t\t", true, false);
//					mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
					mProgressDialog.setCanceledOnTouchOutside(false);
					mProgressDialog.setCancelable(false);*/

				/*	mProgressDialog = new CustomProgressDialog(currentContext, R.style.progress_dialog_theme);
					mProgressDialog.setCancelable(true);
					//Bitmap map = BitmapFactory.decodeResource(currentContext.getResources(), R.drawable.progress_bg);
					//Bitmap fast = BlurBuilder.fastblur(map, 0.2f,90);
					//final Drawable draw = new BitmapDrawable(currentContext.getResources(), fast);
					Window window = mProgressDialog.getWindow();
				//	window.setBackgroundDrawableResource(R.drawable.progress_bg);
					window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
					window.setGravity(Gravity.CENTER);
					mProgressDialog.show();*/
                mProgressDialog = new ProgressDialog(currentContext);
                mProgressDialog.setMessage("Please wait...");
                mProgressDialog.show();
            }
            //}
			/*else{
				if(shouldShowNetworkErrorDialog)
				showNetworkSnackBar();
			}*/
        } catch (Exception e) {
            // TODO: handle exception
            bgListner.OnPreExec();
        }

    }

    @Override
    public void onCancelled() {

        super.onCancelled();
        if (mProgressDialog != null && mProgressDialog.isShowing())
            mProgressDialog.dismiss();
    }

    protected void onPostExecute(final Void unused) {
        if (!isCancelled()) {
            if (mProgressDialog != null && mProgressDialog.isShowing())
                mProgressDialog.dismiss();
            //if(getConnectivityStatus()){
            bgListner.OnBackgroundTaskCompleted();
            //}
        }
    }

    public void dismissLoading() {
        if (mProgressDialog != null && mProgressDialog.isShowing())
            mProgressDialog.dismiss();
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

//	private void showNetworkErrorDialog() {
//		AlertDialog.Builder alertDialog = new AlertDialog.Builder(currentContext);
//		// Setting Dialog Title
//		alertDialog.setTitle("Network error");
//		// Setting Dialog Message
//		alertDialog.setMessage(R.string.network_not_available);
//		// Setting Icon to Dialog
//		alertDialog.setIcon(R.drawable.app_icon);
//		// Setting Positive "Yes" Button
//		alertDialog.setPositiveButton("OK",
//				new DialogInterface.OnClickListener() {
//					public void onClick(DialogInterface dialog, int which) {
////						if (finishActivity) {
////							finish();
////						} else {
////							dialog.cancel();
////							if (customDialog != null)
//						dialog.cancel();
////						}
//					}
//				});
//		// Showing Alert Message
//		alertDialog.show();
//	}
//	

    public boolean getConnectivityStatus() {
        if (currentContext == null) return false;
        ConnectivityManager connManager = (ConnectivityManager) currentContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connManager.getActiveNetworkInfo();
        if (info != null)
            if (info.isConnected()) {
                return true;
            } else {
                return false;
            }
        else
            return false;
    }


    public void dontsShowNetworkErrorDialog() {
        this.shouldShowNetworkErrorDialog = false;
    }


    public void execute(String params) {
//			if(Build.VERSION.SDK_INT >= 11)
        super.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");
//			else
//				super.execute("");
    }

}

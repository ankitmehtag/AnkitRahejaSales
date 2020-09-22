package com.sp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Point;
import android.os.Build;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.helper.BMHConstants;
import com.interfaces.OnDialogClickListener;
import com.utils.TypefaceUtil;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;

public class BMHApplication extends Application
        {
    //public SessionTracker mSessionTracker;
    //public Session mCurrentSession;
    private static BMHApplication mInstance;
    public static final String TAG = BMHApplication.class.getSimpleName();
    private RequestQueue mRequestQueue;

    @Override
    public void onCreate()
         {
        super.onCreate();
        //Fabric.with(this, new Crashlytics());
        mInstance = this;
        //Fabric.with(this, new Crashlytics());
        TypefaceUtil.overrideFont(getApplicationContext(), "MONOSPACE", "fonts/regular.ttf");
        // font from assets: "assets/fonts/Roboto-Regular.ttf
        //printHashKey();
    }

    public static synchronized BMHApplication getInstance() {
        return mInstance;
    }

    void showToast(String message) {
        Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public void showToastAtCenter(Activity acti, String message) {
        Toast toast = Toast.makeText(acti, message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public void shakeEdittext(EditText editText) {
        Animation shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake_anim);
        editText.startAnimation(shake);
    }

    void showMesageDialog(final Activity acti, final String message, final boolean finishActivity,
                          final OnDialogClickListener dialogClickListener) {

        acti.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!acti.isFinishing()) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(acti);
                    alertDialog.setTitle(acti.getString(R.string.app_name));
                    alertDialog.setMessage(message);
                    alertDialog.setIcon(R.drawable.app_icon_img);
                    alertDialog.setPositiveButton("OK", new OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            if (finishActivity) {
                                acti.finish();
                            } else {
                                if (dialogClickListener != null) {
                                    dialogClickListener.onPositiveButtonClicked();
                                }
                                dialog.cancel();
                            }
                        }
                    });
                    alertDialog.setNegativeButton("Cancel", new OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (dialogClickListener != null) {
                                dialogClickListener.onPositiveButtonClicked();
                            } else
                                dialog.cancel();
                        }
                    });
                    // Showing Alert Message
                    alertDialog.show();
                }
            }
        });

    }

    public String getThumbUrlViaId(String id) {
        return "http://img.youtube.com/vi/" + id + "/0.jpg";
    }

    void showNetworkErrorDialog(final Activity acti) {

        acti.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!acti.isFinishing()) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(acti);
                    // Setting Dialog Title
                    alertDialog.setTitle("Network error");
                    // Setting Dialog Message
                    alertDialog.setMessage("Network not available");
                    // Setting Icon to Dialog
                    alertDialog.setIcon(R.drawable.app_icon_img);
                    // Setting Positive "Yes" Button
                    alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // if (finishActivity) {
                            // finish();
                            // } else {
                            // dialog.cancel();
                            // if (customDialog != null)
                            dialog.cancel();
                            // }
                        }
                    });
                    // Showing Alert Message
                    alertDialog.show();
                }
            }
        });
    }

    public void saveTabIntoPrefs(String key, String value) {
        SharedPreferences prefs = getSharedPreferences(BMHConstants.PREF_TAB_NAME, MODE_PRIVATE);
        Editor edit = prefs.edit();
        edit.putString(key, value);
        edit.commit();
    }

    public String getTabFromPrefs(String key)
             {

        SharedPreferences prefs = getSharedPreferences(BMHConstants.PREF_TAB_NAME, MODE_PRIVATE);
        return prefs.getString(key, BMHConstants.DEFAULT_VALUE);

                  }

    public void clearTabPref()
             {
        SharedPreferences prefs = getSharedPreferences(BMHConstants.PREF_TAB_NAME, MODE_PRIVATE);
        Editor preferencesEditor = prefs.edit();
        preferencesEditor.clear();
        preferencesEditor.apply();


          }

    public void saveIntoPrefs(String key, String value)
          {
        SharedPreferences prefs = getSharedPreferences(BMHConstants.PREF_NAME, MODE_PRIVATE);
        Editor edit = prefs.edit();
        edit.putString(key, value);
        edit.commit();

       }

    public void saveIntoPrefs(String key, int value) {
        SharedPreferences prefs = getSharedPreferences(BMHConstants.PREF_NAME, MODE_PRIVATE);
        Editor edit = prefs.edit();
        edit.putInt(key, value);
        edit.commit();

    }

    public void saveIntoPrefs(String key, boolean value) {
        SharedPreferences prefs = getSharedPreferences(BMHConstants.PREF_NAME, MODE_PRIVATE);
        Editor edit = prefs.edit();
        edit.putBoolean(key, value);
        edit.commit();

    }

    public String getFromPrefs(String key) {
        SharedPreferences prefs = getSharedPreferences(BMHConstants.PREF_NAME, MODE_PRIVATE);
        return prefs.getString(key, BMHConstants.DEFAULT_VALUE);
    }

    public int getIntFromPrefs(String key) {
        SharedPreferences prefs = getSharedPreferences(BMHConstants.PREF_NAME, MODE_PRIVATE);
        return prefs.getInt(key, -1);
    }

    public boolean getBooleanFromPrefs(String key) {
        SharedPreferences prefs = getSharedPreferences(BMHConstants.PREF_NAME, MODE_PRIVATE);
        return prefs.getBoolean(key, false);
    }

    public int getWidth(Activity acti) {
        int measuredWidth = 0;
        Point size = new Point();
        WindowManager w = acti.getWindowManager();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            w.getDefaultDisplay().getSize(size);
            measuredWidth = size.x;
        } else {
            Display d = w.getDefaultDisplay();
            measuredWidth = d.getWidth();
        }
        return measuredWidth;
    }

    public int getHeight(Activity acti) {
        int measuredHeight = 0;
        Point size = new Point();
        WindowManager w = acti.getWindowManager();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            w.getDefaultDisplay().getSize(size);
            measuredHeight = size.y;
        } else {
            Display d = w.getDefaultDisplay();
            measuredHeight = d.getHeight();
        }
        return measuredHeight;
    }

    /*public void showSnackBar(Activity acti, String message, String buttonTitle, final SnackBarCallback listner) {
        new SnackBar.Builder(acti).withOnClickListener(new OnMessageClickListener() {
            @Override
            public void onMessageClick(Parcelable token) {
                listner.onActionButtonClick(token);
            }
        }).withMessage(message).withActionMessage(buttonTitle).withStyle(SnackBar.Style.ALERT)
                .withBackgroundColorId(R.color.sb__snack_bkgnd).withDuration(SnackBar.PERMANENT_SNACK).show();
    }

    public void showSnackBarError(Activity acti, final SnackBarCallback listner) {
        new SnackBar.Builder(acti).withOnClickListener(new OnMessageClickListener() {
            @Override
            public void onMessageClick(Parcelable token) {
                listner.onActionButtonClick(token);
            }
        }).withMessage("Something went wrong, Please Try again.").withActionMessage("Retry")
                .withStyle(SnackBar.Style.ALERT).withBackgroundColorId(R.color.sb__snack_bkgnd)
                .withDuration(SnackBar.PERMANENT_SNACK).show();
    }

    public void showSnackBar(Activity acti, Parcelable parc, final SnackBarCallback listner) {
        new SnackBar.Builder(acti).withToken(parc).withOnClickListener(new OnMessageClickListener() {
            @Override
            public void onMessageClick(Parcelable token) {
                listner.onActionButtonClick(token);
            }
        }).withMessage("Something went wrong, Please Try again.").withActionMessage("Retry")
                .withStyle(SnackBar.Style.ALERT).withBackgroundColorId(R.color.sb__snack_bkgnd)
                .withDuration(SnackBar.PERMANENT_SNACK).show();

    }

    public void showSnackBar(Activity acti, String message) {
        new SnackBar.Builder(acti).withOnClickListener(new OnMessageClickListener() {
            @Override
            public void onMessageClick(Parcelable token) {
            }
        }).withMessage(message).withActionMessage("OK")

                .withStyle(SnackBar.Style.CONFIRM).withBackgroundColorId(R.color.sb__snack_bkgnd)
                .withDuration(SnackBar.PERMANENT_SNACK)

                .show();
    }

    public void showSnackBar(Activity acti, String message, short duration) {
        new SnackBar.Builder(acti).withOnClickListener(new OnMessageClickListener() {
            @Override
            public void onMessageClick(Parcelable token) {

            }
        }).withMessage(message).withActionMessage("OK").withStyle(SnackBar.Style.CONFIRM)
                .withBackgroundColorId(R.color.sb__snack_bkgnd).withDuration(duration).show();
    }
*/
    public String getDecimalFormatedPrice(double price) {
        String pattern = "##.##";
        DecimalFormat decimalFormat = new DecimalFormat(pattern);

        String a = "0";
        double b = 0;
        if (price == 0) {
            a = "0";
        } else if (price >= 1000 && price < 100000) {
            a = price + "";
            b = price / 1000f;
            a = decimalFormat.format(b) + " K";
        } else if (price >= 100000 && price < 10000000) {
            b = price / 100000f;
            a = decimalFormat.format(b) + " Lacs+";
        } else if (price >= 10000000) {
            b = price / 10000000f;
            a = decimalFormat.format(b) + " Cr+";
        }

        return a;
    }

    public void hideSoftKeyboard(EditText editText) {
        editText.clearFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);

    }

    /**
     * Shows the soft keyboard
     */
    public void showSoftKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        view.requestFocus();
        inputMethodManager.showSoftInput(view, 0);
    }

    public void shakeEdittext(String first_name) {
        // TODO Auto-generated method stub

    }

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }

    public void printHashKey() {
        // Add code to print out the key hash
        try {
            PackageInfo info = getPackageManager().getPackageInfo("com.builder", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }


    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
}

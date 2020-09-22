package com.appnetwork;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.helper.BMHConstants;
import com.helper.ParamsConstants;
import com.sp.BMHApplication;

import java.util.HashMap;
import java.util.Map;

public class VolleyRequestApi {

    private String mNotificationId;
    private String apiUrl;
    private Context mContext;
    public ProgressDialog dialog;

    public VolleyRequestApi(Context mContext) {
        this.mContext = mContext;
    }

    public VolleyRequestApi(String mNotificationId, String apiUrl, Context mContext) {
        this.mNotificationId = mNotificationId;
        this.apiUrl = apiUrl;
        this.mContext = mContext;
    }

    public void updateNotificationReadStatus() {

        dialog = new ProgressDialog(mContext);
        dialog.setMessage("Please wait...");
        dialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, apiUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                            dialog = null;
                        }
                        Log.d("Response", response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                            dialog = null;
                        }
                        // error
                        Log.d("Error.Response", "" + error);
                    }
                }
        ) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(ParamsConstants.USER_ID, BMHApplication.getInstance().getFromPrefs(BMHConstants.USERID_KEY));
                params.put(ParamsConstants.BUILDER_ID, BMHConstants.CURRENT_BUILDER_ID);
                params.put(ParamsConstants.NOTIFICATION_ID, mNotificationId);
                params.put(ParamsConstants.USER_TYPE, BMHConstants.SALES_PERSON);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap();
                headers.put("Content-Type", WEBAPI.contentTypeFormData);
                // headers.put("apiKey", "xxxxxxxxxxxxxxx");
                return headers;
            }
        };
        BMHApplication.getInstance().addToRequestQueue(stringRequest, "headerRequest");
    }
}

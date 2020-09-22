package com.database.task;

import android.content.Context;
import android.os.AsyncTask;

import com.database.AppDatabase;
import com.database.entity.CallRecordingEntity;
import com.database.entity.UniversalContactsEntity;

import java.util.List;

public class SyncRecordingTask extends AsyncTask<Void, String, Void> {

    Context context;
    List<CallRecordingEntity> recordingEList;
    ISyncRecordingCommunicator communicator;

    public SyncRecordingTask(Context context) {
        this.context = context;
        this.communicator = (ISyncRecordingCommunicator) context;

    }

    @Override
    protected Void doInBackground(Void... voids) {
        recordingEList = AppDatabase.getInstance().getCallRecordingDao().getRecordingToSync(1);
        return null;
    }

    @Override
    protected void onPostExecute(Void s) {
        super.onPostExecute(s);
        if (recordingEList.size() > 0 )
            communicator.callbackCallRecordings(recordingEList);
    }

    public interface ISyncRecordingCommunicator {
        void callbackCallRecordings(List<CallRecordingEntity> recordingEList);
    }

    /*private void syncCallRecordingFile(final String enquiryId, final String mobileNo, final String filePath, final String timeStamp) {

        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, WEBAPI.getWEBAPI(APIType.SYNC_CALL_RECORDING),
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        String resultResponse = new String(response.data);
                        try {
                            JSONObject result = new JSONObject(resultResponse);

                            if (result.optBoolean("status")) {
                                Toast.makeText(context, result.optString("message"), Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(context, result.optString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                String errorMessage = "Unknown error";
                if (networkResponse == null) {
                    if (error.getClass().equals(TimeoutError.class)) {
                        errorMessage = "Request timeout";
                    } else if (error.getClass().equals(NoConnectionError.class)) {
                        errorMessage = "Failed to connect server";
                    }
                } else {
                    String result = new String(networkResponse.data);
                    try {
                        JSONObject response = new JSONObject(result);
                        String status = response.getString("status");
                        String message = response.getString("message");

                        Log.e("Error Status", status);
                        Log.e("Error Message", message);

                        if (networkResponse.statusCode == 404) {
                            errorMessage = "Resource not found";
                        } else if (networkResponse.statusCode == 401) {
                            errorMessage = message + " Please login again";
                        } else if (networkResponse.statusCode == 400) {
                            errorMessage = message + " Check your inputs";
                        } else if (networkResponse.statusCode == 500) {
                            errorMessage = message + " Something is getting wrong";
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                Log.i("Error", errorMessage);
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(BMHConstants.ENQUIRY_ID, enquiryId);
                params.put(BMHConstants.MOBILE_NO, mobileNo);
                //   params.put(BMHConstants.RECORDING_FILE_PATH, filePath);
                params.put(BMHConstants.TIME_STAMP, timeStamp);
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                // file name could found file base or direct access from real path
                // for now just get bitmap data from ImageView
                params.put("uploadedfile", new DataPart(new File(filePath).getName(), getFileInBytes(filePath)));

                return params;
            }

            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap();
                //  headers.put("Content-Type", WEBAPI.contentTypeAudio);
                return headers;
            }
        };

        BMHApplication.getInstance().addToRequestQueue(multipartRequest, "call_recording_sync");
    }*/
}

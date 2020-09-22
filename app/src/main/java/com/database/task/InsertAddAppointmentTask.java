package com.database.task;

import android.content.Context;
import android.os.AsyncTask;

import com.database.AppDatabase;
import com.database.entity.AddAppointmentEntity;
import com.google.gson.Gson;
import com.helper.BMHConstants;
import com.helper.ParamsConstants;
import com.sp.BMHApplication;
import com.sp.R;
import com.utils.Connectivity;
import com.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class InsertAddAppointmentTask extends AsyncTask<Void, Void, Boolean> {

    private AddAppointmentEntity entity;
    private Context context;
    private List<AddAppointmentEntity> addAppointList;
    private InsertAddAppointmentTask.IAppointmentCommunicator communicator;
    JSONObject appointJObj;
    JSONArray appointJArray;
    private BMHApplication app;
    Gson gson;
    JSONObject jsonObj = new JSONObject();

    public InsertAddAppointmentTask(Context context, AddAppointmentEntity entity) {
        this.context = context;
        this.entity = entity;
    }

    public InsertAddAppointmentTask(Context context) {
        this.context = context;
        communicator = (InsertAddAppointmentTask.IAppointmentCommunicator) context;
        gson = new Gson();
        app = BMHApplication.getInstance();
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        if (entity != null) {
            AppDatabase.getInstance().getAddAppointmentDao().insert(entity);
            return true;
        } else {
            addAppointList = AppDatabase.getInstance().getAddAppointmentDao().getAppointmentsToSync(1);
            try {
                jsonObj.put(ParamsConstants.USER_ID, app.getFromPrefs(BMHConstants.USERID_KEY));
                jsonObj.put(ParamsConstants.BUILDER_ID, BMHConstants.CURRENT_BUILDER_ID);
                jsonObj.put(ParamsConstants.USER_DESIGNATION, BMHApplication.getInstance().getFromPrefs((BMHConstants.USER_DESIGNATION)));

                if (addAppointList != null) {
                    AddAppointmentEntity appointEntity;
                    int appointSize = addAppointList.size();
                    appointJArray = new JSONArray();
                    for (int i = 0; i < appointSize; i++) {
                        appointEntity = addAppointList.get(i);
                        appointJObj = new JSONObject();
                        String entString = gson.toJson(appointEntity);
                        appointJObj.put("add_appointment", entString);
                        appointJArray.put(i, appointJObj);
                    }
                    jsonObj.put("add_appointment_list", appointJArray);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    protected void onPostExecute(Boolean bool) {
        super.onPostExecute(bool);
        if (!Connectivity.isConnected(context) && bool) {
            Utils.showToast(context, context.getString(R.string.saved_data_locally));
        } else if (entity == null) {
            communicator.getSyncAppointList(jsonObj);
        }
    }

    public interface IAppointmentCommunicator {
        void getSyncAppointList(JSONObject jsonObject);
    }
}

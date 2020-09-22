package com.database.task;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.activities.AlertDialogActivity;
import com.database.AppDatabase;
import com.database.entity.CallRecordingEntity;
import com.sp.R;
import com.utils.Utils;

import java.lang.ref.WeakReference;

public class CallRecordingTask extends AsyncTask<Void, Void, Boolean> {

    private CallRecordingEntity mRecordingEntity;
    private Context context;

    public CallRecordingTask(Context context, CallRecordingEntity recordingEntity) {
        this.mRecordingEntity = recordingEntity;
        this.context = context;

           }

    // doInBackground methods runs on a worker thread
    @Override
    protected Boolean doInBackground(Void... objs) {
        if (mRecordingEntity != null) {
            AppDatabase.getInstance().getCallRecordingDao().insertCallRecording(mRecordingEntity);
            return true;
        }

        return false;
    }

    // onPostExecute runs on main thread
    @Override
    protected void onPostExecute(Boolean bool) {

        if (TextUtils.isEmpty(mRecordingEntity.getEnquiryId()))
            Utils.showToast(context, context.getString(R.string.remark_added_success));
    }
}

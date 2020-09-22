package com.database.task;

import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.database.AppDatabase;
import com.helper.BMHConstants;
import com.sp.BMHApplication;
import com.utils.Utils;

import org.json.JSONObject;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;

public class UpdateCallRecordingTask extends AsyncTask<Void, Void, String> {
    private JSONObject jsonObject;

    public UpdateCallRecordingTask(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    @Override
    protected String doInBackground(Void... voids) {
        if (jsonObject.optBoolean("success")) {
            JSONObject object = jsonObject.optJSONObject("data");
            String audioFileName = object.optString("file_name");
            String recDir = BMHApplication.getInstance().getFromPrefs(BMHConstants.DIR_CALL_RECORDING);
            if (!TextUtils.isEmpty(recDir) && !recDir.equalsIgnoreCase("") && !recDir.equalsIgnoreCase(null)) {
                File fileDir = new File(recDir);
                // DELETE SYNC CALL RECORDING FILES FROM INTERNAL STORAGE FOLDER
                if (!Utils.isMemorySpaceAvailable(Utils.getAvailableInternalMemorySize(), Utils.getTotalInternalMemorySize())) {
                    File[] files = fileDir.listFiles();


                    Arrays.sort(files, new Comparator() {
                        public int compare(Object o1, Object o2) {
                            if (((File) o1).lastModified() > ((File) o2).lastModified()) {
                                return -1;
                            } else if (((File) o1).lastModified() < ((File) o2).lastModified()) {
                                return +1;
                            } else {
                                return 0;
                            }
                        }
                    });
                   /* int size= files.length;

                    if(size>9){
                        for (int i = 9; i < size; i++) {
                            files[i].delete();
                        }
                    }*/

                    for (File file : files) {
                        if (null != file) {
                            long lastModified = file.lastModified();
                            if (0 < lastModified) {
                                Date lastMDate = new Date(lastModified);
                                Date today = new Date(System.currentTimeMillis());
                                if (null != lastMDate && null != today) {
                                    long diff = today.getTime() - lastMDate.getTime();
                                    long diffDays = diff / (24 * 60 * 60 * 1000);
                                    if (0 < diffDays) {
                                        file.delete();
                                        Log.d("DELETED FILE", file.getAbsolutePath());
                                    }
                                }
                            }
                        }
                    }
                }

            }
            AppDatabase.getInstance().getCallRecordingDao().deleteRecordByFile(audioFileName);
        }
        return null;
    }
}

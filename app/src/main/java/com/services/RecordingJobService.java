package com.services;

import android.app.job.JobParameters;
import android.app.job.JobService;

public class RecordingJobService extends JobService{
    private static final String TAG = "RecordingSyncService";

    @Override
    public boolean onStartJob(JobParameters params) {
        //Intent service = new Intent(getApplicationContext(), CallRecordingService.class);
       // getApplicationContext().startService(service);
      //  Utils.scheduleJob(getApplicationContext()); // reschedule the job
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }
}

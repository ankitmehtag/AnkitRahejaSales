package com.services;

import android.app.Service;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.IBinder;
import androidx.annotation.Nullable;

import com.helper.BMHConstants;
import com.sp.BMHApplication;

import java.io.IOException;

public class CallRecordingService extends Service {

    private MediaRecorder mRecorder = null;
    private String savingPath;
    boolean isRecordStarted = false;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        if (intent != null) {
            savingPath = intent.getStringExtra("OUTPUT_PATH");
            BMHApplication.getInstance().saveIntoPrefs(BMHConstants.RECORDING_FILE_PATH, savingPath);
            startRecording();
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        stopRecording();
        super.onDestroy();
    }

    // this process must be done prior to the start of recording
    private void resetRecorder() {
        mRecorder.reset();
     /*   mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        } else {
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        }
        //  mRecorder.setAudioEncodingBitRate(12200);
        //  mRecorder.setAudioSamplingRate(8000);
        mRecorder.setOutputFile(savingPath);

        try {
            mRecorder.prepare();
        } catch (IllegalStateException | IOException e) {
            e.printStackTrace();
            mRecorder = null;
        }
    }

    private void startRecording() {
        if (mRecorder != null) {
            mRecorder.stop();
            mRecorder.release();
        }
        mRecorder = new MediaRecorder();
        resetRecorder();
        try {
            // Sometimes prepare takes some time to complete
            Thread.sleep(2000);
            if (!isRecordStarted) {
                mRecorder.start();
                isRecordStarted = true;
            } else if (isRecordStarted) {
                stopRecording();
            }
        } catch (InterruptedException | IllegalStateException e) {
            e.printStackTrace();
            /*if (mRecorder != null) {
                mRecorder.start();
                isRecordStarted = true;
            } else if (isRecordStarted) {
                isRecordStarted = false;
                stopRecording();
            }*/
        }
    }

    public void stopRecording() {
        try {
            if (mRecorder != null && isRecordStarted) {
                mRecorder.stop();
                mRecorder.reset();
                mRecorder.release();
                mRecorder = null;
                isRecordStarted = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

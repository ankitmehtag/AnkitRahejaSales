package com.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import androidx.annotation.Nullable;
import android.util.Log;

import com.adapters.MySyncAdapter;

public class MySyncService extends Service {

    // Storage for an instance of the sync adapter
    private static MySyncAdapter mSyncAdapter = null;
    // Object to use as a thread-safe lock
    private static final Object mSyncAdapterLock = new Object();


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d("MyServiceSync", "onBind");
        return mSyncAdapter.getSyncAdapterBinder();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    @Override
    public void onCreate() {
        super.onCreate();
        synchronized (mSyncAdapterLock) {
            if (mSyncAdapter == null) {
                mSyncAdapter = new MySyncAdapter(getApplicationContext(), true);
            }
        }
    }
}

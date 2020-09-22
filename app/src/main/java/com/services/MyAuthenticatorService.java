package com.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.utils.MyAuthenticator;


public class MyAuthenticatorService extends Service {
    // Instance field that stores the authenticator object
    private MyAuthenticator mAuthenticator;

    @Override
    public void onCreate() {
        Log.d("MyAuthenticatorService", "onCreate");
        // Create a new authenticator object
        mAuthenticator = new MyAuthenticator(this);
    }

    /*
     * When the system binds to this Service to make the RPC call
     * return the authenticator's IBinder.
     */
    @Override
    public IBinder onBind(Intent intent) {
        Log.d("MyAuthenticatorService", "onBind");
        return mAuthenticator.getIBinder();
    }
}

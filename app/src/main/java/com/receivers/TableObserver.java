package com.receivers;

import android.accounts.Account;
import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;


public class TableObserver extends ContentObserver {

    Account mAccount;
    /**
     * Creates a content observer.
     *
     * @param handler The handler to run {@link #onChange} on, or null if none.
     */
    public TableObserver(Context handler) {
        super(null);
     //   this.mAccount = Splash1Activity.mAccount;
    }

    /*
     * Define a method that's called when data in the
     * observed content provider changes.
     * This method signature is provided for compatibility with
     * older platforms.
     */
    @Override
    public void onChange(boolean selfChange) {
        /*
         * Invoke the method signature available as of
         * Android platform version 4.1, with a null URI.
         */
        onChange(selfChange, null);
    }

    /*
     * Define a method that's called when data in the
     * observed content provider changes.
     */
    @Override
    public void onChange(boolean selfChange, Uri changeUri) {
        /*
         * Ask the framework to run your sync adapter.
         * To maintain backward compatibility, assume that
         * changeUri is null.
         */
     //   ContentResolver.requestSync(mAccount, AUTHORITY, null);
    }
}

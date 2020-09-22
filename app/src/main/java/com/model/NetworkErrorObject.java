package com.model;

import android.app.AlertDialog;

import com.AppEnums.UIEventType;

/**
 * Created by Naresh on 26-Dec-16.
 */

public class NetworkErrorObject {
    private UIEventType uiEventType;
    private AlertDialog alertDialog;

    public NetworkErrorObject(UIEventType curEvent, AlertDialog alertDialog) {
        uiEventType = curEvent;
        this.alertDialog = alertDialog;
    }

    public UIEventType getUiEventType() {
        return uiEventType;
    }

    public void setUiEventType(UIEventType uiEventType) {
        this.uiEventType = uiEventType;
    }

    public AlertDialog getAlertDialog() {
        return alertDialog;
    }

    public void setAlertDialog(AlertDialog alertDialog) {
        this.alertDialog = alertDialog;
    }
}

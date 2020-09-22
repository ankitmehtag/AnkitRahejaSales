package com.helper;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Network {
	public static boolean getConnectivityStatus(Context context) {
		ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo info = connManager.getActiveNetworkInfo();
		if (info != null)
			if (info.isConnected()) {
				return true;
			} else {
				return false;
			}
		else
			return false;
	}
}

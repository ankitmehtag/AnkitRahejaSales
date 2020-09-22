package com.sp;

import android.app.Activity;
import android.app.DownloadManager;
import android.app.DownloadManager.Query;
import android.app.DownloadManager.Request;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.helper.BMHConstants;
import com.helper.UrlFactory;
import com.utils.Utils;

import java.io.File;

public class DownloadService extends Service{

	private static final int ID_SERVICE = 2001;
	private long enqueue;
    private DownloadManager dm;
    private String fileName = "brochure.pdf";
    private BroadcastReceiver receiver;
    protected BMHApplication app;
	private final String TAG = DownloadService.class.getSimpleName();
    String url;
    
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		// Create the Foreground Service
		NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		String channelId = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ? Utils.createNotificationChannel(notificationManager) : "";
		NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, channelId);
		Notification notification = notificationBuilder.setAutoCancel(true)
				.setSmallIcon(R.mipmap.icon_sales)
				.setPriority(Notification.PRIORITY_MIN)
				.setCategory(NotificationCompat.CATEGORY_SERVICE)
				.build();
		startForeground(ID_SERVICE, notification);

		app =(BMHApplication) getApplication();
		 receiver = new BroadcastReceiver() {
	        @Override
	        public void onReceive(Context context, Intent intent) {
	            String action = intent.getAction();
	            if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
	                long downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);
	                Query query = new Query();
	                query.setFilterById(enqueue);
	                Cursor c = dm.query(query);
	                if (c.moveToFirst()) {
	                    int columnIndex = c.getColumnIndex(DownloadManager.COLUMN_STATUS);
	                    if (DownloadManager.STATUS_SUCCESSFUL == c.getInt(columnIndex)) {
	                    	Toast.makeText(DownloadService.this, "File downloaded", Toast.LENGTH_SHORT).show();
//	                    	String pdfPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath()+"/"+fileName;
	                    	File f = new File(dm.getUriForDownloadedFile(enqueue).getPath());
	                    	if(f.exists()){
	                    		System.out.println("hh hai file");
	                    	}
	                    	dm.addCompletedDownload(fileName, "Project Brocher", true, "application/pdf", f.getAbsolutePath(), f.length(), true);
//	                    	String pdfPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
//	                    	Intent target = new Intent(Intent.ACTION_VIEW);
//	                        target.setDataAndType(Uri.parse(pdfPath + fileName), "application/pdf");
//	                        startActivity(target);
	                    	SharedPreferences prefs = getSharedPreferences(BMHConstants.PREF_NAME, MODE_PRIVATE);
	            			Editor edit = prefs.edit();
	            			edit.putBoolean(BMHConstants.IS_DOWNLOADING_KEY, false);
	            			edit.commit();
	            			
	                    	Intent broad = new Intent(BMHConstants.ACTION_DOWNLOAD_COMPLETE);
	                    	LocalBroadcastManager.getInstance(DownloadService.this).sendBroadcast(broad);
	                    	DownloadService.this.stopSelf();
	                    }
	                }
	            }
	        }
	    };
	    
	    registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
	    
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		String s = intent.getStringExtra("pdfname");
		String tempUrl = Utils.removeLastSlashSign(intent.getStringExtra("pdfurl"));
		String separator = UrlFactory.IMG_BASEURL.endsWith("/") ? "" : "/";
		url = tempUrl.startsWith(UrlFactory.IMG_BASEURL) ? tempUrl : UrlFactory.IMG_BASEURL + separator + tempUrl;
		System.out.println("hh on start command pdf name ="+s);
		if(s != null && s.length()>0) fileName = s+".pdf";
		downloadFile();
		return super.onStartCommand(intent, flags, startId);
		
	}
	@Override
	public IBinder onBind(Intent intent) {
		System.out.println("hh on bind");
		return null;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		System.out.println("hh on destroy");
		unregisterReceiver(receiver);
	}
	
	 public void downloadFile() {
		 
		 
		 try {
			 
			 dm = (DownloadManager) getSystemService(Activity.DOWNLOAD_SERVICE);
//		        Request request = new Request(Uri.parse("http://54.251.121.65/wp-content/uploads/2015/01/Bhanu_I27898.pdf"));
				Request request = new Request(Uri.parse(url));
		        String pdfPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
		        request.setDestinationInExternalPublicDir(pdfPath, "/"+fileName);
		        enqueue = dm.enqueue(request);
		        
//		        app.saveIntoPrefs(, "downoloading");
		        
		        SharedPreferences prefs = getSharedPreferences(BMHConstants.PREF_NAME, MODE_PRIVATE);
				Editor edit = prefs.edit();
				edit.putBoolean(BMHConstants.IS_DOWNLOADING_KEY, true);
				edit.commit();
			
		} catch (Exception e) {
			 Log.e(TAG,e.toString());
			// TODO: handle exception
		}
			
		}
	 
	 
}

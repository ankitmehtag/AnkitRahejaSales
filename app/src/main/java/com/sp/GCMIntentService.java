/**
 * 
 */
package com.sp;

//import com.google.android.gcm.GCMBaseIntentService;

/**
 * This intent service will be called by the GCMBroadcastReceiver (which is is
 * provided by GCM library).
 * 
 * @author ADMIN
 * 
 */
public class GCMIntentService {

	/*public GCMIntentService() {
		super(UrlFactory.getProjectId());
	}

	@Override
	protected void onError(Context context, String errorId) {
		System.out.println("hh on Error");
	}

	@Override
	protected void onMessage(Context context, Intent intent) {
		System.out.println("hh on Message");
		System.out.println("hh intent.getExtras()= "+intent.getExtras().toString());

		String message = intent.getExtras().getString("message");
		JSONObject obj;
		try {
			if(message!=null){
				obj = new JSONObject(message);
				String projId =  obj.optString("Project_id");
				String Title =  obj.optString("Title");
				String msg =  obj.optString("Message");
				System.out.println("hh massage ="+message);
				showNotificaton(context,Title, msg, projId);
			}

			
		} catch (JSONException e) {
			e.printStackTrace();
		}

		
		
//		JSONObject msgJson;
		
//		try {
			
//			msgJson = new JSONObject(message);
//			String userId = msgJson.optString("notifiedUserId");
			
//				new NotificationHandler().handleNotification(context,msgJson, true);
			
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}

	}

	@Override
	protected void onRegistered(Context context, String registerationId) {
		System.out.println("hh on Registered : " + registerationId);
		BMHApplication app = (BMHApplication) getApplication();
		if(app.getFromPrefs(BMHConstants.GCM_REG_ID).equalsIgnoreCase("") && !registerationId.isEmpty()){
			app.saveIntoPrefs(BMHConstants.GCM_REG_ID, registerationId);
			try {
				ContentLoader.getPostContentFromServer(UrlFactory.getGCMRegisterUrl(), "device_id="+registerationId+"&device_type=android");
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		
		
	}

	@Override
	protected void onUnregistered(Context context, String regId) {
		System.out.println("on unregistered");
	}

	public void showNotificaton(Context context,String title,String msg, String projid ){
		Intent i = new Intent(this, ProjectDetailActivity.class);
		i.putExtra("propertyId", projid);
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
		NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0,i, PendingIntent.FLAG_UPDATE_CURRENT);
		Bitmap notificationLargeIconBitmap = BitmapFactory.decodeResource(context.getResources(),R.drawable.app_icon);
		 NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
		 mBuilder
		 .setLargeIcon(notificationLargeIconBitmap)
		 .setSmallIcon(R.drawable.app_icon)
		 .setColor(context.getResources().getColor(R.color.green))
		 .setContentTitle(title)
		 .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
	     .setContentText(msg)
	     .setAutoCancel(true)
	     .setTicker(msg);
        mBuilder.setContentIntent(contentIntent);
        Uri sound= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        mBuilder.setSound(sound);
        notificationManager.notify(new Random().nextInt(), mBuilder.build());
    
	}
	
	*/
}

package view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

public class onclicklistener1 extends View {

	public onclicklistener1(Context context) {
		super(context);
	}

	public onclicklistener1(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public onclicklistener1(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	/*public onclicklistener1(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
	}

	@Override
	public boolean bindService(Intent arg0, ServiceConnection arg1, int arg2) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int checkCallingOrSelfPermission(String permission) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int checkCallingOrSelfUriPermission(Uri uri, int modeFlags) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int checkCallingPermission(String permission) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int checkCallingUriPermission(Uri uri, int modeFlags) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int checkPermission(String permission, int pid, int uid) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int checkUriPermission(Uri uri, int pid, int uid, int modeFlags) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int checkUriPermission(Uri uri, String readPermission,
			String writePermission, int pid, int uid, int modeFlags) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void clearWallpaper() throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public Context createConfigurationContext(
			Configuration overrideConfiguration) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Context createDisplayContext(Display display) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Context createPackageContext(String packageName, int flags)
			throws NameNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] databaseList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean deleteDatabase(String name) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteFile(String name) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void enforceCallingOrSelfPermission(String permission, String message) {
		// TODO Auto-generated method stub

	}

	@Override
	public void enforceCallingOrSelfUriPermission(Uri uri, int modeFlags,
			String message) {
		// TODO Auto-generated method stub

	}

	@Override
	public void enforceCallingPermission(String permission, String message) {
		// TODO Auto-generated method stub

	}

	@Override
	public void enforceCallingUriPermission(Uri uri, int modeFlags,
			String message) {
		// TODO Auto-generated method stub

	}

	@Override
	public void enforcePermission(String permission, int pid, int uid,
			String message) {
		// TODO Auto-generated method stub

	}

	@Override
	public void enforceUriPermission(Uri uri, int pid, int uid, int modeFlags,
			String message) {
		// TODO Auto-generated method stub

	}

	@Override
	public void enforceUriPermission(Uri uri, String readPermission,
			String writePermission, int pid, int uid, int modeFlags,
			String message) {
		// TODO Auto-generated method stub

	}

	@Override
	public String[] fileList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Context getApplicationContext() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ApplicationInfo getApplicationInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AssetManager getAssets() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public File getCacheDir() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ClassLoader getClassLoader() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public File getCodeCacheDir() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ContentResolver getContentResolver() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public File getDatabasePath(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public File getDir(String name, int mode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public File getExternalCacheDir() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public File[] getExternalCacheDirs() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public File getExternalFilesDir(String type) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public File[] getExternalFilesDirs(String type) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public File[] getExternalMediaDirs() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public File getFileStreamPath(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public File getFilesDir() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Looper getMainLooper() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public File getNoBackupFilesDir() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public File getObbDir() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public File[] getObbDirs() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPackageCodePath() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PackageManager getPackageManager() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPackageName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPackageResourcePath() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Resources getResources() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SharedPreferences getSharedPreferences(String name, int mode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getSystemService(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Theme getTheme() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Drawable getWallpaper() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getWallpaperDesiredMinimumHeight() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getWallpaperDesiredMinimumWidth() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void grantUriPermission(String toPackage, Uri uri, int modeFlags) {
		// TODO Auto-generated method stub

	}

	@Override
	public FileInputStream openFileInput(String name)
			throws FileNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FileOutputStream openFileOutput(String name, int mode)
			throws FileNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SQLiteDatabase openOrCreateDatabase(String name, int mode,
			CursorFactory factory) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SQLiteDatabase openOrCreateDatabase(String name, int mode,
			CursorFactory factory, DatabaseErrorHandler errorHandler) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Drawable peekWallpaper() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Intent registerReceiver(BroadcastReceiver receiver,
			IntentFilter filter) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Intent registerReceiver(BroadcastReceiver receiver,
			IntentFilter filter, String broadcastPermission, Handler scheduler) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removeStickyBroadcast(Intent intent) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeStickyBroadcastAsUser(Intent intent, UserHandle user) {
		// TODO Auto-generated method stub

	}

	@Override
	public void revokeUriPermission(Uri uri, int modeFlags) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendBroadcast(Intent intent) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendBroadcast(Intent intent, String receiverPermission) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendBroadcastAsUser(Intent intent, UserHandle user) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendBroadcastAsUser(Intent intent, UserHandle user,
			String receiverPermission) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendOrderedBroadcast(Intent intent, String receiverPermission) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendOrderedBroadcast(Intent intent, String receiverPermission,
			BroadcastReceiver resultReceiver, Handler scheduler,
			int initialCode, String initialData, Bundle initialExtras) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendOrderedBroadcastAsUser(Intent intent, UserHandle user,
			String receiverPermission, BroadcastReceiver resultReceiver,
			Handler scheduler, int initialCode, String initialData,
			Bundle initialExtras) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendStickyBroadcast(Intent intent) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendStickyBroadcastAsUser(Intent intent, UserHandle user) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendStickyOrderedBroadcast(Intent intent,
			BroadcastReceiver resultReceiver, Handler scheduler,
			int initialCode, String initialData, Bundle initialExtras) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendStickyOrderedBroadcastAsUser(Intent intent,
			UserHandle user, BroadcastReceiver resultReceiver,
			Handler scheduler, int initialCode, String initialData,
			Bundle initialExtras) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setTheme(int resid) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setWallpaper(Bitmap bitmap) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setWallpaper(InputStream data) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void startActivities(Intent[] intents) {
		// TODO Auto-generated method stub

	}

	@Override
	public void startActivities(Intent[] intents, Bundle options) {
		// TODO Auto-generated method stub

	}

	@Override
	public void startActivity(Intent intent) {
		// TODO Auto-generated method stub

	}

	@Override
	public void startActivity(Intent intent, Bundle options) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean startInstrumentation(ComponentName className,
			String profileFile, Bundle arguments) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void startIntentSender(IntentSender intent, Intent fillInIntent,
			int flagsMask, int flagsValues, int extraFlags)
			throws SendIntentException {
		// TODO Auto-generated method stub

	}

	@Override
	public void startIntentSender(IntentSender intent, Intent fillInIntent,
			int flagsMask, int flagsValues, int extraFlags, Bundle options)
			throws SendIntentException {
		// TODO Auto-generated method stub

	}

	@Override
	public ComponentName startService(Intent service) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean stopService(Intent service) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void unbindService(ServiceConnection conn) {
		// TODO Auto-generated method stub

	}

	@Override
	public void unregisterReceiver(BroadcastReceiver receiver) {
		// TODO Auto-generated method stub

	}
*/
}

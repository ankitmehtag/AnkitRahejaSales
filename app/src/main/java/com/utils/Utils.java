package com.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.StatFs;
import android.provider.MediaStore;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.FileProvider;
import android.text.Editable;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.AppEnums.UIEventType;
import com.adapters.NewLaunchListAdapter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.maps.model.LatLng;
import com.helper.BMHConstants;
import com.helper.ChatConstants;
import com.helper.UrlFactory;
import com.model.AsmSalesModel;
import com.model.Assign_To;
import com.model.LeadStatus;
import com.model.NetworkErrorObject;
import com.model.NotInterestedLead;
import com.model.PreSalesAsmModel;
import com.model.PreSalesSpModel;
import com.model.Projects;
import com.sp.BMHApplication;
import com.sp.BuildConfig;
import com.sp.ProjectsListActivity;
import com.sp.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Currency;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    private static final String TAG = Utils.class.getSimpleName();
    public static final String dateFormat = "E, MMM dd yyyy";
   // public static final String dateFormat1 = "yyyy-MM-dd hh:mm:ss";
    public static final String dateFormat1 = "yyyy-MM-dd hh:mm:ss";

    public static final String dateFormat_yyymmdd = "yyyy-MM-dd";
    public static final String dateFormat_yyymmdd_test = "dd";//"MM/dd/yyyy";
    public static String subRootFolder = "/SP_APP/";
    private static File directory = null;
    private static File recDirectory = null;
    private static MediaPlayer mPlayer = null;
    private static int hasExtCallPermission;
    private List<String> permissionsCall = new ArrayList<>();
	/*public int dp2px(int dp, Context c) {
		float scale = c.getResources().getDisplayMetrics().density;
		int pixels = (int) (dp * scale + 0.5f);
		return pixels;
	}

	public int UnitDP(int dp, Context d) {
		float scale = d.getResources().getDisplayMetrics().density;
		int pixels = (int) (dp * scale + 0.5f);
		return pixels;

	}*/

    public static void openAttachment(Context context, String downloadUrl, File url) {
        try {
            Uri uri = FileProvider.getUriForFile(context,
                    BuildConfig.APPLICATION_ID + ".provider",
                    url);
            //Uri uri = Uri.fromFile(url);

            Intent intent = new Intent(Intent.ACTION_VIEW);
            if (url.toString().contains(".doc") || url.toString().contains(".docx")) {
                // Word document
                intent.setDataAndType(uri, "application/msword");
            } else if (url.toString().contains(".pdf")) {
                // PDF file
                intent.setDataAndType(uri, "application/pdf");
            } else if (url.toString().contains(".ppt") || url.toString().contains(".pptx")) {
                // Powerpoint file
                intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
            } else if (url.toString().contains(".xls") || url.toString().contains(".xlsx")) {
                // Excel file
                intent.setDataAndType(uri, "application/vnd.ms-excel");
            } else if (url.toString().contains(".zip") || url.toString().contains(".rar")) {
                // WAV audio file
                intent.setDataAndType(uri, "application/x-wav");
            } else if (url.toString().contains(".rtf")) {
                // RTF file
                intent.setDataAndType(uri, "application/rtf");
            } else if (url.toString().contains(".wav") || url.toString().contains(".mp3")) {
                // WAV audio file
                intent.setDataAndType(uri, "audio/x-wav");
            } else if (url.toString().contains(".gif")) {
                // GIF file
                intent.setDataAndType(uri, "image/gif");
            } else if (url.toString().contains(".jpg") || url.toString().contains(".jpeg") || url.toString().contains(".png")) {
                // JPG file
                intent.setDataAndType(uri, "image/jpeg");
            } else if (url.toString().contains(".txt")) {
                // Text file
                intent.setDataAndType(uri, "text/plain");
            } else if (url.toString().contains(".3gp") || url.toString().contains(".mpg") ||
                    url.toString().contains(".mpeg") || url.toString().contains(".mpe") || url.toString().contains(".mp4") || url.toString().contains(".avi")) {
                // Video files
                intent.setDataAndType(uri, "video/*");
            } else {
                intent.setDataAndType(uri, "*/*");
            }

            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, "No application found which can open the file", Toast.LENGTH_SHORT).show();
        }
    }

    // schedule the start of the service every 10 - 30 seconds
  /*  public static void scheduleJob(Context context) {
        ComponentName serviceComponent = new ComponentName(context, RecordingJobService.class);
        JobInfo.Builder builder = new JobInfo.Builder(0, serviceComponent);
        builder.setMinimumLatency(1 * 1000); // wait at least
        builder.setOverrideDeadline(3 * 1000); // maximum delay
        //builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED); // require unmetered network
        //builder.setRequiresDeviceIdle(true); // device should be idle
        //builder.setRequiresCharging(false); // we don't care if the device is charging or not

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            JobScheduler jobScheduler = null;
            jobScheduler = context.getSystemService(JobScheduler.class);
            jobScheduler.schedule(builder.build());
        }
    }*/

    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }

        // The directory is now empty so delete it
        return dir.delete();
    }

    public static boolean deleteDirectory(File dir) {
        if (dir.isDirectory()) {
            File[] children = dir.listFiles();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDirectory(children[i]);
                if (!success) {
                    return false;
                }
            }
        } // either file or an empty directory
        System.out.println("removing file or directory : " + dir.getName());
        return dir.delete();
    }

    public static File getInternalStorageDirectory(Context context) {
        try {
            File folder = context.getFilesDir();
            recDirectory = new File(folder, BMHConstants.APP_ROOT_FOLDER);
           /* if (!directory.exists())
                directory.mkdir();*/
            //  FileOutputStream out = new FileOutputStream(file); //Use the stream as usual to write into the file.
        } catch (Exception e) {
            e.printStackTrace();
        }
        return recDirectory;
    }

    public static File createRecordingDirectory(Context context) {
        String root = BMHApplication.getInstance().getFromPrefs(BMHConstants.APP_ROOT_FOLDER);
        if (TextUtils.isEmpty(root)) {
           /* if (Utils.isSdCardAvailable() && Utils.isExternalStorageWritable()) {
                directory = new File(Utils.getDirectory());
            } else {*/
            ContextWrapper cw = new ContextWrapper(context);
            directory = cw.getDir(cw.getString(R.string.folder_name), Context.MODE_PRIVATE);
            //  directory = getInternalStorageDirectory(context);
            //  }
            if (!directory.exists())
                directory.mkdirs();
            BMHApplication.getInstance().saveIntoPrefs(BMHConstants.APP_ROOT_FOLDER, directory.getAbsolutePath());
        } else {
            directory = new File(root);
        }
        return directory;
    }

    public static File createFileDirectory(Context context) {
        String root = BMHApplication.getInstance().getFromPrefs(BMHConstants.APP_ROOT_FOLDER);
        if (TextUtils.isEmpty(root)) {
            if (Utils.isSdCardAvailable() && Utils.isExternalStorageWritable()) {
                directory = new File(Utils.getDirectory());
            } else {
                ContextWrapper cw = new ContextWrapper(context);
                directory = cw.getDir(cw.getString(R.string.folder_name), Context.MODE_PRIVATE);
                //  directory = getInternalStorageDirectory(context);
            }
            if (!directory.exists())
                directory.mkdirs();
            BMHApplication.getInstance().saveIntoPrefs(BMHConstants.APP_ROOT_FOLDER, directory.getAbsolutePath());
        } else {
            directory = new File(root);
        }
        return directory;
    }

    public static String getDirectory() {
        String dirPath = null;
        try {
            dirPath = Environment.getExternalStorageDirectory() + subRootFolder;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dirPath;
    }

    /* Checks if external storage is available for read and write */
    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available for read and write */
    public static boolean isSdCardAvailable() {
        return android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
    }

    /* Checks if external storage is available to at least read */
    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    public static File renameFileExtension(String source, String newExtension) {
        String target;
        String currentExtension = getFileExtension(source);
        target = source.replaceAll("." + currentExtension, newExtension);
        new File(source).renameTo(new File(target));
        return new File(target);
    }

    public static String getFileExtension(String f) {
        String ext = "";
        int i = f.lastIndexOf('.');
        if (i > 0 && i < f.length() - 1) {
            ext = f.substring(i + 1).toLowerCase();
        }
        return ext;
    }

    public static Date parseDate(String timeText, Date date) {
        final String inputFormat = "dd-M-yyyy hh:mm";
        /*SimpleDateFormat inputParser = new SimpleDateFormat(inputFormat, Locale.getDefault());
        try {
            return inputParser.parse(timeText);
        } catch (java.text.ParseException e) {
            return new Date(0);
        }*/

        SimpleDateFormat mSdf = new SimpleDateFormat("MMM dd yyyy");
        String mDate = mSdf.format(date);

        SimpleDateFormat sdf = new SimpleDateFormat(inputFormat);
        String dateInString = mDate + " " + timeText;
        try {
            return sdf.parse(dateInString);
        } catch (ParseException e) {
            return new Date(date.getDay());
        }
    }

    public int UnitDP(int dp, NewLaunchListAdapter newLaunchListAdapter) {
        // TODO Auto-generated method stub
        return 0;
    }

    public static float dp2px(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }

    /**
     * This method converts device specific pixels to density independent pixels.
     *
     * @param px      A value in px (pixels) unit. Which we need to convert into db
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent dp equivalent to px value
     */
    public static float px2dp(float px, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return dp;
    }

    public static NetworkErrorObject showNetworkErrorDialog(Context context, UIEventType curEvent, View.OnClickListener mOnClickListener) {
        View mView = null;
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = inflater.inflate(R.layout.network, null);
        alertDialogBuilder.setView(mView);
        alertDialogBuilder.setCancelable(false);
        final AlertDialog alertDialog = alertDialogBuilder.create();
        mView.findViewById(R.id.btn_try_again).setOnClickListener(mOnClickListener);
        alertDialog.show();
        return new NetworkErrorObject(curEvent, alertDialog);
    }

    public static void showToast(Context ctx, String msg) {
        Toast.makeText(ctx, msg, Toast.LENGTH_LONG).show();
    }

    public static void openMailClient(Context ctx, String subject, String[] to, String bodyText) {
        if (ctx == null) return;
        try {
            Intent emailIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:"));
            emailIntent.putExtra(Intent.EXTRA_EMAIL, to);
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
            emailIntent.putExtra(Intent.EXTRA_TEXT, bodyText);
            ctx.startActivity(emailIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String priceFormat(double value) {
        try {
            NumberFormat nf = NumberFormat.getCurrencyInstance();
            nf.setCurrency(Currency.getInstance("INR"));
            nf.setMaximumFractionDigits(0);
            DecimalFormatSymbols decimalFormatSymbols = ((DecimalFormat) nf).getDecimalFormatSymbols();
            decimalFormatSymbols.setCurrencySymbol("");
            ((DecimalFormat) nf).setDecimalFormatSymbols(decimalFormatSymbols);
            return nf.format(value).trim();
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return String.valueOf(value);
        }
    }

    public static int convertStringToMoney(String string) {
        String str = string.trim();
        String[] moneyStr = str.split(" ");
        //   double d = Double.valueOf(moneyStr[0]);
        //  int value = Integer.parseInt(String.valueOf(d));
        //  int value = Integer.valueOf(moneyStr[0].replace(".",""));
        float value = Float.valueOf(moneyStr[0]);
        switch (moneyStr[1]) {
            case "Cr":
                value = value * 10000000;
                break;
            case "Lacs":
                value = value * 100000;
                break;
            default:
                break;
        }
        return (int) value;
    }

    public static String getFileExtension(File file) {
        String extension = "";
        try {
            if (file != null) {
                String name = file.getName();
                extension = name.substring(name.lastIndexOf("."));
            }
        } catch (Exception e) {
            extension = "";
        }
        return extension;

    }

    public static String integerToStringMoney(String budget) {
        String str = budget.trim();
        int len = str.length();
        String value;
        StringBuilder builder = new StringBuilder();
        switch (len) {
            case 8:
                value = str.substring(0, 2);
                if (Integer.valueOf(value) > 10) {
                    builder.append(str.substring(0, 1)).append(".").append(str.substring(1, 2) + " Cr");
                } else {
                    // builder.append(value + ".0 Cr");
                    builder.append(str.substring(0, 1)).append(".0 Cr");
                }
                break;
            case 7:
                value = str.substring(0, 2);
                builder.append(value + " Lacs");
                break;
            default:
                break;
        }
        return builder.toString();
    }

    public static String removeLastComma(String str) {
        if (str != null && str.length() > 0 && str.charAt(str.length() - 1) == ',') {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }

    public static String removeLastAndSign(String str) {
        if (str != null && str.length() > 0 && str.charAt(str.length() - 1) == '&') {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }

    public static String removeLastSlashSign(String str) {
        if (str != null && str.length() > 0 && str.charAt(str.length() - 1) == '/') {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }


    public static Typeface getAwesomeTypeface(Context ctx) {
        try {
            return Typeface.createFromAsset(ctx.getAssets(), "fonts/fontawesome-webfont.ttf");
        } catch (Exception e) {
            return null;
        }
    }

	/*public static Bitmap takeScreenShot(Activity activity) {
		View view = activity.getWindow().getDecorView();
		view.setDrawingCacheEnabled(true);
		view.buildDrawingCache();
		Bitmap b1 = view.getDrawingCache();
		if(b1 != null) {
			Rect frame = new Rect();
			activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
			int statusBarHeight = frame.top;
			Display display = activity.getWindowManager().getDefaultDisplay();
			Point size = new Point();
			display.getSize(size);
			int width = size.x;
			int height = size.y;
			b1 = Bitmap.createBitmap(b1, 0, statusBarHeight, width, height - statusBarHeight);
			view.destroyDrawingCache();
		}
		return b1;
	}*/

    public static Bitmap takeScreenshot(Activity activity, int rootId) {

        View rootView = activity.getWindow().getDecorView().findViewById(rootId);
        rootView.setDrawingCacheEnabled(true);
        return rootView.getDrawingCache();

    }

    public static boolean saveBitmap(Context ctx, Bitmap bitmap) {
        String root = Environment.getExternalStorageDirectory().toString();
        String folder = "Raheja Developer Ltd";
        File newDir = new File(root, folder);
        if (!newDir.exists()) {
            if (!newDir.mkdirs()) {
                Log.e(TAG, "Problem creating Image folder");
            }
        }
        Random gen = new Random();
        int n = 10000;
        n = gen.nextInt(n);
        String fotoname = "Screenshot-" + n + ".jpg";
        File file = new File(newDir, fotoname);
        if (file.exists())
            file.delete();
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            Toast.makeText(ctx, "Screenshot saved in: " + newDir.getAbsolutePath(), Toast.LENGTH_SHORT).show();

        } catch (FileNotFoundException e) {
            Log.e(TAG, e.getMessage(), e);
            return false;
        } catch (IOException e) {
            Log.e(TAG, e.getMessage(), e);
            return false;
        }
        return true;
    }

    public static boolean createDirIfNotExists(String path) {
        boolean ret = true;
        File file = new File(Environment.getExternalStorageDirectory(), path);
        if (!file.exists()) {
            if (!file.mkdirs()) {
                Log.e("Utils :: ", "Problem creating Image folder");
                ret = false;
            }
        }
        return ret;
    }

    public static int toInt(String value) {
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            return 0;
        }
    }

    public static float toFloat(String value) {
        try {
            return Float.parseFloat(value);
        } catch (Exception e) {
            return 0f;
        }
    }

    public static double toDouble(String value) {
        try {
            return Double.parseDouble(value);
        } catch (Exception e) {
            return 0.0;
        }
    }

    public static Object cloneObject(Object obj) {
        try {
            Object clone = obj.getClass().newInstance();
            for (Field field : obj.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                field.set(clone, field.get(obj));
            }
            return clone;
        } catch (Exception e) {
            return null;
        }
    }

    public static String getYoutubeThumbnailUrl(String key) {
        if (key == null || key.isEmpty()) return "";
        return String.format("http://img.youtube.com/vi/%s/hqdefault.jpg", key);
    }

    public static List<LatLng> decodePoly(String encoded) {

        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)), (((double) lng / 1E5)));
            poly.add(p);
        }

        return poly;
    }

    public static boolean isEmailValid(String email) {
      /*  String regExpn = "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?" + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?" + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";*/
        String regExpn = "^[\\w-\\+]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-z]{2,})$";

        CharSequence inputStr = email;
        Pattern pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);

        if (matcher.matches())
            return true;
        else
            return false;
    }

    public static boolean isValidUserName(String userName) {
        String strRegex = "^[a-zA-Z ]{3,30}$";
        Pattern pattern = Pattern.compile(strRegex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(userName.trim());
        if (matcher.matches())
            return true;
        else
            return false;
      /*  if (userName.matches("^[a-zA-Z ]{3,30}$"))
            return true;
        else
            return false;*/
    }

    public static boolean isValidPANCardNumber(String panNumber) {
        if (panNumber == null || panNumber.trim().isEmpty()) return false;
        Pattern pattern = Pattern.compile("[A-Z]{5}[0-9]{4}[A-Z]{1}");
        Matcher matcher = pattern.matcher(panNumber);
        if (matcher.matches()) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isValidPinCode(String pinCode) {
        if (pinCode == null || pinCode.isEmpty()) return false;
        String regex = "[0-9]{6}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(pinCode);
        if (matcher.matches()) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isValidMobileNumber(String mobileNumber) {
        boolean isValid = false;
        if (mobileNumber == null || mobileNumber.trim().length() < 2) return isValid;
        char firstCharacter = mobileNumber.charAt(0);
        switch (firstCharacter) {
            case '7':
                isValid = true;
                break;
            case '8':
                isValid = true;
                break;
            case '9':
                isValid = true;
                break;
        }
        return isValid;
    }

    public static void hideKeyboard(Activity context) {
        if (context == null) return;
        View view = context.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static String getAppVersionString(Context context) {
        Log.d(TAG, "Inside getAppVersionString");
        int versionNumber = 0;
        String versionName = "";
        try {
            PackageInfo pinfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            versionNumber = pinfo.versionCode;
            versionName = pinfo.versionName;
        } catch (Exception e) {
            Log.d(TAG, "getAppVersionString() Exception is :" + e);
        }
        // Log.e(TAG, "getAppVersionString() Version No is :" + versionNumber);
        // Log.e(TAG, "getAppVersionString() Version Name is :" + versionName);
        return versionName;
    }

	/*public static <T> ArrayList<T[]> chunks(ArrayList<T> bigList,int n){
		ArrayList<T[]> chunks = new ArrayList<T[]>();

		for (int i = 0; i < bigList.size(); i += n) {
			T[] chunk = (T[])bigList.subList(i, Math.min(bigList.size(), i + n)).toArray();
			chunks.add(chunk);
		}

		return chunks;
	}*/

    public static <T> ArrayList<List<T>> chunks(List<T> bigList, int n) {
        ArrayList<List<T>> chunks = new ArrayList<List<T>>();

        for (int i = 0; i < bigList.size(); i += n) {
            T[] chunk = (T[]) bigList.subList(i, Math.min(bigList.size(), i + n)).toArray();
            List<T> tempChunk = Arrays.asList(chunk);
            chunks.add(tempChunk);
        }

        return chunks;
    }

    public static boolean isAutoDateTimeAndTimeZone(Context context) {
        if (context == null) return false;
        if (isTimeAutomatic(context) && isZoneAutomatic(context)) return true;
        else return false;
    }

    public static boolean isTimeAutomatic(Context c) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return Settings.Global.getInt(c.getContentResolver(), Settings.Global.AUTO_TIME, 0) == 1;
        } else {
            return android.provider.Settings.System.getInt(c.getContentResolver(), android.provider.Settings.System.AUTO_TIME, 0) == 1;
        }
    }

    public static boolean isZoneAutomatic(Context c) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return Settings.Global.getInt(c.getContentResolver(), Settings.Global.AUTO_TIME_ZONE, 0) == 1;
        } else {
            return android.provider.Settings.System.getInt(c.getContentResolver(), android.provider.Settings.System.AUTO_TIME_ZONE, 0) == 1;
        }
    }

    @NonNull
    public static Calendar getDateFromTextView(SimpleDateFormat formatter, String fromDate) throws ParseException {
        Date date = formatter.parse(fromDate);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }

    public static boolean isBudgetChanged(String modelText, String text) {
        return (TextUtils.isEmpty(modelText) && !TextUtils.isEmpty(text))
                || (!TextUtils.isEmpty(modelText) && !modelText.equalsIgnoreCase(text));
    }

    public static boolean isChanged(String modelText, Editable text) {
        return (TextUtils.isEmpty(modelText) && !TextUtils.isEmpty(text.toString()))
                || (!TextUtils.isEmpty(modelText) && !modelText.equalsIgnoreCase(text.toString()));
    }

    public static boolean isChanged(String modelText, String text) {
        return (TextUtils.isEmpty(modelText) && !TextUtils.isEmpty(text))
                || (!TextUtils.isEmpty(modelText) && !modelText.equalsIgnoreCase(text));
    }

    @NonNull
    public static String convertStringTo24Format(String timeStr) throws ParseException {
        SimpleDateFormat displayFormat = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat parseFormat = new SimpleDateFormat("hh:mm a");
        Date date = parseFormat.parse(timeStr);
        // System.out.println(parseFormat.format(date) + " = " + displayFormat.format(date));
        return displayFormat.format(date);
    }

    public static int convertStringToInt(String monthString) {
        switch (monthString) {
            case "Jan":
                return 1;
            case "Feb":
                return 2;
            case "Mar":
                return 3;
            case "Apr":
                return 4;
            case "May":
                return 5;
            case "Jun":
                return 6;
            case "Jul":
                return 7;
            case "Aug":
                return 8;
            case "Sep":
                return 9;
            case "Oct":
                return 10;
            case "Nov":
                return 11;
            case "Dec":
                return 12;
        }
        return 0;
    }

    public static String getMonthFromInt(int num) {
        String monthName = null;
        switch (num) {
            case 0:
                monthName = "Jan";
                break;
            case 1:
                monthName = "Feb";
                break;
            case 2:
                monthName = "Mar";
                break;
            case 3:
                monthName = "Apr";
                break;
            case 4:
                monthName = "May";
                break;
            case 5:
                monthName = "Jun";
                break;
            case 6:
                monthName = "Jul";
                break;
            case 7:
                monthName = "Aug";
                break;
            case 8:
                monthName = "Sep";
                break;
            case 9:
                monthName = "Oct";
                break;
            case 10:
                monthName = "Nov";
                break;
            case 11:
                monthName = "Dec";
                break;
        }
        return monthName;
    }

    public static String getDayName(int dayofWeek) {

        String dayName = null;
        switch (dayofWeek) {
            case 1:
                dayName = "Sunday";
                break;
            case 2:
                dayName = "Monday";
                break;
            case 3:
                dayName = "Tuesday";
                break;
            case 4:
                dayName = "Wednesday";
                break;
            case 5:
                dayName = "Thursday";
                break;
            case 6:
                dayName = "Friday";
                break;
            case 7:
                dayName = "Saturday";
                break;
        }
        return dayName;
    }

    // INPUT - Feb 20 2019
    // OUTPUT - 2019-02-20
    public static String getDateFromString(String dateString) {
        if (!TextUtils.isEmpty(dateString)) {
            StringBuilder dateBuilder = new StringBuilder();
            String[] strArr = dateString.trim().split(" ");
            if (strArr.length > 2)
                return dateBuilder.append(strArr[2]).append("-").append(convertStringToInt(strArr[1])).append("-").append(strArr[0]).toString();
        }
        return null;
    }

    @NonNull
    public static String getRecordingDateTime() {
        /*try {
            Calendar currentCal = Calendar.getInstance();
            currentCal.getTimeInMillis();
            String year = String.valueOf(currentCal.get(Calendar.YEAR));
            String month = String.valueOf(currentCal.get(Calendar.MONTH) + 1);
            String date = String.valueOf(currentCal.get(Calendar.DATE));

            String amPM = currentCal.get(Calendar.AM_PM) == 0 ? "AM" : "PM";
            String mLimitText = currentCal.get(Calendar.HOUR) + ":" + currentCal.get(Calendar.MINUTE) + " " + amPM;
            return year + "-" + month + "-" + date + "_" + Utils.convertStringTo24Format(mLimitText);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";*/
        Calendar currentCal = Calendar.getInstance();
        return String.valueOf(currentCal.getTimeInMillis());
    }

    @NonNull
    public static String getCurrentDateTime() {
        try {
            Calendar currentCal = Calendar.getInstance();
            //    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String year = String.valueOf(currentCal.get(Calendar.YEAR));
            int month = currentCal.get(Calendar.MONTH);
            String date = String.valueOf(currentCal.get(Calendar.DATE));

            String amPM = currentCal.get(Calendar.AM_PM) == 0 ? "AM" : "PM";
            String mLimitText = currentCal.get(Calendar.HOUR) + ":" + currentCal.get(Calendar.MINUTE) + " " + amPM;
            return date + " " + getMonthFromInt(month) + " " + year + " | " + Utils.convertStringTo24Format(mLimitText);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    @NonNull
    public static String getSystemDateTime() {
        try {
            Calendar currentCal = Calendar.getInstance();
            //    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String year = String.valueOf(currentCal.get(Calendar.YEAR));
            int month = currentCal.get(Calendar.MONTH) + 1;
            String date = String.valueOf(currentCal.get(Calendar.DATE));

            String amPM = currentCal.get(Calendar.AM_PM) == 0 ? "AM" : "PM";
            String mLimitText = currentCal.get(Calendar.HOUR) + ":" + currentCal.get(Calendar.MINUTE) + " " + amPM;
            //    return year + "-" + month + "-" + date + " " + Utils.convertStringTo24Format(mLimitText);
            return date + " " + getMonthFromInt(month) + " " + year + " | " + Utils.convertStringTo24Format(mLimitText);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static boolean isServiceRunning(Context ctx, Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public static String getFormattedToDate() {
        Calendar cal = Calendar.getInstance();
        Date currentDate = cal.getTime();
        return getChangeDate(dateFormat, currentDate);
    }

    public static String getFormattedFromDate(int previousMonths) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, previousMonths);
        Date fromDate = cal.getTime();
        return getChangeDate(dateFormat, fromDate);
    }

    public static String getChangeDate(String pattern, Date date) {
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        return df.format(date);
    }

    public static String createTaggedMessage(String tag, String msg) {
        if (msg == null || msg.isEmpty()) return "";
        if (tag == null || tag.isEmpty()) {
            return msg;
        } else {
            return ChatConstants.OPEN_TAG + tag + ChatConstants.CLOSE_TAG + msg;
        }
    }

    public static String createTagWithOpenAndCloseTag(String tag) {
        if (tag == null || tag.isEmpty()) return "";
        else
            return ChatConstants.OPEN_TAG + tag + ChatConstants.CLOSE_TAG;
    }

    public static String getMessageWithOutTag(String msg) {
        if (msg == null || msg.isEmpty()) return "";
        if (msg.startsWith(ChatConstants.OPEN_TAG)) {
            String[] arr = msg.split(ChatConstants.CLOSE_TAG);
            if (arr != null && arr.length > 1) return arr[1];
            else return "";
        } else {
            return msg;
        }
    }

    public static String getFormattedDate(long longDate) {
        if (longDate > 0) {
            SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm");
            Date date = new Date(longDate);
            return format.format(date);
        } else {
            return "";
        }
    }

    public static String getDeviceId(Context ctx) {
        return Settings.Secure.getString(ctx.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public static void imageUpdate(Context ctx, ImageView iv, String imgPath) {
        if (ctx != null && iv != null && imgPath != null && !imgPath.isEmpty()) {
            String imgUrl = UrlFactory.IMG_BASEURL + imgPath;
            Glide.with(ctx)
                    .load(imgUrl)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .error(BMHConstants.NO_IMAGE)
                    .override(100, 100)
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            //progressBar.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            //progressBar.setVisibility(View.GONE);
                            return false;
                        }
                    }).into(iv);
        }
    }

    public static void gotToHome(Context context) {
        Intent intent = new Intent(context, ProjectsListActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_CLEAR_TASK |
                Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static int getLeadStatusId(ArrayList<LeadStatus> leadsList, String selectedSp) {
        for (int i = 0; i < leadsList.size(); i++) {
            LeadStatus leadStatusModel = leadsList.get(i);
            if (selectedSp.equalsIgnoreCase(leadStatusModel.getTitle())) {
                return Integer.valueOf(leadStatusModel.getDisposition_id());
            }
        }
        return 0;
    }

    public static List<Projects> getAllSelectedProject(List<Projects> projectList, String selected) {

        List<Projects> projectsList = new ArrayList<>();
        if (projectList != null && projectList.size() > 0) {
            for (int i = 0; i < projectList.size(); i++) {
                Projects model = new Projects(projectList.get(i).getProject_id(), projectList.get(i).getProject_name());
                if (selected.contains(model.getProject_name()))
                    projectsList.add(model);
            }
        }
        return projectsList;
    }


    public static String getMultiSelectedProject(List<Projects> multiProjList) {
        String strMulti = "";
        if (multiProjList.size() > 0) {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < multiProjList.size(); i++) {
                builder.append(multiProjList.get(i).getProject_name()).append(", ");
            }
            strMulti = StringUtil.removeLastComma(builder.toString());
        }
        return strMulti;
    }

    public static List<String> getMultiSelectedProject(String string) {
        if (!TextUtils.isEmpty(string)) {
            String strMulti = string.trim();
            if (strMulti != null && strMulti.length() > 0) {
                List<String> stringList = new ArrayList<>();
                String[] strArr = strMulti.split(",");
                for (int i = 0; i < strArr.length; i++) {
                    stringList.add(strArr[i].trim());
                }
                return stringList;
            }
        }
        return null;
    }

    public static String getSelectedMultiProjectIds(List<Projects> selectedProj) {
        String selectedIds = "";
        if (selectedProj != null) {
            StringBuilder idBuilder = new StringBuilder();
            for (int i = 0; i < selectedProj.size(); i++) {
                idBuilder.append(selectedProj.get(i).getProject_id() + ", ");
            }
            selectedIds = StringUtil.removeLastComma(idBuilder.toString());
        }
        return selectedIds;
    }

    public static String getAssignToId(ArrayList<Assign_To> assignList, String salesPerson) {
        Assign_To assignModel;
        for (int i = 0; i < assignList.size(); i++) {
            assignModel = assignList.get(i);
            if (salesPerson.equalsIgnoreCase(assignModel.getSalesperson_name())) {
                return assignModel.getSalesperson_id();
            }
        }
        return null;
    }

    public static String getBrokerId(ArrayList<NotInterestedLead> brokerList, String salesPerson) {
        NotInterestedLead model;
        for (int i = 0; i < brokerList.size(); i++) {
            model = brokerList.get(i);
            if (salesPerson.equalsIgnoreCase(model.getTitle())) {
                return model.getId();
            }
        }
        return null;
    }

    public static File createRecordingDir(final Context context, String mobileNo) {
        File outputFile = null;
        File mFileDirectory;
        String mFileName;
        StringBuilder builder = new StringBuilder();
        mFileDirectory = createRecordingDirectory(context);
        //mFileDirectory = getInternalStorageDirectory(context);
        if (!mFileDirectory.exists()) {
            mFileDirectory.mkdir();
        }
        File subDir = new File(mFileDirectory, BMHConstants.CALL_REC_SUB_DIR);
        if (!subDir.exists())
            subDir.mkdirs();
        BMHApplication.getInstance().saveIntoPrefs(BMHConstants.DIR_CALL_RECORDING, subDir.getAbsolutePath());
      /*  Random gen = new Random();
        int n = 10000;
        n = gen.nextInt(n);*/
        if (!isMemorySpaceAvailable(getAvailableInternalMemorySize(), getTotalInternalMemorySize())) {
            // DELETE FILES
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    Utils.showToast(context, "Sync files to make storage available ");
                }
            });
           // deleteFilteredFiles(subDir);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mFileName = builder.append(mobileNo).append("_").append(Utils.getRecordingDateTime())
                    .append(BMHConstants.CALL_REC_EXTENSION_MP3).toString();
        } else {
            mFileName = builder.append(mobileNo).append("_").append(Utils.getRecordingDateTime())
                    .append(BMHConstants.CALL_REC_EXTENSION_MP4).toString();
        }
        outputFile = new File(subDir, mFileName);
        if (!outputFile.exists()) {
            try {
                outputFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return outputFile;
    }

    public static void deleteFilteredFiles(File subDir) {
        if (subDir.isDirectory()) {
            File[] files = subDir.listFiles();

            Arrays.sort(files, new Comparator() {
                public int compare(Object o1, Object o2) {
                    if (((File) o1).lastModified() > ((File) o2).lastModified()) {
                        return -1;
                    } else if (((File) o1).lastModified() < ((File) o2).lastModified()) {
                        return +1;
                    } else {
                        return 0;
                    }
                }
            });
            for (int i = 10; i < files.length; i++) {
                while (i > 10) {
                    files[i].delete();
                }
            }
        }
    }

    public static File getCallRecordingDir(Context context, String mFileName) {
        File outputFile = null;
        File mFileDirectory;
        mFileDirectory = createRecordingDirectory(context);
        // mFileDirectory = getInternalStorageDirectory(context);
        if (mFileDirectory.exists()) {
            File subDir = new File(mFileDirectory, BMHConstants.CALL_REC_SUB_DIR);
            outputFile = new File(subDir, mFileName);
        }
        return outputFile;
    }

    public static byte[] getFileInBytes(String outputFile, Context context) {
        byte[] soundBytes = new byte[1024];
        try {
            InputStream inputStream =
                    context.getContentResolver().openInputStream(Uri.fromFile(new File(outputFile)));
            soundBytes = new byte[inputStream.available()];
            soundBytes = toByteArray(inputStream);
            //  Toast.makeText(this, "Recording Finished"+ " " + soundBytes, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }

            return soundBytes;

    }

    public static byte[] toByteArray(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int read = 0;
        byte[] buffer = new byte[1024 * 1024];
        while (read != -1) {
            read = in.read(buffer);
            if (read != -1)
                out.write(buffer, 0, read);
        }
        out.close();
        return out.toByteArray();
    }

    public static String getPath(Context context, Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(context, contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }

    public static Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public static String getRealPathFromURI(Context context, Uri uri) {
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        Objects.requireNonNull(cursor).moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }


    public static List<AsmSalesModel> filter(List<AsmSalesModel> models, String query) {
        query = query.toLowerCase();
        final List<AsmSalesModel> filteredModelList = new ArrayList<>();
        for (AsmSalesModel model : models) {
            final String customerName = model.getCustomer_name();
            final String custName = (TextUtils.isEmpty(customerName) ? "" : customerName.toLowerCase());
            final String status = model.getStatus();
            final String statusName = (TextUtils.isEmpty(status) ? "" : status.toLowerCase());
            final String mobile = (model.getCustomer_mobile()).toLowerCase();
            final String enquiryId = (model.getEnquiry_id()).toLowerCase();
            if (custName.contains(query) || statusName.contains(query) || mobile.contains(query) || enquiryId.contains(query)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }

    public static Comparator<AsmSalesModel> lastUpdatedTime = new Comparator<AsmSalesModel>() {

        public int compare(AsmSalesModel s1, AsmSalesModel s2) {

            String date1 = s1.getLastupdatedon();
            String date2 = s2.getLastupdatedon();
            if ((!TextUtils.isEmpty(date1) || !date1.equals(""))
                    && (!TextUtils.isEmpty(date2) || !date2.equals(""))) {
                String[] arr1 = date1.trim().replace("|", "t").split("t");
                String[] arr2 = date2.trim().replace("|", "t").split("t");
                // For descending order
                //  return (arr2[0]+" "+arr2[1]).compareTo(arr1[0]+" "+arr1[1]);

                String str1 = getDateFromString(arr1[0]) + " " + arr1[1];
                String str2 = getDateFromString(arr2[0]) + " " + arr2[1];
                return str2.compareTo(str1);
            }
            return 0;
        }
    };

    public static Comparator<PreSalesAsmModel> sortByLastUpdateAsm = new Comparator<PreSalesAsmModel>() {

        public int compare(PreSalesAsmModel s1, PreSalesAsmModel s2) {

            String date1 = s1.getLastupdatedon();
            String date2 = s2.getLastupdatedon();
            if ((!TextUtils.isEmpty(date1) || !date1.equals(""))
                    && (!TextUtils.isEmpty(date2) || !date2.equals(""))) {
                String[] arr1 = date1.trim().replace("|", "t").split("t");
                String[] arr2 = date2.trim().replace("|", "t").split("t");
                // For descending order
                //  return (arr2[0]+" "+arr2[1]).compareTo(arr1[0]+" "+arr1[1]);

                String str1 = getDateFromString(arr1[0]) + " " + arr1[1];
                String str2 = getDateFromString(arr2[0]) + " " + arr2[1];
                return str2.compareTo(str1);
            }
            return 0;
        }
    };

    public static Comparator<PreSalesSpModel> sortByLastUpdateSp = new Comparator<PreSalesSpModel>() {

        public int compare(PreSalesSpModel s1, PreSalesSpModel s2) {

            String date1 = s1.getLastupdatedon();
            String date2 = s2.getLastupdatedon();
            if ((!TextUtils.isEmpty(date1) || !date1.equals(""))
                    && (!TextUtils.isEmpty(date2) || !date2.equals(""))) {
                String[] arr1 = date1.trim().replace("|", "t").split("t");
                String[] arr2 = date2.trim().replace("|", "t").split("t");
                // For descending order
                //  return (arr2[0]+" "+arr2[1]).compareTo(arr1[0]+" "+arr1[1]);

                String str1 = getDateFromString(arr1[0]) + " " + arr1[1];
                String str2 = getDateFromString(arr2[0]) + " " + arr2[1];
                return str2.compareTo(str1);
            }
            return 0;
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String createNotificationChannel(NotificationManager notificationManager) {
        String channelId = "my_service_channelid";
        String channelName = "My Foreground Service";
        NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
        // omitted the LED color
        channel.setImportance(NotificationManager.IMPORTANCE_NONE);
        channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        notificationManager.createNotificationChannel(channel);
        return channelId;
    }

    public static long getTotalInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSizeLong();
        long totalBlocks = stat.getBlockCountLong();
        //  return formatSize(totalBlocks * blockSize);
        return availableMb(totalBlocks * blockSize);
    }

    public static long getAvailableInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSizeLong();
        long availableBlocks = stat.getAvailableBlocksLong();
        //return formatSize(availableBlocks * blockSize);
        return availableMb(availableBlocks * blockSize);
    }


    public static long availableMb(long size) {
        /*
        1 KB = 1024 Bytes
        1 MB = 1024 * 1024 Bytes
        1 GB = 1024 * 1024 * 1024 Bytes.
      */
        long mbAvailable = size / (1024 * 1025);
        return mbAvailable;
    }

    public static String formatSize(long size) {
        String suffix = null;

        if (size >= 1024) {
            suffix = "KB";
            size /= 1024;
            if (size >= 1024) {
                suffix = "MB";
                size /= 1024;
            }
        }

        StringBuilder resultBuffer = new StringBuilder(Long.toString(size));
        int commaOffset = resultBuffer.length() - 3;
        while (commaOffset > 0) {
            resultBuffer.insert(commaOffset, ',');
            commaOffset -= 3;
        }

        if (suffix != null)
            resultBuffer.append(suffix);
        return resultBuffer.toString();
    }

    public static boolean isMemorySpaceAvailable(long avSize, long totSize) {
        if (avSize > 0 && totSize > 0) {
            Long per30 = (30 * totSize) / 100;
            /**
             *  MEMORY CRITERIA, AS OF NOW 30% AVAILABLE, CHANGE IT LATTER IF REQUIRED
             */
            if (avSize > per30) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }
}

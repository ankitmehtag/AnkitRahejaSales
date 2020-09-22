package com.sp;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.AppEnums.APIType;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.appnetwork.AsyncThread;
import com.appnetwork.ReqRespBean;
import com.appnetwork.WEBAPI;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.helper.BMHConstants;
import com.helper.ParamsConstants;
import com.helper.UrlFactory;
import com.jsonparser.JsonParser;
import com.model.LoginRespData;
import com.model.ProfileNameUpdatRespModel;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends BaseFragmentActivity implements ConnectivityReceiver.ConnectivityReceiverListener {

    private BMHApplication app;
    private Activity ctx;
    public static final String TAG = ProfileActivity.class.getSimpleName();
    private Button btn_update_profile, btn_change_pwd;
    private EditText et_name, et_email, et_mobile, et_old_pwd, et_new_pwd, et_confirm_pwd;
    private ImageView iv_profile;
    private AsyncThread mAsyncThread = null;
    private final int ACTION_UPDATE_NAME = 1, ACTION_UPDATE_EMAIL = 3, ACTION_UPDATE_IMAGE = 0, ACTION_UPDATE_CONTACT = 2, ACTION_CHANGE_PASSWORD = 4;
    private SimpleMultiPartRequest smr;
    private ProgressBar progressBar;
    private String selectedImagePath;
    private int percentage;
    private Bitmap bitmap;
    protected static final int CAMERA_REQUEST = 0;
    protected static final int GALLERY_PICTURE = 1;
    private String imageName = "temp.jpg";
    private RelativeLayout rl_profile;
    private Toolbar toolbar;
    private LinearLayout ll_change_pwd_root;
    private int hasExtCameraPermission;
    private int hasExtStoragePermission;
    private List<String> permissions = new ArrayList<>();
    String[] mutiPermissions = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_profile);
        app = (BMHApplication) getApplication();
        initViews();
        setClickListener();
        toolbar = setToolBar();
        toolbar.setTitle("Profile");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        imageName = getImageName(String.valueOf(System.currentTimeMillis()));
        if (app.getBooleanFromPrefs(BMHConstants.THIRD_PARTY))
            ll_change_pwd_root.setVisibility(View.GONE);
        else ll_change_pwd_root.setVisibility(View.VISIBLE);
        fetchUserDetails();
    }


    private void initViews() {
        btn_update_profile = (Button) findViewById(R.id.btn_update_profile);
        btn_change_pwd = (Button) findViewById(R.id.btn_change_pwd);
        iv_profile = (ImageView) findViewById(R.id.iv_profile);
        et_name = (EditText) findViewById(R.id.et_name);
        et_email = (EditText) findViewById(R.id.et_email);
        et_mobile = (EditText) findViewById(R.id.et_mobile);

        et_old_pwd = (EditText) findViewById(R.id.et_old_pwd);
        et_new_pwd = (EditText) findViewById(R.id.et_new_pwd);
        et_confirm_pwd = (EditText) findViewById(R.id.et_confirm_pwd);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        rl_profile = (RelativeLayout) findViewById(R.id.rl_profile);
        ll_change_pwd_root = (LinearLayout) findViewById(R.id.ll_change_pwd_root);

    }

    private void setClickListener() {
        btn_update_profile.setOnClickListener(mOnClickListener);
        btn_change_pwd.setOnClickListener(mOnClickListener);
        rl_profile.setOnClickListener(mOnClickListener);

    }

    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_update_profile:
                    if (isValidUsername()) {
                        editNameRequest();
                    }
                    break;
                case R.id.btn_change_pwd:
                    if (isPasswordValidData()) {
                        changePasswordRequest();
                    }
                    break;
                case R.id.rl_profile:
                    capturePicture();
                    break;
            }
        }
    };


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {

            case android.R.id.home:
                finish();
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected String setActionBarTitle() {
        return "Profile";
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {

    }


    private void fetchUserDetails() {
        ReqRespBean mBean = new ReqRespBean();
        mBean.setApiType(APIType.PROFILE_DETAILS);
        mBean.setRequestmethod(WEBAPI.POST);
        mBean.setUrl(WEBAPI.getWEBAPI(APIType.PROFILE_DETAILS));
        mBean.setMimeType(WEBAPI.contentTypeFormData);
        String userId = app.getFromPrefs(BMHConstants.USERID_KEY);
        StringBuilder mStringBuilder = new StringBuilder("");
        mStringBuilder.append(ParamsConstants.USER_ID);
        mStringBuilder.append("=");
        mStringBuilder.append(userId);
        mStringBuilder.append("&");
        mStringBuilder.append(ParamsConstants.BUILDER_ID);
        mStringBuilder.append("=");
        mStringBuilder.append(BMHConstants.CURRENT_BUILDER_ID);
        mBean.setJson(mStringBuilder.toString());
        mBean.setmHandler(mHandler);
        mBean.setCtx(this);
        mAsyncThread = new AsyncThread();
        mAsyncThread.initProgressDialog(this, mOnCancelListener);
        mAsyncThread.execute(mBean);
        mAsyncThread = null;
    }


    private void editNameRequest() {
        ReqRespBean mBean = new ReqRespBean();
        mBean.setApiType(APIType.EDIT_NAME);
        mBean.setRequestmethod(WEBAPI.POST);
        mBean.setUrl(WEBAPI.getWEBAPI(APIType.EDIT_NAME));
        mBean.setMimeType(WEBAPI.contentTypeFormData);
        String userId = app.getFromPrefs(BMHConstants.USERID_KEY);
        StringBuilder mStringBuilder = new StringBuilder("");
        mStringBuilder.append(ParamsConstants.USER_ID);
        mStringBuilder.append("=");
        mStringBuilder.append(userId);
        mStringBuilder.append("&");
        mStringBuilder.append("updation_field=");
        mStringBuilder.append(ACTION_UPDATE_NAME);
        mStringBuilder.append("&");
        mStringBuilder.append("first_name=");
        mStringBuilder.append(et_name.getText().toString().trim());
        mBean.setJson(mStringBuilder.toString());
        mBean.setmHandler(mHandler);
        mBean.setCtx(this);
        mAsyncThread = new AsyncThread();
        mAsyncThread.initProgressDialog(this, mOnCancelListener);
        mAsyncThread.execute(mBean);
        mAsyncThread = null;
    }

    private void changePasswordRequest() {
        ReqRespBean mBean = new ReqRespBean();
        mBean.setApiType(APIType.CHANGE_PASSWORD);
        mBean.setRequestmethod(WEBAPI.POST);
        mBean.setUrl(WEBAPI.getWEBAPI(APIType.CHANGE_PASSWORD));
        mBean.setMimeType(WEBAPI.contentTypeFormData);
        String userId = app.getFromPrefs(BMHConstants.USERID_KEY);
        StringBuilder mStringBuilder = new StringBuilder("");
        mStringBuilder.append(ParamsConstants.USER_ID);
        mStringBuilder.append("=");
        mStringBuilder.append(userId);
        mStringBuilder.append("&");
        mStringBuilder.append("updation_field=");
        mStringBuilder.append(ACTION_CHANGE_PASSWORD);
        mStringBuilder.append("&");
        mStringBuilder.append("old_password=");
        mStringBuilder.append(et_old_pwd.getText().toString().trim());
        mStringBuilder.append("&");
        mStringBuilder.append("new_password=");
        mStringBuilder.append(et_new_pwd.getText().toString().trim());
        mBean.setJson(mStringBuilder.toString());
        mBean.setmHandler(mHandler);
        mBean.setCtx(this);
        mAsyncThread = new AsyncThread();
        mAsyncThread.initProgressDialog(this, mOnCancelListener);
        mAsyncThread.execute(mBean);
        mAsyncThread = null;
    }


    DialogInterface.OnCancelListener mOnCancelListener = new DialogInterface.OnCancelListener() {
        @Override
        public void onCancel(DialogInterface dialog) {
            if (mAsyncThread != null) mAsyncThread.cancel(true);
            mAsyncThread = null;
        }
    };


    Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.obj == null) {
                //showConfirmAlert("","Unable to connect Server.Please try later");
                // showToast("Server error");
                return false;
            } else {
                ReqRespBean mBean = (ReqRespBean) msg.obj;
                switch (mBean.getApiType()) {
                    case EDIT_NAME:
                        ProfileNameUpdatRespModel model = (ProfileNameUpdatRespModel) JsonParser.convertJsonToBean(APIType.EDIT_NAME, mBean.getJson());
                        if (model != null && model.isSuccess()) {
                            app.saveIntoPrefs(BMHConstants.USERNAME_KEY, et_name.getText().toString().trim());
                            showToast(model.getMessage());
                        } else if (model != null) {
                            showToast(model.getMessage());
                        } else {
                            showToast(getString(R.string.something_went_wrong));
                        }
                        break;
                    case PROFILE_DETAILS:
                        LoginRespData basevo = (LoginRespData) JsonParser.convertJsonToBean(APIType.USER_LOGIN, mBean.getJson());
                        if (basevo != null) {
                            if (basevo.getData() != null && basevo.isSuccess()) {
                                et_name.setText(basevo.getData().getFirst_name());
                                et_email.setText(basevo.getData().getEmail());
                                et_mobile.setText(basevo.getData().getPhone_number());
                                app.saveIntoPrefs(BMHConstants.USER_IMAGE_URL, basevo.getData().getUser_image());
                                app.saveIntoPrefs(BMHConstants.USERNAME_KEY, basevo.getData().getFirst_name());
                                if (basevo.getData().getUser_image() != null && !basevo.getData().getUser_image().isEmpty()) {
                                    progressBar.setVisibility(View.VISIBLE);
                                    String imgUrl = UrlFactory.IMG_BASEURL + basevo.getData().getUser_image();
                                    Glide.with(ProfileActivity.this)
                                            .load(imgUrl)
                                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                                            .skipMemoryCache(true)
                                            .error(BMHConstants.NO_IMAGE)
                                            .override(100, 100)
                                            .listener(new RequestListener<String, GlideDrawable>() {
                                                @Override
                                                public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                                    progressBar.setVisibility(View.GONE);
                                                    return false;
                                                }

                                                @Override
                                                public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                                    progressBar.setVisibility(View.GONE);
                                                    return false;
                                                }
                                            }).into(iv_profile);
                                }
                            } else {
                                showToast(basevo.getMessage());
                            }
                        } else {
                            showToast(getString(R.string.something_went_wrong));
                        }

                        break;
                    case CHANGE_PASSWORD:
                        ProfileNameUpdatRespModel changePwd = (ProfileNameUpdatRespModel) JsonParser.convertJsonToBean(APIType.CHANGE_PASSWORD, mBean.getJson());
                        if (changePwd != null && changePwd.isSuccess()) {
                            showToast(changePwd.getMessage());
                            et_old_pwd.setText("");
                            et_new_pwd.setText("");
                            et_confirm_pwd.setText("");
                        } else {
                            showToast(changePwd.getMessage());
                        }
                        break;
                    default:
                        break;
                }
            }
            return true;
        }
    });


    private void imageUpload(final String imagePath) {
        if (imagePath == null || imagePath.isEmpty()) return;
        smr = new SimpleMultiPartRequest(Request.Method.POST, WEBAPI.getWEBAPI(APIType.UPLOAD_PROFILE_IMAGE),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("Response", response);
                        if (response != null) {
                            ProfileNameUpdatRespModel changePwd = (ProfileNameUpdatRespModel) JsonParser.convertJsonToBean(APIType.CHANGE_PASSWORD, response);
                            if (changePwd != null && changePwd.getData() != null && changePwd.isSuccess()) {
                                showToast(changePwd.getMessage());
                                app.saveIntoPrefs(BMHConstants.USER_IMAGE_URL, changePwd.getData().getUser_image());
                            } else {
                                showToast(response);
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error != null && error.getMessage() != null) {
                    //int respCode =  error.networkResponse.statusCode;
                    Log.e(TAG, error.getMessage());
                    showToast(getString(R.string.something_went_wrong) + error.getMessage());
                } else {
                    showToast(getString(R.string.something_went_wrong));
                }
                //btn_make_payment.setEnabled(true);
            }
        });

        smr.setOnProgressListener(mProgressListener);
        smr.addFile("image", imagePath);
        String userid = app.getFromPrefs(BMHConstants.USERID_KEY);
        smr.addStringParam("user_id", userid);
        smr.addStringParam("updation_field", String.valueOf(ACTION_UPDATE_IMAGE));

        BMHApplication.getInstance().addToRequestQueue(smr);
    }

    Response.ProgressListener mProgressListener = new Response.ProgressListener() {
        @Override
        public void onProgress(long transferredBytes, long totalSize) {
            percentage = (int) ((transferredBytes / ((float) totalSize)) * 100);
            if (percentage >= 0 && percentage <= 100) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setProgress(percentage);
                        //tv_progress_value.setText(percentage + "%");
                        if (progressBar.getVisibility() == View.GONE)
                            progressBar.setVisibility(View.VISIBLE);
                        //showToast("Progress"+percentage + "%");
                        if (percentage == 100) {
                            progressBar.setVisibility(View.GONE);
                            //	tv_progress_value.setText("Please wait...");

                        }
                    }
                });
            }
        }
    };

    private String getImageName(String str) {
        return "image_" + str + ".jpg";
    }

    @Override
    public void onPause() {
        if (smr != null) smr.cancel();
        if (mAsyncThread != null)
            mAsyncThread.cancel(true);
        super.onPause();

    }

    @Override
    public void onStop() {
        if (smr != null) smr.cancel();
        super.onStop();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        bitmap = null;
        selectedImagePath = null;
        if (resultCode == RESULT_OK && requestCode == CAMERA_REQUEST) {

            if (data != null && data.getExtras() != null) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");

                // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                Uri tempUri = getImageUri(getApplicationContext(), photo);

                // CALL THIS METHOD TO GET THE ACTUAL PATH
                File finalFile = new File(getRealPathFromURI(tempUri));

                // Save a file: path for use with ACTION_VIEW intents
                selectedImagePath = finalFile.getAbsolutePath();

                Bitmap bm = BitmapFactory.decodeFile(selectedImagePath);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.PNG, 100, baos); //bm is the bitmap object
                byte[] byteArrayImage = baos.toByteArray();
                String encodedImage = Base64.encodeToString(byteArrayImage, Base64.DEFAULT);
                byte[] decodeResponse = Base64.decode(encodedImage, Base64.DEFAULT | Base64.NO_WRAP);
                bitmap = BitmapFactory.decodeByteArray(decodeResponse, 0, decodeResponse.length); // load
                bitmap = Bitmap.createScaledBitmap(bitmap, 300, 300, false);
                bitmap = rotateBitmap(selectedImagePath, bitmap);
                final int sizeInBytes = bitmap.getByteCount();
                if (sizeInBytes <= 5242880) {
                    iv_profile.setImageBitmap(bitmap);
                    imageUpload(selectedImagePath);
                } else {
                    showToast("Image Size must be less than 5 MB");
                    String imgUrl = UrlFactory.IMG_BASEURL + app.getFromPrefs(BMHConstants.USER_IMAGE_URL);
                    Glide.with(ProfileActivity.this)
                            .load(imgUrl)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true)
                            .error(BMHConstants.NO_IMAGE)
                            .override(100, 100).into(iv_profile);
                }
            }

        } else if (resultCode == RESULT_OK && requestCode == GALLERY_PICTURE) {
            if (data != null) {
                Uri selectedImage = data.getData();
                selectedImagePath = getPath(selectedImage);
                if (selectedImagePath != null) {
                    Bitmap bm = BitmapFactory.decodeFile(selectedImagePath);
                    bitmap = getResizedBitmap(bm, 300);
                    bitmap = rotateBitmap(selectedImagePath, bitmap);
                    final int sizeInBytes = bitmap.getByteCount();
                    if (sizeInBytes <= 5242880) {
                        iv_profile.setImageBitmap(bitmap);
                        imageUpload(selectedImagePath);
                    } else {
                        showToast("Image Size must be less than 5 MB");
                        String imgUrl = UrlFactory.IMG_BASEURL + app.getFromPrefs(BMHConstants.USER_IMAGE_URL);
                        Glide.with(ProfileActivity.this)
                                .load(imgUrl)
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .skipMemoryCache(true)
                                .error(BMHConstants.NO_IMAGE)
                                .override(100, 100).into(iv_profile);
                    }
                }
            } else {
                showToast("Canceled");
            }
        }

    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    private String getPath(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(this, contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }

    public static Bitmap rotateBitmap(String src, Bitmap bitmap) {
        try {
            int orientation = getExifOrientation(src);

            if (orientation == 1) {
                return bitmap;
            }

            Matrix matrix = new Matrix();
            switch (orientation) {
                /*case 0:
                    matrix.setRotate(90);
                    break;*/
                case 2:
                    matrix.setScale(-1, 1);
                    break;
                case 3:
                    matrix.setRotate(180);
                    break;
                case 4:
                    matrix.setRotate(180);
                    matrix.postScale(-1, 1);
                    break;
                case 5:
                    matrix.setRotate(90);
                    matrix.postScale(-1, 1);
                    break;
                case 6:
                    matrix.setRotate(90);
                    break;
                case 7:
                    matrix.setRotate(-90);
                    matrix.postScale(-1, 1);
                    break;
                case 8:
                    matrix.setRotate(-90);
                    break;
                default:
                    return bitmap;
            }

            try {
                Bitmap oriented = Bitmap.createBitmap(bitmap, 0, 0,
                        bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                bitmap.recycle();
                return oriented;
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
                return bitmap;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    private void capturePicture() {
        AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(this);
        myAlertDialog.setTitle("Upload Pictures Option");
        myAlertDialog.setMessage("How do you want to set your picture?");

        myAlertDialog.setPositiveButton("Gallery",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (checkGalleryPermissions()) {
                                openGallery();
                            } else {
                                requestGalleryPermissions();
                            }
                        } else {
                            openGallery();
                        }
                    }
                });

        myAlertDialog.setNegativeButton("Camera", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkPermissions()) {
                        openCamera();
                    } else {
                        requestCameraPermissions();
                    }
                } else {
                    openCamera();
                }
            }
        });
        myAlertDialog.show();
    }

    private boolean checkGalleryPermissions() {
        hasExtStoragePermission = ContextCompat.checkSelfPermission(ProfileActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (hasExtStoragePermission == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    private boolean checkCameraPermissions() {
        hasExtCameraPermission = ContextCompat.checkSelfPermission(ProfileActivity.this, android.Manifest.permission.CAMERA);
        if (hasExtCameraPermission == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    private boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : mutiPermissions) {
            result = ContextCompat.checkSelfPermission(this, p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), BMHConstants.CAMERA_STORAGE_PERMISSION);
            return false;
        }
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestGalleryPermissions() {
        if (hasExtStoragePermission != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!permissions.isEmpty()) {
            requestPermissions(permissions.toArray(new String[permissions.size()]), BMHConstants.GALLERY_PERMISSION_CODE);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestCameraPermissions() {
        if (hasExtCameraPermission != PackageManager.PERMISSION_GRANTED) {
            permissions.add(android.Manifest.permission.CAMERA);
        }
        if (!permissions.isEmpty()) {
            requestPermissions(permissions.toArray(new String[permissions.size()]), BMHConstants.PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case BMHConstants.CAMERA_STORAGE_PERMISSION: {
                String permissionsDenied = "";
                for (int i = 0; i < permissions.length; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                        permissionsDenied += "\n" + i;
                    }
                }
                if (TextUtils.isEmpty(permissionsDenied))
                    openCamera();
            }
            break;
            case BMHConstants.GALLERY_PERMISSION_CODE: {
                for (int i = 0; i < permissions.length; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        openGallery();
                    } else if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                        Log.d("Permissions", "Permission Denied: " + permissions[i]);
                    }
                }
            }
            break;
            default: {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }
    }

    private void openGallery() {
        Intent pictureActionIntent = null;
        pictureActionIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pictureActionIntent, GALLERY_PICTURE);
    }

    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //  File f = new File(android.os.Environment.getExternalStorageDirectory(), imageName);
        // intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
        startActivityForResult(intent, CAMERA_REQUEST);
    }

    private boolean isPasswordValidData() {
        if (et_old_pwd.getText().toString().trim().isEmpty()) {
            showToast("Enter old password");
            return false;
        } else if (et_new_pwd.getText().toString().trim().isEmpty()) {
            showToast("Enter new password");
            return false;
        } else if (et_new_pwd.getText().toString().trim().length() < 6) {
            showToast("Minimum 6 digit length required");
            et_new_pwd.requestFocus();
            return false;
        } else if (et_new_pwd.getText().toString().trim().equalsIgnoreCase(et_old_pwd.getText().toString().trim())) {
            showToast("Old password and new password should not be same.");
            return false;
        } else if (et_confirm_pwd.getText().toString().trim().isEmpty()) {
            showToast("Confirm password");
            return false;
        } else if (et_confirm_pwd.getText().toString().trim().length() < 6) {
            showToast("Minimum 6 digit length required");
            et_confirm_pwd.requestFocus();
            return false;
        } else if (!et_new_pwd.getText().toString().trim().equalsIgnoreCase(et_confirm_pwd.getText().toString().trim())) {
            showToast("Password miss match");
            return false;
        }
        return true;
    }

    private boolean isValidUsername() {
        if (et_name.getText().toString().trim().isEmpty()) {
            showToast("Enter name");
            return false;
        }
        return true;
    }


    private void saveUserInfo(LoginRespData basevo) {
        if (basevo == null) return;
        if (basevo.getData() != null) {
            app.saveIntoPrefs(BMHConstants.USERID_KEY, basevo.getData().getUser_id());
            app.saveIntoPrefs(BMHConstants.USERNAME_KEY, basevo.getData().getFirst_name() + " " + basevo.getData().getLast_name());
            app.saveIntoPrefs(BMHConstants.SP_CODE, basevo.getData().getEmp_code());
            app.saveIntoPrefs(BMHConstants.USER_IMAGE_URL, basevo.getData().getUser_image());
            app.saveIntoPrefs(BMHConstants.USER_EMAIL, basevo.getData().getEmail());
            app.saveIntoPrefs(BMHConstants.MOBILE_KEY, basevo.getData().getPhone_number());
        }
    }

    private static int getExifOrientation(String src) throws IOException {
        int orientation = 1;

        try {
            /**
             * if your are targeting only api level >= 5 ExifInterface exif =
             * new ExifInterface(src); orientation =
             * exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
             */
            if (Build.VERSION.SDK_INT >= 5) {
                Class<?> exifClass = Class
                        .forName("android.media.ExifInterface");
                Constructor<?> exifConstructor = exifClass
                        .getConstructor(new Class[]{String.class});
                Object exifInstance = exifConstructor
                        .newInstance(new Object[]{src});
                Method getAttributeInt = exifClass.getMethod("getAttributeInt",
                        new Class[]{String.class, int.class});
                Field tagOrientationField = exifClass
                        .getField("TAG_ORIENTATION");
                String tagOrientation = (String) tagOrientationField.get(null);
                orientation = (Integer) getAttributeInt.invoke(exifInstance,
                        new Object[]{tagOrientation, 1});
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        return orientation;
    }
}

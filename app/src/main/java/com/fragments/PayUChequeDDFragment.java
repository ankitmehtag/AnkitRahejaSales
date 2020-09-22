package com.fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.AppEnums.APIType;
import com.AppEnums.UIEventType;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.appnetwork.WEBAPI;
import com.helper.ParamsConstants;
import com.sp.BMHApplication;
import com.sp.ConnectivityReceiver;
import com.sp.PayUPaymentProcessActivity;
import com.sp.PaymentStatusActivity;
import com.sp.R;
import com.helper.BMHConstants;
import com.helper.IntentDataObject;
import com.jsonparser.JsonParser;
import com.model.ChequeDDPaymentRespModel;
import com.model.NetworkErrorObject;
import com.utils.Utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Naresh on 27-Apr-17.
 */

public class PayUChequeDDFragment extends Fragment implements ConnectivityReceiver.ConnectivityReceiverListener {

    private final String TAG = PayUChequeDDFragment.class.getSimpleName();
    private View view;
    private Button btn_upload_cheque_or_dd, btn_make_payment;
    private EditText et_cheque_number, et_bank_name, et_ammount;
    private ImageView cheque_dd_preview;
    private TextView tv_cheque_dd_date_title, tv_cheque_dd_date_value, tv_progress_value;
    private RelativeLayout rl_dob_root;
    private ProgressBar progressBar;
    private Spinner sp_cheque_dd;

    protected static final int CAMERA_REQUEST = 0;
    protected static final int GALLERY_PICTURE = 1;
    private Intent pictureActionIntent = null;
    Bitmap bitmap;
    private String type = "Cheque/DD";
    String selectedImagePath;

    private Calendar SELECTED_CALENDER;
    private int mSelectedDay, mSelectedMonth, mSelectedYear;
    private DateFormat df_ddMMyyyy = null;
    private BMHApplication app = null;
    private String imageName = "temp.jpg";
    private final int CHEQUE = 3;
    private final int DD = 4;
    private int spSelectedPos = CHEQUE;
    private String unitId = "";
    private int percentage;
    private NetworkErrorObject mNetworkErrorObject = null;
    private int hasExtStoragePermission;
    private int hasExtCameraPermission;
    private List<String> permissions = new ArrayList<>();
    private SimpleMultiPartRequest smr;
    android.app.DatePickerDialog datePicker = null;
    String[] mutiPermissions = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA};

    public PayUChequeDDFragment() {
    }
    /*@Override
    public void onResume() {
        super.onResume();
        BMHApplication.getInstance().setConnectivityListener(this);
    }*/

    @Override
    public void onPause() {
        if (smr != null) smr.cancel();
        super.onPause();

    }

    @Override
    public void onStop() {
        if (smr != null) smr.cancel();
        super.onStop();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_payu_cheque_dd, container, false);
        initView();
        setListeners();
        app = (BMHApplication) getActivity().getApplication();
        df_ddMMyyyy = new SimpleDateFormat("dd-MM-yyyy");
        if (getActivity() instanceof PayUPaymentProcessActivity) {
            PayUPaymentProcessActivity mPayUPaymentProcessActivity = (PayUPaymentProcessActivity) getActivity();
            unitId = mPayUPaymentProcessActivity.mUpdatePersonalDetailRespModel.getData().getService_provider_information().getUnit_id();
            //unitId = getUnitId(productInfo);
            imageName = getImageName(unitId);
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        // cal.add(Calendar.DAY_OF_YEAR, -7); // to get one week ego date
        cal.add(Calendar.MONTH, -2); // to get two month ego date

        SELECTED_CALENDER = cal;
        mSelectedYear = SELECTED_CALENDER.get(Calendar.YEAR);
        mSelectedMonth = SELECTED_CALENDER.get(Calendar.MONTH);
        mSelectedDay = SELECTED_CALENDER.get(Calendar.DAY_OF_MONTH);
        datePicker = new android.app.DatePickerDialog(getActivity(), onDateSet, mSelectedYear, mSelectedMonth, mSelectedDay);
        datePicker.getDatePicker().setMinDate(SELECTED_CALENDER.getTimeInMillis());
        return view;
    }

    private void initView() {
        btn_upload_cheque_or_dd = (Button) view.findViewById(R.id.btn_upload_cheque_or_dd);
        btn_make_payment = (Button) view.findViewById(R.id.btn_make_payment);
        et_cheque_number = (EditText) view.findViewById(R.id.et_cheque_number);
        et_bank_name = (EditText) view.findViewById(R.id.et_bank_name);
        et_ammount = (EditText) view.findViewById(R.id.et_ammount);
        cheque_dd_preview = (ImageView) view.findViewById(R.id.cheque_dd_preview);
        tv_cheque_dd_date_title = (TextView) view.findViewById(R.id.tv_cheque_dd_date_title);
        tv_cheque_dd_date_value = (TextView) view.findViewById(R.id.tv_cheque_dd_date_value);
        tv_progress_value = (TextView) view.findViewById(R.id.tv_progress_value);
        rl_dob_root = (RelativeLayout) view.findViewById(R.id.rl_dob_root);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        sp_cheque_dd = (Spinner) view.findViewById(R.id.sp_cheque_dd);
    }

    private void setListeners() {
        btn_make_payment.setOnClickListener(mOnClickListener);
        btn_upload_cheque_or_dd.setOnClickListener(mOnClickListener);
        rl_dob_root.setOnClickListener(mOnClickListener);
        sp_cheque_dd.setOnItemSelectedListener(mOnItemSelectedListener);
    }

    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_make_payment:
                    if (isValidData()) {
                        if (ConnectivityReceiver.isConnected()) {
                            uploadChequeDD();
                        } else {
                            mNetworkErrorObject = Utils.showNetworkErrorDialog(getActivity(), UIEventType.RETRY_MAKE_PAYMENT, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (ConnectivityReceiver.isConnected()) {
                                        uploadChequeDD();
                                    } else {
                                        Utils.showToast(getActivity(), getString(R.string.check_your_internet_connection));
                                    }
                                }
                            });
                        }
                    }
                    break;
                case R.id.btn_upload_cheque_or_dd:
                    capturePicture();
                    break;
                case R.id.rl_dob_root:
                    datePicker.show();
                    break;
            }
        }
    };

    private void uploadChequeDD() {
        progressBar.setVisibility(View.VISIBLE);
        tv_progress_value.setVisibility(View.VISIBLE);
        tv_progress_value.setText("");
        imageUpload(selectedImagePath);
        btn_make_payment.setEnabled(false);
    }

    private void capturePicture() {
        AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(getActivity());
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

    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA_REQUEST);
    }

    private void openGallery() {
        Intent pictureActionIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pictureActionIntent, GALLERY_PICTURE);
    }

    private boolean checkGalleryPermissions() {
        hasExtStoragePermission = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (hasExtStoragePermission == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    private boolean checkCameraPermissions() {
        hasExtCameraPermission = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA);
        if (hasExtCameraPermission == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    private boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : mutiPermissions) {
            result = ContextCompat.checkSelfPermission(getActivity(), p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(getActivity(), listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), BMHConstants.CAMERA_STORAGE_PERMISSION);
            return false;
        }
        return true;
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

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestGalleryPermissions() {
        if (hasExtStoragePermission != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!permissions.isEmpty()) {
            requestPermissions(permissions.toArray(new String[permissions.size()]), BMHConstants.GALLERY_PERMISSION_CODE);
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
                        Log.d(TAG, "Permission Denied: " + permissions[i]);
                    }
                }
            }
            break;
            default: {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        bitmap = null;
        selectedImagePath = null;
        if (resultCode == RESULT_OK && requestCode == CAMERA_REQUEST) {

            Bitmap photo = (Bitmap) data.getExtras().get("data");

            // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
            Uri tempUri = getImageUri(getActivity(), photo);

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
            cheque_dd_preview.setVisibility(View.VISIBLE);
            cheque_dd_preview.setImageBitmap(bitmap);

        } else if (resultCode == RESULT_OK && requestCode == GALLERY_PICTURE) {
            if (data != null) {
                Uri selectedImage = data.getData();
                selectedImagePath = getPath(selectedImage);
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor c = getActivity().getContentResolver().query(selectedImage, filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                selectedImagePath = c.getString(columnIndex);
                c.close();
                if (selectedImagePath != null) {
                    bitmap = BitmapFactory.decodeFile(selectedImagePath); // load
                    bitmap = Bitmap.createScaledBitmap(bitmap, 400, 400, false);
                    cheque_dd_preview.setImageBitmap(bitmap);
                    cheque_dd_preview.setVisibility(View.VISIBLE);
                }
            } else {
                Toast.makeText(getActivity(), "Canceled", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private String getPath(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(getActivity(), contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    private DatePickerDialog.OnDateSetListener onDateSet = new android.app.DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mSelectedDay = dayOfMonth;
            mSelectedMonth = monthOfYear;
            mSelectedYear = year;
            SELECTED_CALENDER.set(mSelectedYear, mSelectedMonth, mSelectedDay, 0, 0);
            tv_cheque_dd_date_value.setText(df_ddMMyyyy.format(SELECTED_CALENDER.getTime()));
        }
    };


    private void imageUpload(final String imagePath) {
        if (imagePath == null || imagePath.isEmpty()) return;
        smr = new SimpleMultiPartRequest(Request.Method.POST, WEBAPI.getWEBAPI(APIType.UPLOAD_IMAGE),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("Response", response);
                        btn_make_payment.setEnabled(true);
                        //showToast(response);
                        ChequeDDPaymentRespModel chequeDDPaymentRespModel = (ChequeDDPaymentRespModel) JsonParser.convertJsonToBean(APIType.CHEQUE_DD_RESPONSE, response);
                        if (chequeDDPaymentRespModel != null && chequeDDPaymentRespModel.getData() != null) {
                            IntentDataObject mIntentDataObject = new IntentDataObject();
                            if (chequeDDPaymentRespModel.getIsSuccess() == 1) {
                                mIntentDataObject.putData(getString(R.string.payment_status), getString(R.string.txt_success));
                            } else {
                                mIntentDataObject.putData(getString(R.string.payment_status), getString(R.string.txt_failure));
                            }
                            mIntentDataObject.putData(getString(R.string.txt_payment_mode), type);
                            for (ChequeDDPaymentRespModel.Details mDetails : chequeDDPaymentRespModel.getData()) {
                                if (mDetails != null) {
                                    mIntentDataObject.putData(mDetails.getCaption(), mDetails.getValue());
                                }
                            }
                            Intent mIntent = new Intent(getActivity(), PaymentStatusActivity.class);
                            mIntent.putExtra(IntentDataObject.OBJ, mIntentDataObject);
                            startActivity(mIntent);
                            getActivity().finish();
                        } else {
                            //TODO:
                            showToast(getString(R.string.something_went_wrong));
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
                btn_make_payment.setEnabled(true);
            }
        });

        smr.setOnProgressListener(mProgressListener);
        smr.addFile("image", imagePath);
        smr.addStringParam("unit_id", unitId);
        String userid = app.getFromPrefs(BMHConstants.USERID_KEY);
        smr.addStringParam("user_id", userid);
        smr.addStringParam("payment_type", String.valueOf(spSelectedPos));
        smr.addStringParam("cheque_dd_number", et_cheque_number.getText().toString());
        smr.addStringParam("bank_name", et_bank_name.getText().toString());
        smr.addStringParam("amount", et_ammount.getText().toString());
        smr.addStringParam(ParamsConstants.APP_TYPE, BMHConstants.SALES_PERSON);
        smr.addStringParam("cheque_dd_date", tv_cheque_dd_date_value.getText().toString());
        //smr.addMultipartParam("unit_id","","3939");
        BMHApplication.getInstance().addToRequestQueue(smr);
    }

    Response.ProgressListener mProgressListener = new Response.ProgressListener() {
        @Override
        public void onProgress(long transferredBytes, long totalSize) {
            percentage = (int) ((transferredBytes / ((float) totalSize)) * 100);
            if (percentage >= 0 && percentage <= 100) {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setProgress(percentage);
                            tv_progress_value.setText(percentage + "%");
                            if (percentage == 100) {
                                progressBar.setVisibility(View.GONE);
                                tv_progress_value.setText("Please wait...");

                            }
                        }
                    });
                }
            }
        }
    };
    AdapterView.OnItemSelectedListener mOnItemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (position == 0) {
                et_cheque_number.setHint(getString(R.string.cheque_number));
                tv_cheque_dd_date_title.setText(getString(R.string.cheque_date));
                btn_upload_cheque_or_dd.setText(getString(R.string.attach_cheque));
                type = "Cheque";
                spSelectedPos = CHEQUE;
            } else if (position == 1) {
                et_cheque_number.setHint(getString(R.string.dd_number));
                tv_cheque_dd_date_title.setText(getString(R.string.dd_date));
                btn_upload_cheque_or_dd.setText(getString(R.string.attach_dd));
                spSelectedPos = DD;
                type = "Demand Draft";
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            // spSelectedPos = CHEQUE;
        }
    };

    private void showToast(String msg) {
        if (getActivity() != null) {
            Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isValidData() {
        if (et_cheque_number.getText().toString().isEmpty()) {
            showToast("Enter " + type + " number");
            return false;
        } else if (et_bank_name.getText().toString().isEmpty()) {
            showToast("Enter bank name");
            return false;
        } else if (et_ammount.getText().toString().isEmpty()) {
            showToast("Enter " + type + " amount");
            return false;
        } else if (Utils.toInt(et_ammount.getText().toString()) == 0) {
            showToast(type + " amount can't be 0");
            return false;
        } else if (tv_cheque_dd_date_value.getText().toString().isEmpty() ||
                tv_cheque_dd_date_value.getText().toString().equalsIgnoreCase(getString(R.string.ddmmyyyy))) {
            showToast("Choose " + type + " date");
            return false;
        } else if (selectedImagePath == null || selectedImagePath.isEmpty()) {
            showToast("Attache " + type + " image");
            return false;
        }
        return true;
    }

    private String getUnitId(String str) {
        if (str == null) return "";
        if (str.indexOf("-") != -1) {
            return str.substring(0, str.indexOf("-"));
        }
        return "";
    }

    private String getImageName(String str) {
        return "image_" + str + ".jpg";
    }


    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (isConnected) {
            if (mNetworkErrorObject == null || mNetworkErrorObject.getUiEventType() == null || mNetworkErrorObject.getAlertDialog() == null)
                return;
            switch (mNetworkErrorObject.getUiEventType()) {
                case RETRY_MAKE_PAYMENT:
                    uploadChequeDD();
                    break;

            }
            mNetworkErrorObject.getAlertDialog().dismiss();
            mNetworkErrorObject = null;
        }
    }
}

package com.sp;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.AppEnums.APIType;
import com.appnetwork.AsyncThread;
import com.appnetwork.ReqRespBean;
import com.appnetwork.WEBAPI;
import com.fragments.ContactUsFragment;
import com.helper.BMHConstants;
import com.model.BaseRespModel;
import com.model.ContactUsRespModel;
import com.utils.Connectivity;
import com.utils.StringUtil;
import com.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class ContactUsActivity extends BaseFragmentActivity {

    private BMHApplication app;
    private Activity ctx;
    private WebView web;
    public static final String TAG = ContactUsFragment.class.getSimpleName();
    private EditText editFirst, editEmail, editPhone, editMsg;
    private ImageButton img;
    private Button btn;
    private TextView tv_drop_address, tv_contact_no, tv_add1, tv_add2;
    private ContactUsRespModel contactUsRespModel;
    private Toolbar toolbar;
    private ImageView iv_drop_in, iv_connect_online;
    private AsyncThread mAsyncThread = null;
    private int hasExtCallPermission;
    private List<String> permissions = new ArrayList<>();
    LinearLayout parentLayout, defaultLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = (BMHApplication) getApplication();
        setContentView(R.layout.activity_contact_us);
        initViews();
        setListeners();
        toolbar = setToolBar();
        toolbar.setTitle("Contact Us");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //loadingTask.execute("");
        if (Connectivity.isConnected(this)) {
            parentLayout.setVisibility(View.VISIBLE);
            defaultLayout.setVisibility(View.GONE);
            getContactData();
        } else {
            parentLayout.setVisibility(View.GONE);
            defaultLayout.setVisibility(View.VISIBLE);
        }
    }

    private void setListeners() {
        img.setOnClickListener(mOnClickListener);
        btn.setOnClickListener(mOnClickListener);
        iv_drop_in.setOnClickListener(mOnClickListener);
        tv_drop_address.setOnClickListener(mOnClickListener);
        iv_connect_online.setOnClickListener(mOnClickListener);
        tv_add1.setOnClickListener(mOnClickListener);
        tv_add2.setOnClickListener(mOnClickListener);
        tv_contact_no.setOnClickListener(mOnClickListener);
    }

    private void initViews() {
        editFirst = (EditText) findViewById(R.id.editTextName);
        editEmail = (EditText) findViewById(R.id.editTextEmail);
        editPhone = (EditText) findViewById(R.id.editTextPhone);
        editMsg = (EditText) findViewById(R.id.editTextMessage);
        img = (ImageButton) findViewById(R.id.Give);
        btn = (Button) findViewById(R.id.buttonSend);
        iv_drop_in = (ImageView) findViewById(R.id.iv_drop_in);
        iv_connect_online = (ImageView) findViewById(R.id.iv_connect_online);
        tv_drop_address = (TextView) findViewById(R.id.tv_drop_address);
        tv_contact_no = (TextView) findViewById(R.id.tv_contact_no);
        tv_add1 = (TextView) findViewById(R.id.tv_add1);
        tv_add2 = (TextView) findViewById(R.id.tv_add2);
        parentLayout = findViewById(R.id.parentLayout);
        defaultLayout = findViewById(R.id.default_layout);
    }

    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.Give:
                case R.id.tv_contact_no:
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (checkPermissions()) {
                            actionCall();
                        } else {
                            requestPermissions();
                        }
                    } else {
                        actionCall();
                    }
                    break;
                case R.id.buttonSend:
                    if (isValidData()) {
                        sendContactEnquiryData();
                    }
                    break;
                case R.id.tv_add1:
                case R.id.iv_connect_online:
                    if (contactUsRespModel != null && contactUsRespModel.getData() != null && contactUsRespModel.getData().getEmail1() != null && !contactUsRespModel.getData().getEmail1().isEmpty()) {
                        Utils.openMailClient(ContactUsActivity.this, "Enquiry", new String[]{contactUsRespModel.getData().getEmail1()}, "");
                    }
                    break;
                case R.id.tv_add2:
                    if (contactUsRespModel != null && contactUsRespModel.getData() != null && contactUsRespModel.getData().getEmail2() != null && !contactUsRespModel.getData().getEmail2().isEmpty()) {
                        Utils.openMailClient(ContactUsActivity.this, "Enquiry", new String[]{contactUsRespModel.getData().getEmail1()}, "");
                    }
                    break;
                case R.id.iv_drop_in:
                case R.id.tv_drop_address:
                    if (contactUsRespModel != null && contactUsRespModel.getData() != null && contactUsRespModel.getData().getAddress() != null && !contactUsRespModel.getData().getAddress().isEmpty()) {
                        openGoogleMap(contactUsRespModel.getData().getAddress());
                    }
                    break;
            }
        }
    };

    public void actionCall() {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + contactUsRespModel.getData().getPhone()));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        startActivity(callIntent);
    }

    private boolean checkPermissions() {
        hasExtCallPermission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE);
        return hasExtCallPermission == PackageManager.PERMISSION_GRANTED;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestPermissions() {
        if (hasExtCallPermission != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.CALL_PHONE);
        }
        if (!permissions.isEmpty()) {
            requestPermissions(permissions.toArray(new String[permissions.size()]), BMHConstants.PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case BMHConstants.PERMISSION_CODE: {
                for (int i = 0; i < permissions.length; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        actionCall();
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

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //	getMenuInflater().inflate(R.menu.contact_us, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings:
                break;
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected String setActionBarTitle() {
        // TODO Auto-generated method stub
        return "ContactUs";
    }

    protected boolean checkMobileValidity(char firstCharacter) {
        boolean validNo = false;
        switch (firstCharacter) {
            case '7':
                validNo = true;
                break;
            case '8':
                validNo = true;
                break;
            case '9':
                validNo = true;
                break;
        }
        return validNo;
    }

    private boolean isValidData() {
        String pName = editFirst.getText().toString().trim();
        String uEmail = editEmail.getText().toString().trim();
        String uPhone = editPhone.getText().toString().trim();
        String message = editMsg.getText().toString().trim();

        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (pName.isEmpty()) {
            showToast("Enter your first name.");
            app.shakeEdittext(editFirst);
            editFirst.requestFocus();
            return false;
        } else if (StringUtil.checkSpecialCharacter(pName)) {
            showToast("Special character and digits are not allowed in Name.");
            app.shakeEdittext(editFirst);
            editFirst.requestFocus();
            return false;
        } else if (pName.length() < 3) {
            showToast("Name can not be less then 3 characters.");
            app.shakeEdittext(editFirst);
            editFirst.requestFocus();
            return false;
        } else if (uEmail.equals("")) {
            showToast("Please enter Email.");
            app.shakeEdittext(editEmail);
            editEmail.requestFocus();
            return false;
        } else if (!uEmail.matches(emailPattern) && editEmail.length() > 0) {
            showToast("Please enter a valid Email.");
            app.shakeEdittext(editEmail);
            editEmail.requestFocus();
            return false;
        } else if (uPhone.equals("")) {
            showToast("Please enter Mobile Number.");
            app.shakeEdittext(editPhone);
            editPhone.requestFocus();
            return false;
        } else if (uPhone.length() < 10) {
            showToast("Please enter valid Mobile Number.");
            app.shakeEdittext(editPhone);
            editPhone.requestFocus();
            return false;
        } else if (!checkMobileValidity(uPhone.charAt(0))) {
            showToast("Please enter valid Mobile Number.");
            app.shakeEdittext(editPhone);
            editPhone.requestFocus();
            return false;
        } else if (message.isEmpty()) {
            showToast("Enter your message.");
            app.shakeEdittext(editMsg);
            editMsg.requestFocus();
            return false;
        } else {
            return true;
        }
    }

    private void getContactData() {
        ReqRespBean mBean = new ReqRespBean();
        mBean.setApiType(APIType.GET_CONTACT_US_DATA);
        mBean.setRequestmethod(WEBAPI.POST);
        mBean.setUrl(WEBAPI.getWEBAPI(APIType.GET_CONTACT_US_DATA));
        mBean.setMimeType(WEBAPI.contentTypeFormData);
        //if(dataParams != null && dataParams.length() > 0) mBean.setJson(dataParams);
        mBean.setmHandler(mHandler);
        mBean.setCtx(this);
        mAsyncThread = new AsyncThread();
        mAsyncThread.initProgressDialog(ContactUsActivity.this, mOnCancelListener);
        mAsyncThread.execute(mBean);
        mAsyncThread = null;
    }

    private void sendContactEnquiryData() {
        final String param = "firstname=" + editFirst.getText().toString().trim() + "&lastname=" + "" + "&contactno=" + editPhone.getText().toString().trim()
                + "&email=" + editEmail.getText().toString().trim() + "&message=" + editMsg.getText().toString().trim();

        ReqRespBean mBean = new ReqRespBean();
        mBean.setApiType(APIType.CONTACT_ENQUIRY);
        mBean.setRequestmethod(WEBAPI.POST);
        mBean.setUrl(WEBAPI.getWEBAPI(APIType.CONTACT_ENQUIRY));
        mBean.setMimeType(WEBAPI.contentTypeFormData);
        if (param != null && param.length() > 0) mBean.setJson(param);
        mBean.setmHandler(mHandler);
        mBean.setCtx(this);
        mAsyncThread = new AsyncThread();
        mAsyncThread.initProgressDialog(ContactUsActivity.this, mOnCancelListener);
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
                return false;
            } else {
                ReqRespBean mBean = (ReqRespBean) msg.obj;
                Log.i(TAG, mBean.getJson());
                switch (mBean.getApiType()) {
                    case GET_CONTACT_US_DATA:
                        if (mBean.getJson() != null) {
                            contactUsRespModel = (ContactUsRespModel) com.jsonparser.JsonParser.convertJsonToBean(APIType.GET_CONTACT_US_DATA, mBean.getJson());
                            if (contactUsRespModel != null) {
                                if (contactUsRespModel.isSuccess() && contactUsRespModel.getData() != null) {
                                    tv_add1.setText(contactUsRespModel.getData().getEmail1());
                                    tv_add2.setText(contactUsRespModel.getData().getEmail2());
                                    tv_drop_address.setText(contactUsRespModel.getData().getAddress());
                                    tv_contact_no.setText(contactUsRespModel.getData().getPhone());
                                }
                            } else {
                                showToast(getString(R.string.something_went_wrong));
                            }
                        }

                        break;
                    case CONTACT_ENQUIRY:
                        if (mBean.getJson() != null) {
                            BaseRespModel respModel = (BaseRespModel) com.jsonparser.JsonParser.convertJsonToBean(APIType.CONTACT_ENQUIRY, mBean.getJson());
                            if (respModel != null) {
                                showToast(respModel.getMessage());
                                finish();
                            } else {
                                showToast(getString(R.string.something_went_wrong));
                            }
                        }

                        break;
                    default:
                        break;
                }
            }
            return true;
        }
    });


    private void openGoogleMap(String yourAddress) {
        try {
            String uri = "http://maps.google.co.in/maps?q=" + yourAddress;
            //String uri = String.format(Locale.ENGLISH, "geo:%f,%f", latitude, longitude);
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openMailClient(String subject, String[] to, String bodyText) {
        try {
            Intent mailer = new Intent(Intent.ACTION_SEND);
            //mailer.setType("text/plain");
            mailer.setType("message/rfc822");
            mailer.putExtra(Intent.EXTRA_EMAIL, to);
            mailer.putExtra(Intent.EXTRA_SUBJECT, subject);
            mailer.putExtra(Intent.EXTRA_TEXT, bodyText);
            startActivity(Intent.createChooser(mailer, "Send email..."));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

package com.fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.VO.ContactDataVO;
import com.sp.BMHApplication;
import com.sp.CustomAsyncTask;
import com.sp.CustomAsyncTask.AsyncListner;
import com.sp.ProjectsListActivity;
import com.sp.R;
import com.exception.BMHException;
import com.helper.BMHConstants;
import com.helper.ContentLoader;
import com.helper.UrlFactory;
import com.model.ContactUsModel;
import com.utils.StringUtil;

import org.json.JSONException;
import org.json.JSONObject;

public class ContactUsFragment extends BaseFragment {

    private View rootView;
    private BMHApplication app;
    private Activity ctx;
    private WebView web;
    public static final String TAG = ContactUsFragment.class.getSimpleName();

    private ContactDataVO contactData;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // if (rootView == null) {
        rootView = inflater.inflate(R.layout.activity_contact_us, container, false);
        app = (BMHApplication) getActivity().getApplication();
        ctx = getActivity();

        final EditText editFirst = (EditText) rootView.findViewById(R.id.editTextName);
        final EditText editEmail = (EditText) rootView.findViewById(R.id.editTextEmail);
        final EditText editPhone = (EditText) rootView.findViewById(R.id.editTextPhone);
        final EditText editMsg = (EditText) rootView.findViewById(R.id.editTextMessage);
        if (app.getFromPrefs(BMHConstants.USERNAME_KEY) != null)
            editFirst.setText(app.getFromPrefs(BMHConstants.USERNAME_KEY));
        if (app.getFromPrefs(BMHConstants.USER_EMAIL) != null)
            editEmail.setText(app.getFromPrefs(BMHConstants.USER_EMAIL));
        // final TextView tvAddress = (TextView)
        // rootView.findViewById(R.id.drop_address);
        // TextView tvGive = (TextView) rootView.findViewById(R.id.give_us_a);
        // TextView tvOnLine = (TextView) rootView.findViewById(R.id.textadd);

        ImageButton img = (ImageButton) rootView.findViewById(R.id.Give);
        img.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse(BMHConstants.CUSTOME_CARE));
                startActivity(callIntent);
            }
        });

        Button btn = (Button) rootView.findViewById(R.id.buttonSend);
        btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                String pName = editFirst.getText().toString().trim();
                String uEmail = editEmail.getText().toString().trim();
                String uPhone = editPhone.getText().toString().trim();
                String message = editMsg.getText().toString().trim();

                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

                if (pName.isEmpty()) {
                    app.showToastAtCenter(ctx, "Enter your first name.");
                } else if (StringUtil.checkSpecialCharacter(pName)) {
                    app.showToastAtCenter(ctx, "Special character and digits are not allowed in Name.");
                    app.shakeEdittext(editFirst);
                } else if (pName.length() < 3) {
                    app.showToastAtCenter(ctx, "Name can not be less then 3 characters.");
                    app.shakeEdittext(editFirst);
                } else if (uEmail.equals("")) {
                    app.showToastAtCenter(ctx, "Please enter Email.");
                    // app.shakeEdittext(edEmail);
                } else if (!uEmail.matches(emailPattern) && editEmail.length() > 0) {
                    app.showToastAtCenter(ctx, "Please enter a valid Email.");
                    // app.shakeEdittext(edEmail);
                }
                // else if (uPhone.equals("")) {
                // app.showToastAtCenter(ctx, "Please enter Mobile Number.");
                // // app.shakeEdittext(edMobileNo);
                // } else if (uPhone.length() < 10) {
                // app.showToastAtCenter(ctx, "Please enter valid Mobile
                // Number.");
                // // app.shakeEdittext(edMobileNo);
                // }

                else if (uPhone.equals("")) {
                    app.showToastAtCenter(ctx, "Please enter Mobile Number.");
                    app.shakeEdittext(editPhone);
                } else if (uPhone.length() < 10) {
                    app.showToastAtCenter(ctx, "Please enter valid Mobile Number.");
                    app.shakeEdittext(editPhone);

                } else if (!checkMobileValidity(uPhone.charAt(0))) {
                    app.showToastAtCenter(ctx, "Please enter valid Mobile Number.");
                    app.shakeEdittext(editPhone);
                } else if (message.isEmpty()) {
                    app.showToastAtCenter(ctx, "Enter your message.");
                } else {
                    // dialog.dismiss();

                    final String url = UrlFactory.getContactUs();
                    final String param = "firstname=" + pName + "&lastname=" + "" + "&contactno=" + uPhone
                            + "&email=" + uEmail + "&message=" + message;
                    CustomAsyncTask loadingTask = new CustomAsyncTask(ctx, new AsyncListner() {
                        JSONObject response = null;

                        public void OnBackgroundTaskCompleted() {
                            try {
                                if (response != null && response.get("message") != null) {
                                    Toast.makeText(getActivity(), (String) response.get("message"), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getActivity(), "Invalid server response", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(getActivity(), "Invalid server response", Toast.LENGTH_SHORT).show();
                            }
                            Intent i = new Intent(getActivity(), ProjectsListActivity.class);
                            startActivity(i);
                        }

                        @Override
                        public void DoBackgroundTask(String[] url_2) {
                            ContactUsModel rawSender = new ContactUsModel();
                            response = rawSender.send(url, param);
                        }

                        @Override
                        public void OnPreExec() {

                        }

                    });
                    loadingTask.execute("");

                    // sendEmail("info@builder.com", "Enquiry for " + pName,
                    // uEmail, uPhone, query);

                }

            }

        });

        // getting data from server

        CustomAsyncTask loadingTask = new CustomAsyncTask(ctx, new AsyncListner() {
            ContactDataVO model;

            @Override
            public void OnBackgroundTaskCompleted() {
                TextView tvAddress = (TextView) rootView.findViewById(R.id.tv_drop_address);
                TextView tvGive = (TextView) rootView.findViewById(R.id.tv_contact_no);
                TextView tvOnLine = (TextView) rootView.findViewById(R.id.tv_add1);
                TextView tvOnLine_one = (TextView) rootView.findViewById(R.id.tv_add2);

                if (contactData != null) {
                    tvAddress.setText(contactData.getAddress());
                    tvGive.setText(contactData.getPhone());
                    tvOnLine.setText("Email: " + contactData.getEmail_one());
                    tvOnLine_one.setText(contactData.getEmail_second());
                }
            }

            @Override
            public void DoBackgroundTask(String[] url) {
                String nurl = UrlFactory.getContactDataUrl();
                String params = "";
                try {
                    String response = ContentLoader.getJsonUsingPost(nurl, params);
                    try {
                        JSONObject jsonObj = new JSONObject(response);
                        contactData = new ContactDataVO(jsonObj.getJSONObject("data"));
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    Log.i(TAG + " loadingTask", response);

                } catch (BMHException e1) {
                    e1.printStackTrace();
                    Log.e(TAG, e1.toString());

                }

            }

            @Override
            public void OnPreExec() {

            }

        });
        loadingTask.dontShowProgressDialog();
        loadingTask.execute("");

        return rootView;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        for (int i = 0; i < menu.size(); i++) {
            menu.getItem(i).setVisible(false);
        }

    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public String getTagText() {
        return TAG;
    }

    @Override
    public boolean onBackPressed() {
        return false;
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

}

package com.activities;

import android.content.Intent;
import android.net.Uri;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.database.entity.CallRecordingEntity;
import com.database.task.CallRecordingTask;
import com.helper.BMHConstants;
import com.sp.R;
import com.utils.Utils;

public class UniversalDialogActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = AlertDialogActivity.class.getSimpleName();
    Button btnCancel, btnSave;
    ImageView img_close;
    TextView tv_mobile;
    EditText edt_remark;
    private String mobileNo;
    private String mRecordingPath, fileName = "";
    private int isRecordingAvailable = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_universal_contact);
        btnCancel = findViewById(R.id.btnCancel);
        img_close = findViewById(R.id.img_close);
        btnSave = findViewById(R.id.btnSave);
        edt_remark = findViewById(R.id.edit_text_remark);
        tv_mobile = findViewById(R.id.tv_customer_mobile);

        btnCancel.setOnClickListener(this);
        img_close.setOnClickListener(this);
        btnSave.setOnClickListener(this);

        Intent intent = getIntent();
        mobileNo = intent.getStringExtra(BMHConstants.MOBILE_NO);
        mRecordingPath = intent.getStringExtra(BMHConstants.RECORDING_FILE_PATH);
        tv_mobile.setText(mobileNo);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btnCancel:
            case R.id.img_close:
                finish();
                break;

            case R.id.btnSave:
                if (mRecordingPath != null) {
                    Uri fileUri = Uri.parse(mRecordingPath);
                    fileName = fileUri.getLastPathSegment();
                    isRecordingAvailable = 1;
                }
                new CallRecordingTask(this, new CallRecordingEntity(fileName, "", mobileNo, 1, isRecordingAvailable,
                        edt_remark.getText().toString(), Utils.getCurrentDateTime())).execute();
                finish();
                break;


            default:
                finish();
                break;
        }

    }
}

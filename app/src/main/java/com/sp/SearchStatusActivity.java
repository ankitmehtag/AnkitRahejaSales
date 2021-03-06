package com.sp;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.adapters.ProjectSearchAdapter;
import com.helper.BMHConstants;
import com.model.ProjectsData;

import java.util.ArrayList;

public class SearchStatusActivity extends AppCompatActivity{

    private EditText et_search;
    private ImageButton ib_back,ib_clear;
    private ArrayList<ProjectsData> projectsDataArrayList;
    private ProjectSearchAdapter mProjectSearchAdapter;
    private ListView lv_search;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.blue_color));
        }
        setContentView(R.layout.activity_search_status);
        initViews();
        setListeners();
        setTypeface();
        if(getIntent() == null || getIntent().getBundleExtra(BMHConstants.BUNDLE) == null)return;
        Bundle mbBundle = getIntent().getBundleExtra(BMHConstants.BUNDLE);
        projectsDataArrayList = (ArrayList<ProjectsData>) mbBundle.getSerializable(BMHConstants.PROJECT_KEY);
        mProjectSearchAdapter = new ProjectSearchAdapter(this,projectsDataArrayList);
        lv_search.setAdapter(mProjectSearchAdapter);
    }

    private void showToast(String msg){
        Toast.makeText(this,msg, Toast.LENGTH_SHORT).show();
    }

    public void initViews() {
        et_search = (EditText)findViewById(R.id.et_search);
        ib_back = (ImageButton)findViewById(R.id.ib_back);
        ib_clear = (ImageButton)findViewById(R.id.ib_clear);
        lv_search = (ListView)findViewById(R.id.lv_search);

    }

    public void setListeners() {
        ib_back.setOnClickListener(mOnClickListener);
        ib_clear.setOnClickListener(mOnClickListener);
        lv_search.setOnItemClickListener(mOnItemClickListener);
        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.toString().isEmpty()){
                    ib_clear.setVisibility(View.GONE);
                }else{
                    ib_clear.setVisibility(View.VISIBLE);
                }
                if(mProjectSearchAdapter != null) {
                    mProjectSearchAdapter.performFiltering(et_search.getText().toString());
                }
            }
        });
    }

    public void setTypeface() {

    }

    public boolean isValidData() {
        return false;
    }

    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.ib_back:
                    finish();
                    break;
                case R.id.ib_clear:
                    et_search.setText("");
                    break;
            }
        }
    };

    AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            ProjectsData mProjectsData = (ProjectsData) view.getTag(R.integer.project_data);
            Intent mIntent = getIntent();
            mIntent.putExtra(BMHConstants.PROJECT_NAME,mProjectsData.getProject_name());
            mIntent.putExtra(BMHConstants.PROJECT_ID,mProjectsData.getProject_id());
            setResult(RESULT_OK,mIntent);
            finish();
        }
    };
}

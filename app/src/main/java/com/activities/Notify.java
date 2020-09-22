package com.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.model.IvrLeadModel;
import com.model.Projects;
import com.sp.R;

import java.util.ArrayList;

public class Notify extends AppCompatActivity {

    private ArrayList<IvrLeadModel> mIvrList;
    private Context mContext;
    IvrLeadModel model;

    public ArrayList<Projects> projectsMasterList;



    public Notify(Context mContext, ArrayList<IvrLeadModel> mIvrList, ArrayList<Projects> projectsMasterList) {
        this.mContext = mContext;
        this.mIvrList = mIvrList;
        this.projectsMasterList = projectsMasterList;
    }
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_transactions);
        Intent test =getIntent();




    }
}

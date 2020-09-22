package com.sp;

import android.graphics.Typeface;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.helper.IntentDataObject;
import com.helper.ParamsConstants;

import java.util.Map;

public class MyTranscationDetailsActivity extends BaseFragmentActivity {

	private final String TAG = MyTranscationDetailsActivity.class.getSimpleName();
	private Toolbar toolbar;
	private LinearLayout ll_root;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_transaction_details);
		app = (BMHApplication) getApplication();
		initViews();
		setListeners();
		setTypeface();
		if(getIntent() != null && getIntent().getSerializableExtra(IntentDataObject.OBJ)!= null
				&& getIntent().getSerializableExtra(IntentDataObject.OBJ) instanceof IntentDataObject ){
			IntentDataObject mIntentDataObject = (IntentDataObject) getIntent().getSerializableExtra(IntentDataObject.OBJ);
			if(mIntentDataObject.getData()!= null){
				LayoutInflater mLayoutInflater = LayoutInflater.from(this);
				toolbar.setTitle(getIntent().getStringExtra(ParamsConstants.TITLE));
				ll_root.removeAllViews();
				for(Map.Entry<String,String> entry : mIntentDataObject.getData().entrySet()){
					View row = mLayoutInflater.inflate(R.layout.transaction_row,null);
					((TextView)row.findViewById(R.id.tv_title)).setText(entry.getKey());
					((TextView)row.findViewById(R.id.tv_value)).setText(entry.getValue());
					ll_root.addView(row);
				}
			}else{
				toolbar.setTitle("Transaction Details");
			}
		}


	}// End of onCreate().

	private void setTypeface() {
		Typeface font = Typeface.createFromAsset(getAssets(), "fonts/regular.ttf");
	}

	private void initViews(){
		toolbar = setToolBar();
		toolbar.setTitle("Transaction Details");
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		ll_root = (LinearLayout)findViewById(R.id.ll_root);
	}
	private void setListeners() {
	}

	@Override
	protected void onResume() {
		super.onResume();
	}


	OnClickListener mOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()){
				case R.id.buttonLogin:
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
	public void onBackPressed() {
		finish();
	}

	@Override
	protected String setActionBarTitle() {
		return "Transaction Details";
	}
}

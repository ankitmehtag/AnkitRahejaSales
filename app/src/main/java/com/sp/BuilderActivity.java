package com.sp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.filter.Builder;
import com.filter.BuilderArrayAdapter;

import java.util.ArrayList;

public class BuilderActivity extends Activity {

	private ListView mainListView = null;
	private BuilderArrayAdapter listAdapter = null;
	private ArrayList<Builder> planetList = new ArrayList<Builder>();
	private EditText inputSearch;
	private Typeface fond;
	private AutoCompleteTextView autoCompleteCity;
	private TextView btnDone,btnClearAll;
	private ImageButton imgbtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_builder);
		fond = Typeface.createFromAsset(getAssets(), "fonts/regular.ttf");
		initViews();
		setListeners();
		setTypeface();
		if(getIntent().getSerializableExtra("selectedBuilders") != null){
			planetList = (ArrayList<Builder>)getIntent().getSerializableExtra("selectedBuilders");
			listAdapter = new BuilderArrayAdapter(BuilderActivity.this, planetList);
			mainListView.setAdapter(listAdapter);
		}
	}

	private void setTypeface() {
		autoCompleteCity.setTypeface(fond);
	}

	private void initViews() {
		autoCompleteCity = (AutoCompleteTextView) findViewById(R.id.inputSearch);
		btnDone = (TextView) findViewById(R.id.builder_done);
		mainListView = (ListView) findViewById(R.id.mainListView);
		imgbtn = (ImageButton) findViewById(R.id.image_btn);
		btnClearAll = (TextView) findViewById(R.id.builder_clear);
	}

	private void setListeners() {
		imgbtn.setOnClickListener(mOnClickListener);
		btnClearAll.setOnClickListener(mOnClickListener);
		btnDone.setOnClickListener(mOnClickListener);

		autoCompleteCity.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
				if(listAdapter != null) listAdapter.getFilter().filter(cs.toString());
			}
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}
			@Override
			public void afterTextChanged(Editable arg0) {}
		});
		autoCompleteCity.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
					actionDone();
				}
				return true;
			}
		});

		mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View item, int position, long id) {
				listAdapter.tempList.get(position).toggleChecked();
				listAdapter.notifyDataSetChanged();
			}

		});
	}

	private void actionDone() {
		Intent intent = new Intent();
		if(listAdapter != null && listAdapter.builderList != null) {
			intent.putExtra("selectedBuilders", listAdapter.builderList);
		}
		setResult(RESULT_OK, intent);
		finish();
	}

	OnClickListener mOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()){
				case R.id.image_btn:
					finish();
					break;
				case R.id.builder_done:
					actionDone();
					break;
				case R.id.builder_clear:
					if(listAdapter != null && listAdapter.builderList != null) {
						for (int i = 0; i < listAdapter.builderList.size(); i++) {
							listAdapter.builderList.get(i).setChecked(false);
						}
						listAdapter.tempList = listAdapter.builderList;
						listAdapter.notifyDataSetChanged();
						autoCompleteCity.setText("");
					}
					break;

			}
		}
	};
}

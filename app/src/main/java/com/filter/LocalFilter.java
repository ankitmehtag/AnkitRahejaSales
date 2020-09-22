package com.filter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.VO.LocationVO;
import com.sp.BaseFragmentActivity;
import com.sp.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class LocalFilter extends BaseFragmentActivity {

	private ListView mainListView = null;
	private Builder[] planets = null;
	private HashMap<String, String> searchMap;
	ArrayList<LocationVO> arrLocation;

	private int clickPos = 0;
	String filer_type = "";
	private int clickPos1 = 1;

	private ArrayAdapter<Builder> listAdapter = null;

	private Activity ctx = LocalFilter.this;
	Typeface fond;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.filter_listview);

		fond = Typeface.createFromAsset(getAssets(), "fonts/regular.ttf");

		Toolbar toolbar = setToolBar();
		toolbar.setTitle(Html.fromHtml("<font color='#ffffff'>" + "Sort Locality" + "</font>"));

		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		searchMap = (HashMap<String, String>) getIntent().getSerializableExtra("searchParams");
		arrLocation = getIntent().getParcelableArrayListExtra("locations");

		// ImageButton imgbtn = (ImageButton) findViewById(R.id.back_button);
		// imgbtn.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		//
		// Intent i = new Intent(LocalFilter.this, LocationMapActivity.class);
		// finish();
		// }
		// });

		// TextView tv1 = (TextView) findViewById(R.id.text_local);
		// tv1.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		//
		// Intent intent = new
		// Intent(LocalFilter.this,LocationMapActivity.class);
		// finish();
		//
		// }
		// });

		// Filter Apply button use method Temporary

		final Button apply = (Button) findViewById(R.id.location_apply);
		apply.setTypeface(fond);

		apply.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.e("listAdapter", filer_type);
				
				Intent intent = getIntent();
				intent.putExtra("Type", "" + clickPos);
				intent.putExtra("filter_type", filer_type);
				setResult(RESULT_OK, intent);
				finish();

			}
		});

		// ============================

		mainListView = (ListView) findViewById(R.id.lv_filter);

		// inputSearch = (EditText) findViewById(R.id.inputSearch);

		// When item is tapped, toggle checked properties of CheckBox and
		// Planet.
		mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View item, int position, long id) {
				Builder builder = listAdapter.getItem(position);
				builder.toggleChecked();
				BuilderViewHolder viewHolder = (BuilderViewHolder) item.getTag();
				viewHolder.getCheckBox().setChecked(builder.isChecked());

				// sendBackResult(position);
			}
			// ----------------------------------------------------------

			// add new feedback resutlt
			// private void sendBackResult(int position) {
			// // TODO Auto-generated method stub
			// Intent returnIntent = new Intent();
			// returnIntent.putExtra("result", position);
			// setResult(RESULT_OK, returnIntent);
			// finish();
			// }
		});

		// Create and populate planets.
//		planets = (Builder[]) getLastNonConfigurationInstance();
		if (planets == null) {
			planets = new Builder[] { new Builder("Price per sq.ft. (low to high)", "psf_ltoh",false),
					new Builder("Price per sq.ft. (high to low)", "psf_htol",false),
					new Builder("Rating (high to low)", "rating",false), new Builder("Infra (high to low)", "infra",false),
					new Builder("Needs (high to low)", "needs",false), new Builder("Lifestyle (high to low)", "lifestyle",false),
					new Builder("Returns (high to low)", "returns",false) };

		}
		ArrayList<Builder> planetList = new ArrayList<Builder>();
		planetList.addAll(Arrays.asList(planets));

		// Set our custom array adapter as the ListView's adapter.
		listAdapter = new BuilderArrayAdapter(this, planetList);
		mainListView.setAdapter(listAdapter);

		mainListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View item, int pos, long arg3) {

				// __________________________________________________________
				Builder planet = listAdapter.getItem(pos);
				planet.toggleChecked();
				BuilderViewHolder viewHolder = (BuilderViewHolder) item.getTag();
				viewHolder.getCheckBox().setChecked(planet.isChecked());

				// __________________________________________________________

				clickPos = pos;
				filer_type = planet.getTag();
				
				// On click list show the filter responce 
				Log.e("listAdapter", filer_type);
				Intent intent = getIntent();
				intent.putExtra("Type", "" + clickPos);
				intent.putExtra("filter_type", filer_type);
				setResult(RESULT_OK, intent);
				finish();
				
				
			}
		});

	}

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
		// TODO Auto-generated method stub
		return null;
	}

	// public Object onRetainNonConfigurationInstance() {
	// return planets;
	//
	// }

}

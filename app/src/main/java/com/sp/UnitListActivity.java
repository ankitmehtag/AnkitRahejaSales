package com.sp;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.VO.UnitCaraouselVO;
import com.adapters.UnitListAdapter;
import com.utils.Connectivity;

import java.util.ArrayList;
import java.util.HashMap;

public class UnitListActivity extends BaseFragmentActivity {

	private UnitListActivity ctx = UnitListActivity.this;
	private ListView list;
	private ArrayList<UnitCaraouselVO> unitCaraouselListVO;
	public HashMap<String, String> searchParams;
	private String FlatTypology;
	ImageView imageViewFav;
	private UnitCaraouselVO unitDetailVO;
	private LinearLayout llFav;
	private final int PAYMENT_REQ = 014;

//	TextView tv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_unit_list);
		FlatTypology = getIntent().getStringExtra("flat_typology");
		unitCaraouselListVO = getIntent().getParcelableArrayListExtra("units");
		searchParams = (HashMap<String, String>) getIntent().getSerializableExtra("searchParams");
		//totalFloors = Integer.parseInt(unitCaraouselListVO.getArrUnits().get(0).getTotal_floor());
		list = (ListView) findViewById(R.id.listProperty);
		list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long arg3) {

				if (unitCaraouselListVO != null) {
					if(unitCaraouselListVO.get(pos).getSold_status().equalsIgnoreCase("sold")){
						showToast(getString(R.string.unit_reserved));
					}else {
						if (Connectivity.isConnectedWithDoalog(ctx)) {
							Intent returnIntent = new Intent(ctx, UnitDetailActivity.class);
							returnIntent.putExtra("unitId", unitCaraouselListVO.get(pos).getUnit_id());
							returnIntent.putExtra("unitTitle", unitCaraouselListVO.get(pos).getUnit_title());
							setResult(RESULT_OK, returnIntent);
//					ctx.finish();
							startActivity(returnIntent);
						}
					}
				}
			}
		});


		//------------------------------------

		Toolbar toolbar = setToolBar();
		toolbar.setTitle("Select Locality");
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		setToolBar();
		supportInvalidateOptionsMenu();
		if (unitCaraouselListVO != null) {
			setPropertyListAdapter();
		}
	}




	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.unit_image_filter, menu);
		menu.getItem(0).setIcon(R.drawable.ic_place_white);
		menu.getItem(1).setVisible(false);
		/*if(menu != null && menu.getItem(0) != null) {
			if (unitCaraouselListVO != null && unitCaraouselListVO.size() <= 1) {
				menu.getItem(0).setVisible(false);
			} else {
				menu.getItem(0).setVisible(true);
			}
		}*/


		return true;
	}

	public void setPropertyListAdapter() {
		UnitListAdapter adapter = new UnitListAdapter(ctx, unitCaraouselListVO);
		list.setAdapter(adapter);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		switch (id) {
			case android.R.id.home:
				onBackPressed();
				break;
			case R.id.action_list:
				onBackPressed();
				//Intent i = new Intent(ctx, UnitListActivity.class);
				//i.putParcelableArrayListExtra("units", basevo.getArrUnits());
				//i.putExtra("searchParams", searchParams);
				//startActivityForResult(i, BMHConstants.FILTER_ACTION);
				//overridePendingTransition(R.anim.push_up_in, R.anim.push_down_in);
				break;
			case R.id.action_unit_filter:
				/*Intent intent = new Intent(ctx, UnitFilter.class);
				intent.putExtra("searchParams", searchParams);
				intent.putExtra("totalFloors", totalFloors);
				startActivityForResult(intent, BMHConstants.FILTER_ACTION);
				overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
				*/break;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	protected String setActionBarTitle() {
		return "Select Unit";
	}


	@Override
	public void onBackPressed() {
		setResult(RESULT_OK);
		finish();
	}
}

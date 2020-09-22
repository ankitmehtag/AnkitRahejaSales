package com.sp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.VO.UnitTypesVO;
import com.adapters.UnitTypeListAdapter;
import com.sp.CustomAsyncTask.AsyncListner;
import com.exception.BMHException;
import com.model.PropertyModel;

import java.util.HashMap;

public class UnitTypeListActivity extends BaseFragmentActivity {

	private Activity ctx = this;
	private String projectId;
	private ListView list;
	private UnitTypesVO vo;
	public HashMap<String, String> map;
	protected int position;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_unit_type_list);
		map = (HashMap<String, String>) getIntent().getSerializableExtra("searchParams");
		projectId = getIntent().getStringExtra("projectId");

		list = (ListView) findViewById(R.id.listViewUnitTypes);
		list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				view.setSelected(true);

				Intent prevI = getIntent();
				Intent i = new Intent(ctx, UnitMapActivity.class);
				i.putExtra("searchParams", map);
				i.putExtra("projectId", projectId);
				i.putExtra("pro_plan_img", prevI.getStringExtra("pro_plan_img"));
				i.putExtra("se", prevI.getStringExtra("se"));
				i.putExtra("nw", prevI.getStringExtra("nw"));
				i.putExtra("unitIds", vo.getArrUnitType().get(position).getUnit_ids());
				i.putExtra("unitType", vo.getArrUnitType().get(position).getTotal_types());

				startActivity(i);

			}
		});

		if (projectId != null && projectId.length() > 0) {
			getUnitTypesTask();
		}
		setToolBar();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.unit_type_list, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void getUnitTypesTask() {
		CustomAsyncTask loadingTask = new CustomAsyncTask(ctx, new AsyncListner() {

			@Override
			public void OnBackgroundTaskCompleted() {
				if (vo == null) {
					showToast(getString(R.string.unable_to_connect_server));
				} else {
					if (vo.isSuccess() && vo.getArrUnitType().size() > 0) {
						UnitTypeListAdapter adapter = new UnitTypeListAdapter(ctx, map, vo.getArrUnitType());
						list.setAdapter(adapter);
					} else {
						showToast(vo.getMessage());
					}
				}

				// =================

				for (int i = 0; i < list.getChildCount(); i++) {
					if (position == i) {
						list.getChildAt(i).setBackgroundColor(Color.BLUE);
					} else {
						list.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);
					}
				}

			}

			@Override
			public void DoBackgroundTask(String[] url) {
				PropertyModel model = new PropertyModel();
				try {
					if (map != null && map.containsKey("type")) {
						vo = model.getUnitTypes(projectId, map.get("type"));
					} else {
						vo = model.getUnitTypes(projectId, "");
					}

				} catch (BMHException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void OnPreExec() {

			}
		});
		loadingTask.execute("");
	}

	@Override
	protected String setActionBarTitle() {
		// TODO Auto-generated method stub
		return "Unit Types";
	}
}

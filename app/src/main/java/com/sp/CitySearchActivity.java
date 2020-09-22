package com.sp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.VO.CitiesVO;
import com.VO.CityVO;
import com.sp.CustomAsyncTask.AsyncListner;
import com.exception.BMHException;
import com.google.gson.Gson;
import com.helper.BMHConstants;
import com.model.LocationModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class CitySearchActivity extends BaseFragmentActivity implements OnClickListener {

	private View rootView;
	private Activity ctx;
	private boolean isFromFilter = false;
	private HashMap<String, String> mapAmenitiesSelected;
	private HashMap<String, String> searchValuesMap;
	private LinearLayout llPropertytype;
	private AutoCompleteTextView autoCompleteCity;
	private CitiesVO citiesVo;
	private CityVO selectedCityVo;
	//final String lastClickedCityName = "lastClickedCityName";
	//final String lastClickedCityId = "lastClickedCityId";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.row_new_cities);

		autoCompleteCity = (AutoCompleteTextView) findViewById(R.id.actv_locality);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

		Intent intent = getIntent();
		if (intent.getExtras() != null) {
			String data = intent.getStringExtra("data");

			Gson gson = new Gson();
			citiesVo = gson.fromJson(data, CitiesVO.class);
			if (citiesVo != null && citiesVo.getArrCity().size() > 0) {

				ArrayList<CityVO> arr = citiesVo.getArrCity();
				String[] arrCities = new String[arr.size()];

				if (!app.getFromPrefs(BMHConstants.CITYID).equals("")) {
					CityVO cityVO = new CityVO(app.getFromPrefs(BMHConstants.CITYNAME), app.getFromPrefs(BMHConstants.CITYID));
					boolean isFound = false;
					for (int i = 0; i < arr.size(); i++) {
						if (app.getFromPrefs(BMHConstants.CITYID).equals(arr.get(i).getCityId())) {
							isFound = true;
							arr.remove(i);
							break;
						}
					}

					if (isFound) {
						arr.add(0, cityVO);
					}
				}

				for (int i = 0; i < arr.size(); i++) {
					arrCities[i] = arr.get(i).getName();
				}
				if(arrCities != null && arrCities.length > 0)
					java.util.Arrays.sort(arrCities, 1, arrCities.length);
				AutocompleteCustomArrayAdapter arrayAdapter = new AutocompleteCustomArrayAdapter(this, R.layout.textview_spinner, arrCities);
				autoCompleteCity.setAdapter(arrayAdapter);

				// ArrayAdapter<String> spinnerArrayAdapter = new
				// ArrayAdapter<String>(this, R.layout.textview_spinner,
				// arrCities);

				// spinnerArrayAdapter.setAdapter(spinnerArrayAdapter);

				autoCompleteCity.setAdapter(arrayAdapter);
				// autoCompleteCity.showDropDown();

				new Handler().postDelayed(new Runnable() {

					@Override
					public void run() {
						autoCompleteCity.showDropDown();
					}
				}, 100);
			}

		}

		autoCompleteCity.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
									long arg3) {
				// TODO Auto-generated method stub
				// Log.e("data", citiesVo.getArrCity().get(arg2).getName());
				String cityName = (String) arg0.getItemAtPosition(arg2);

				String cityId = "";
				for (int i = 0; i < citiesVo.getArrCity().size(); i++) {
					if (citiesVo.getArrCity().get(i).getName().equals(cityName)) {
						cityId = citiesVo.getArrCity().get(i).getCityId();
						break;
					}
				}
				app.saveIntoPrefs(BMHConstants.CITYID, cityId);
				app.saveIntoPrefs(BMHConstants.CITYNAME, cityName);
				Intent intent = new Intent();
				intent.putExtra("city", cityName);
				intent.putExtra("city_id", cityId);


				//====save data in pref====

				// Intent intent = new Intent();
				// intent.putExtra("city",
				// citiesVo.getArrCity().get(arg2).getName());
				// intent.putExtra("city_id",
				// citiesVo.getArrCity().get(arg2).getCityId());

				setResult(RESULT_OK, intent);
				finish();
			}

		});

		ImageView img = (ImageView) findViewById(R.id.imageButton1);
		img.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//Intent i = new Intent(getApplicationContext(),SearchFragment.class);
				finish();

			}
		});


		// ================================================================================
		// AutoCompleteTextView autoC =
		// (AutoCompleteTextView)findViewById(R.id.citySpinner);
		// autoC.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View arg0) {
		// // TODO Auto-generated method stub
		//
		// Intent i = new Intent(getApplicationContext(), SearchFragment.class);
		// finish();
		//
		// }
		// });

	}

	//
	// @SuppressLint("NewApi")
	// @Override
	// public View onCreateView(LayoutInflater inflater, ViewGroup container,
	// Bundle savedInstanceState) {
	// if (rootView == null) {
	// rootView = inflater.inflate(R.layout.row_cities, container, false);
	// ctx = getActivity();
	// app = (BMHApplication) getActivity().getApplication();
	// isFromFilter = getArguments().getBoolean("isFromFilter", false);
	// searchValuesMap = new HashMap<String, String>();
	// // scrollViewRoot = (ScrollView)
	// // rootView.findViewById(R.id.scrollViewroot);
	// mapAmenitiesSelected = new HashMap<String, String>();
	//
	// LinearLayout llPrice = (LinearLayout)
	// rootView.findViewById(R.id.llseekbar);
	// llPropertytype = (LinearLayout)
	// rootView.findViewById(R.id.llpropertytype);
	// autoCompleteCity = (AutoCompleteTextView)
	// rootView.findViewById(R.id.citySpinner);
	// // autoCompleteByBuilder = (AutoCompleteTextView)
	// // rootView.findViewById(R.id.autoCompleteByBuilder);
	//
	// // bhk1 = (Button) rootView.findViewById(R.id.btnBhk1);
	// // bhk2 = (Button) rootView.findViewById(R.id.btnBhk2);
	// // bhk3 = (Button) rootView.findViewById(R.id.btnBhk3);
	// // bhk4 = (Button) rootView.findViewById(R.id.btnBhk4);
	// // bhk5 = (Button) rootView.findViewById(R.id.btnBhk5);
	// // bhk5plus = (Button) rootView.findViewById(R.id.btnBhk5plus);
	//
	// final TextView byCity = (TextView) rootView.findViewById(R.id.byCity);
	// // byBuilder = (Button) rootView.findViewById(R.id.byBuilder);
	// // byProject = (Button) rootView.findViewById(R.id.byProject);
	// // byCity.setSelected(true);
	//
	// byCity.setOnClickListener(new OnClickListener() {
	// @Override
	// public void onClick(View v) {
	// byCity.setSelected(true);
	// // byBuilder.setSelected(false);
	// // byProject.setSelected(false);
	// // autoCompleteByBuilder.setVisibility(View.VISIBLE);
	//
	// if (citiesVo == null) {
	// if (isFromFilter) {
	// setCityAutoCompleteFromPrefs();
	// } else {
	// autoCompleteCity.getText().clear();
	// startCityTask();
	// }
	// }
	//
	// else {
	// setCityAutocompleteAdapter(citiesVo);
	// }
	//
	// autoCompleteCity.setHint("Select City");
	// autoCompleteCity.setCompoundDrawablesWithIntrinsicBounds(R.drawable.map_marker,
	// 0, 0, 0);
	//
	// // autoCompleteCity.setCompoundDrawablesWithIntrinsicBounds(
	// // R.drawable.flat_icon, 0, 0, 0);
	// }
	// });
	//
	// return rootView;
	// }
	// return container;
	//
	// }

	private void setCityAutocompleteAdapter(CitiesVO vo) {
		ArrayList<CityVO> arr = vo.getArrCity();
		String[] arrCities = new String[arr.size()];
		for (int i = 0; i < arr.size(); i++) {
			arrCities[i] = arr.get(i).getName();
			System.out.println("hh city name = " + arr.get(i).getName());
		}

		ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
				ctx, R.layout.textview_spinner, arrCities);
		autoCompleteCity.setAdapter(spinnerArrayAdapter);
		if (!isFromFilter) {
			autoCompleteCity.showDropDown();
		}
	}

	private void startCityTask() {

		CustomAsyncTask loadingTask = new CustomAsyncTask(ctx,
				new AsyncListner() {

					@Override
					public void OnBackgroundTaskCompleted() {
						if (citiesVo == null) {
							// app.showAppMessage(ctx,
							// "Something went wrong, Please Try again.");
							showToast(getString(R.string.unable_to_connect_server));
						} else {
							if (citiesVo.isSuccess()
									&& citiesVo.getArrCity().size() > 0) {
								setCityAutocompleteAdapter(citiesVo);
							} else {
								showToast(citiesVo.getMessage());
							}
						}
					}

					@Override
					public void DoBackgroundTask(String[] url) {
						LocationModel model = new LocationModel();
						try {
							citiesVo = model.getCities(app);
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

	private void setCityAutoCompleteFromPrefs() {

		String json = app.getFromPrefs(BMHConstants.CITY_JSON);
		JSONObject o;
		try {
			o = new JSONObject(json);
			citiesVo = new CitiesVO(o);
			if (citiesVo.isSuccess() && citiesVo.getArrCity().size() > 0) {
				setCityAutocompleteAdapter(citiesVo);

				String selectedId = app.getFromPrefs(BMHConstants.CITYID);
				System.out.println("hh selected id =" + selectedId);
				for (int i = 0; i < citiesVo.getArrCity().size(); i++) {
					String id = citiesVo.getArrCity().get(i).getCityId();
					if (selectedId.equalsIgnoreCase(id)) {
						// autoCompleteCity.setSelection(i);
						autoCompleteCity.setText(citiesVo.getArrCity().get(i)
								.getName());

						// Log.w("", "selectedId"+position);

						// autoCompleteCity.setText(Html.fromHtml(Html.fromHtml((String)
						// citiesVo.getArrCity().get(i).getName()).toString()));

						selectedCityVo = citiesVo.getArrCity().get(i);
						System.out.println("hh pos =" + i);
						break;
					}
				}
				autoCompleteCity.dismissDropDown();

			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub

	}

	// =================

	public class AutocompleteCustomArrayAdapter extends ArrayAdapter<String>
			implements Filterable {

		final String TAG = "AutocompleteCustomArrayAdapter.java";

		Context mContext;
		int layoutResourceId;
		private ArrayList<String> fullList;
		private ArrayList<String> mOriginalValues;
		private LayoutInflater mInflater;

		public AutocompleteCustomArrayAdapter(Context mContext,
											  int layoutResourceId, String[] data) {

			super(mContext, -1);
			mInflater = (LayoutInflater) mContext
					.getSystemService(LAYOUT_INFLATER_SERVICE);

			fullList = new ArrayList<String>(Arrays.asList(data));
			mOriginalValues = new ArrayList<String>(fullList);
			this.layoutResourceId = layoutResourceId;
			this.mContext = mContext;
		}

		@Override
		public int getCount() {
			return fullList.size();
		}

		@Override
		public String getItem(int position) {
			if (fullList.size() == 0) {
				return null;
			} else {
				return fullList.get(position);
			}
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			convertView = mInflater.inflate(R.layout.search_item_suggestions,
					null);

			final TextView title, text, builder;
			title = (TextView) convertView.findViewById(R.id.header_suggestion);
			text = (TextView) convertView.findViewById(R.id.text_suggestion);
			builder = (TextView) convertView
					.findViewById(R.id.project_builder_suggestion);
			try {

				if (position == 0) {
					text.setVisibility(View.GONE);
					title.setVisibility(View.VISIBLE);
				} else {
					title.setVisibility(View.GONE);
					text.setVisibility(View.VISIBLE);
				}
				builder.setVisibility(View.GONE);
				title.setText(Html.fromHtml(fullList.get(position)));
				text.setText(Html.fromHtml(fullList.get(position)));

			} catch (Exception e) {
				e.printStackTrace();
			}
			return convertView;

		}

		@Override
		public Filter getFilter() {
			Filter filter = new Filter() {
				private Object lock = new Object();

				@Override
				protected FilterResults performFiltering(CharSequence prefix) {
					FilterResults results = new FilterResults();

					if (mOriginalValues == null) {
						synchronized (lock) {
							mOriginalValues = new ArrayList<String>(fullList);
						}
					}

					if (prefix == null || prefix.length() == 0) {
						synchronized (lock) {
							ArrayList<String> list = new ArrayList<String>(
									mOriginalValues);
							results.values = list;
							results.count = list.size();
						}
					} else {
						final String prefixString = prefix.toString()
								.toLowerCase();

						ArrayList<String> values = mOriginalValues;
						int count = values.size();

						ArrayList<String> newValues = new ArrayList<String>(
								count);

						for (int i = 0; i < count; i++) {
							String item = values.get(i);
							Log.v("loging", item + "---" + i);
							if (item.toLowerCase().contains(prefixString)) {
								newValues.add(item);
							}

						}

						results.values = newValues;
						results.count = newValues.size();
					}

					return results;
				}

				@SuppressWarnings("unchecked")
				@Override
				protected void publishResults(CharSequence constraint,
											  FilterResults results) {

					if (results.values != null) {
						fullList = (ArrayList<String>) results.values;
					} else {
						fullList = new ArrayList<String>();
					}
					if (results.count > 0) {
						notifyDataSetChanged();
					} else {
						notifyDataSetInvalidated();
					}
				}
			};

			return filter;
		}
	}

	@Override
	protected String setActionBarTitle() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void onBackPressed() {
		//super.onBackPressed();
		finish();
	}
}
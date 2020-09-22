package com.sp;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.VO.PlaceVo;
import com.VO.PlacesBaseVO;
import com.VO.PlacesLocationVO;
import com.adapters.CustomSpinnerAdapter;
import com.adapters.PlacesMapListAdapter;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.sp.CustomAsyncTask.AsyncListner;
import com.exception.BMHException;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.helper.BMHConstants;
import com.model.LocationModel;

import java.util.ArrayList;

public class FullScreenUnitMapActivity extends BaseFragmentActivity {

	private final int DEF_REDIUS_CONS = 4000;
	private final int COTIANT = 9;
	private final int DEF_ZOOM_CONS = 6;
	// private static final double ASSUMED_INIT_LATLNG_DIFF = 1.0;
	// private static final float ACCURACY = 0.01f;
	//
	private Activity ctx = this;
	private GoogleMap map;
	private PlacesBaseVO basevo;;
	private int selectedRadius = 4000;
	private float zoomLevel = 14;
	private String selectedType;
	private int selectedCategory;



	Button landmarks, airport, railways, bus_stands, texi_service, school, hospitals, shopingMalls, department_stores,
			pharmacies, atm, restaurents, banks, parks, hotels, movie_theaters, night_clubs;

	// public static final String PLACE_NIGHT_CLUB = ;
	// public static final String PLACE_RESTAURANT = ;
	// public static final String PLACE_PARK = ;
	// public static final String PLACE_SCHOOL = ;
	// public static final String PLACE_HOSP = ;
	// public static final String PLACE_BANK = ;
	// public static final String PLACE_DEPARTMENT_STORE = ;
	// public static final String PLACE_SHOPPING_MALL = ;
	// public static final String PLACE_PHARMACY = ;
	// public static final String PLACE_THEATER = ;

	String[] arrNeedsQuery = new String[] { "btn_schools", "hospital", "park", "bank", "restaurant", "lodging",
			"movie_theater", "night_club", "grocery_or_supermarket,department_store,home_goods_store", "pharmacy",
			"shopping_mall", "btn_atms" };

	String[] arrNeedsName = new String[] { "School", "Hospital", "Park", "Bank", "Restaurant", "Hotels",
			"Movie Theater", "Night club", "Departmental Stores", "Pharmacy", "Shopping Mall", "Atm" };


	String[] arrTransportQuery = new String[] { "landmark", "btn_airports", "train_station", "bus_station", "taxi_stand",
			"btn_schools", "hospital", "shopping_mall", "grocery_or_supermarket,department_store,home_goods_store",
			"pharmacy", "park", "bank", "btn_atms", "restaurant", "lodging", "movie_theater", "night_club" };

	String[] arrTransportName = new String[] { "btn_landmarks", "Airport", "Train station", "Bus station", "Taxi stand",
			"School", "Hospital", "Shopping Mall", "Departmental Stores", "Pharmacy", "Park", "Bank", "Atm",
			"Restaurant", "Hotels", "Movie Theater", "Night club" };

	// =============================new Update code===========
	public static int[] prgmImages = { R.drawable.schools_gray, R.drawable.hospitals_gray, R.drawable.parks_gray,
			R.drawable.banks_gray, R.drawable.restaurants_gray, R.drawable.hotels_gray, R.drawable.movie_theaters_gray,
			R.drawable.night_clubs_gray, R.drawable.departmental_stores_gray, R.drawable.pharmacies_gray,
			R.drawable.shopping_malls_gray, R.drawable.atms_gray, R.drawable.bus_stand_gray, R.drawable.railways_gray,
			R.drawable.taxi_services_gray, R.drawable.airport_gray, R.drawable.landmarks_gray };

	// =============================



	TextView tvSelectedType , explore_location;
	String selectedName;
	private RelativeLayout rlSpiner;

	// private TextView
	// btn_schools,btn_hospitals,btn_shopping_malls,btn_restaurants,btn_banks,btn_parks,movie,nightClub;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_full_screen_unit_map);

		selectedCategory = getIntent().getIntExtra("type", BMHConstants.NEEDS);

		tvSelectedType = (TextView) findViewById(R.id.tvSelectedType);

		landmarks = (Button) findViewById(R.id.btn_landmarks);
		airport = (Button) findViewById(R.id.btn_airports);
		railways = (Button) findViewById(R.id.btn_railways);
		bus_stands = (Button) findViewById(R.id.btn_busstands);
		texi_service = (Button) findViewById(R.id.btn_texi_service);
		school = (Button) findViewById(R.id.btn_schools);
		hospitals = (Button) findViewById(R.id.btn_hospitals);
		shopingMalls = (Button) findViewById(R.id.btn_shopping_malls);
		department_stores = (Button) findViewById(R.id.btn_dept_stores);
		pharmacies = (Button) findViewById(R.id.btn_pharmacies);
		parks = (Button) findViewById(R.id.btn_parks);
		banks = (Button) findViewById(R.id.btn_banks);
		atm = (Button) findViewById(R.id.btn_atms);
		restaurents = (Button) findViewById(R.id.btn_restaurants);
		hotels = (Button) findViewById(R.id.btn_hotels);
		movie_theaters = (Button) findViewById(R.id.btn_movie_theaters);
		night_clubs = (Button) findViewById(R.id.btn_night_clubs);

		// btn_schools = (TextView) findViewById(R.id.tv_schools);
		// btn_hospitals = (TextView) findViewById(R.id.tv_hospitals);
		// btn_shopping_malls = (TextView) findViewById(R.id.tv_malls);
		// btn_restaurants = (TextView) findViewById(R.id.tv_reataurant);
		// btn_banks = (TextView) findViewById(R.id.tv_banks);
		// btn_parks = (TextView) findViewById(R.id.tv_parks);
		// movie = (TextView) findViewById(R.id.tv_moview);
		// nightClub = (TextView) findViewById(R.id.tv_night_club);

		ArrayList<String> arr2 = new ArrayList<String>();
		arr2.add("1 Km");
		arr2.add("2 Km");
		arr2.add("3 Km");
		arr2.add("4 Km");
		arr2.add("5 Km");
		arr2.add("6 Km");
		arr2.add("7 Km");
		arr2.add("8 Km");
		Spinner spRadius = (Spinner) findViewById(R.id.spinner_radius);
		CustomSpinnerAdapter adapterNoofParkings = new CustomSpinnerAdapter(ctx, R.layout.textview_spinner, arr2);
		spRadius.setAdapter(adapterNoofParkings);

		rlSpiner = (RelativeLayout) findViewById(R.id.rlSpinner);

		// ===========================----------------
		Toolbar toolbar = setToolBar();
		// toolbar.setLogo(R.drawable.arrownav);
		// toolbar.setTitle(" ");

		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		// =============================---------------------


		landmarks.setTag(BMHConstants.PLACE_LANDMARKS);
		airport.setTag(BMHConstants.PLACE_AIRPORT);
		railways.setTag(BMHConstants.PLACE_RAILWAYS);
		bus_stands.setTag(BMHConstants.PLACE_BUS_STAND);
		texi_service.setTag(BMHConstants.PLACE_TEXI_SERVICE);
		// btn_schools.setTag(BMHConstants.PLACE_SCHOOL);
		// btn_hospitals.setTag(BMHConstants.PLACE_HOSP);
		// btn_shopping_malls.setTag(BMHConstants.PLACE_SHOPPING_MALL);
		// btn_restaurants.setTag(BMHConstants.PLACE_RESTAURANT);
		// btn_banks.setTag(BMHConstants.PLACE_BANK);
		// btn_parks.setTag(BMHConstants.PLACE_PARK);
		// movie.setTag(BMHConstants.PLACE_THEATER);
		// nightClub.setTag(BMHConstants.PLACE_NIGHT_CLUB);
		//
		// btn_schools.setOnClickListener(onclick);
		// btn_hospitals.setOnClickListener(onclick);
		// btn_shopping_malls.setOnClickListener(onclick);
		// btn_restaurants.setOnClickListener(onclick);
		// btn_banks.setOnClickListener(onclick);
		// btn_parks.setOnClickListener(onclick);
		// movie.setOnClickListener(onclick);
		// nightClub.setOnClickListener(onclick);

		landmarks.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!v.isSelected()) {
					v.setSelected(true);
				}
				airport.setSelected(false);
				railways.setSelected(false);
				bus_stands.setSelected(false);
				texi_service.setSelected(false);
				school.setSelected(false);
				hospitals.setSelected(false);
				shopingMalls.setSelected(false);
				department_stores.setSelected(false);
				pharmacies.setSelected(false);
				restaurents.setSelected(false);
				banks.setSelected(false);
				parks.setSelected(false);
				atm.setSelected(false);
				hotels.setSelected(false);
				movie_theaters.setSelected(false);
				night_clubs.setSelected(false);

				java.util.ArrayList<PlaceVo> list = getPlaceData(BMHConstants.NEWALL);
				PlaceVo vo = (PlaceVo) list.get(0);

				showPlaces(vo.getQueryString(), vo.getName());

			}
		});

		airport.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!v.isSelected()) {
					v.setSelected(true);
				}
				landmarks.setSelected(false);
				railways.setSelected(false);
				bus_stands.setSelected(false);
				texi_service.setSelected(false);
				school.setSelected(false);
				hospitals.setSelected(false);
				shopingMalls.setSelected(false);
				department_stores.setSelected(false);
				pharmacies.setSelected(false);
				restaurents.setSelected(false);
				banks.setSelected(false);
				parks.setSelected(false);
				atm.setSelected(false);
				hotels.setSelected(false);
				movie_theaters.setSelected(false);
				night_clubs.setSelected(false);

				java.util.ArrayList<PlaceVo> list = getPlaceData(BMHConstants.NEWALL);
				PlaceVo vo = (PlaceVo) list.get(1);

				showPlaces(vo.getQueryString(), vo.getName());

			}
		});

		railways.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!v.isSelected()) {
					v.setSelected(true);
				}
				landmarks.setSelected(false);
				airport.setSelected(false);
				bus_stands.setSelected(false);
				texi_service.setSelected(false);
				school.setSelected(false);
				hospitals.setSelected(false);
				shopingMalls.setSelected(false);
				department_stores.setSelected(false);
				pharmacies.setSelected(false);
				restaurents.setSelected(false);
				banks.setSelected(false);
				parks.setSelected(false);
				atm.setSelected(false);
				hotels.setSelected(false);
				movie_theaters.setSelected(false);
				night_clubs.setSelected(false);

				java.util.ArrayList<PlaceVo> list = getPlaceData(BMHConstants.NEWALL);
				PlaceVo vo = (PlaceVo) list.get(2);

				showPlaces(vo.getQueryString(), vo.getName());
				// map.addCircle(c);
				// map.addMarker(new
				// MarkerOptions().position(lat).title(projname).snippet(projAddress)
				// .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));

				// icon(BitmapDescriptorFactory.fromResource(R.drawable.btn_landmarks));

			}

		});

		bus_stands.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!v.isSelected()) {
					v.setSelected(true);
				}

				landmarks.setSelected(false);
				railways.setSelected(false);
				airport.setSelected(false);
				texi_service.setSelected(false);
				school.setSelected(false);
				hospitals.setSelected(false);
				shopingMalls.setSelected(false);
				department_stores.setSelected(false);
				pharmacies.setSelected(false);
				restaurents.setSelected(false);
				banks.setSelected(false);
				parks.setSelected(false);
				atm.setSelected(false);
				hotels.setSelected(false);
				movie_theaters.setSelected(false);
				night_clubs.setSelected(false);

				java.util.ArrayList<PlaceVo> list = getPlaceData(BMHConstants.NEWALL);
				PlaceVo vo = (PlaceVo) list.get(3);

				showPlaces(vo.getQueryString(), vo.getName());
			}
		});

		texi_service.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!v.isSelected()) {
					v.setSelected(true);
				}

				landmarks.setSelected(false);
				railways.setSelected(false);
				airport.setSelected(false);
				bus_stands.setSelected(false);
				school.setSelected(false);
				hospitals.setSelected(false);
				shopingMalls.setSelected(false);
				department_stores.setSelected(false);
				pharmacies.setSelected(false);
				restaurents.setSelected(false);
				banks.setSelected(false);
				parks.setSelected(false);
				atm.setSelected(false);
				hotels.setSelected(false);
				movie_theaters.setSelected(false);
				night_clubs.setSelected(false);

				java.util.ArrayList<PlaceVo> list = getPlaceData(BMHConstants.NEWALL);
				PlaceVo vo = (PlaceVo) list.get(4);

				showPlaces(vo.getQueryString(), vo.getName());
			}
		});

		school.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!v.isSelected()) {
					v.setSelected(true);
				}

				landmarks.setSelected(false);
				railways.setSelected(false);
				airport.setSelected(false);
				bus_stands.setSelected(false);
				texi_service.setSelected(false);
				hospitals.setSelected(false);
				shopingMalls.setSelected(false);
				department_stores.setSelected(false);
				pharmacies.setSelected(false);
				restaurents.setSelected(false);
				banks.setSelected(false);
				parks.setSelected(false);
				atm.setSelected(false);
				hotels.setSelected(false);
				movie_theaters.setSelected(false);
				night_clubs.setSelected(false);

				java.util.ArrayList<PlaceVo> list = getPlaceData(BMHConstants.NEWALL);
				PlaceVo vo = (PlaceVo) list.get(5);

				showPlaces(vo.getQueryString(), vo.getName());
			}
		});

		hospitals.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!v.isSelected()) {
					v.setSelected(true);
				}

				landmarks.setSelected(false);
				railways.setSelected(false);
				airport.setSelected(false);
				bus_stands.setSelected(false);
				texi_service.setSelected(false);
				school.setSelected(false);
				shopingMalls.setSelected(false);
				department_stores.setSelected(false);
				pharmacies.setSelected(false);
				restaurents.setSelected(false);
				banks.setSelected(false);
				parks.setSelected(false);
				atm.setSelected(false);
				hotels.setSelected(false);
				movie_theaters.setSelected(false);
				night_clubs.setSelected(false);

				java.util.ArrayList<PlaceVo> list = getPlaceData(BMHConstants.NEWALL);
				PlaceVo vo = (PlaceVo) list.get(6);

				showPlaces(vo.getQueryString(), vo.getName());
			}
		});

		shopingMalls.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!v.isSelected()) {
					v.setSelected(true);
				}

				landmarks.setSelected(false);
				railways.setSelected(false);
				airport.setSelected(false);
				bus_stands.setSelected(false);
				texi_service.setSelected(false);
				school.setSelected(false);
				hospitals.setSelected(false);
				department_stores.setSelected(false);
				pharmacies.setSelected(false);
				restaurents.setSelected(false);
				banks.setSelected(false);
				parks.setSelected(false);
				atm.setSelected(false);
				hotels.setSelected(false);
				movie_theaters.setSelected(false);
				night_clubs.setSelected(false);

				java.util.ArrayList<PlaceVo> list = getPlaceData(BMHConstants.NEWALL);
				PlaceVo vo = (PlaceVo) list.get(7);

				showPlaces(vo.getQueryString(), vo.getName());
			}
		});

		department_stores.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!v.isSelected()) {
					v.setSelected(true);
				}

				landmarks.setSelected(false);
				railways.setSelected(false);
				airport.setSelected(false);
				bus_stands.setSelected(false);
				texi_service.setSelected(false);
				school.setSelected(false);
				hospitals.setSelected(false);
				shopingMalls.setSelected(false);
				pharmacies.setSelected(false);
				restaurents.setSelected(false);
				banks.setSelected(false);
				parks.setSelected(false);
				atm.setSelected(false);
				hotels.setSelected(false);
				movie_theaters.setSelected(false);
				night_clubs.setSelected(false);

				java.util.ArrayList<PlaceVo> list = getPlaceData(BMHConstants.NEWALL);
				PlaceVo vo = (PlaceVo) list.get(8);

				showPlaces(vo.getQueryString(), vo.getName());
			}
		});

		pharmacies.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!v.isSelected()) {
					v.setSelected(true);
				}

				landmarks.setSelected(false);
				railways.setSelected(false);
				airport.setSelected(false);
				bus_stands.setSelected(false);
				texi_service.setSelected(false);
				school.setSelected(false);
				hospitals.setSelected(false);
				shopingMalls.setSelected(false);
				department_stores.setSelected(false);
				restaurents.setSelected(false);
				banks.setSelected(false);
				parks.setSelected(false);
				atm.setSelected(false);
				hotels.setSelected(false);
				movie_theaters.setSelected(false);
				night_clubs.setSelected(false);
				java.util.ArrayList<PlaceVo> list = getPlaceData(BMHConstants.NEWALL);
				PlaceVo vo = (PlaceVo) list.get(9);

				showPlaces(vo.getQueryString(), vo.getName());
			}
		});

		restaurents.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!v.isSelected()) {
					v.setSelected(true);
				}

				landmarks.setSelected(false);
				railways.setSelected(false);
				airport.setSelected(false);
				bus_stands.setSelected(false);
				texi_service.setSelected(false);
				school.setSelected(false);
				hospitals.setSelected(false);
				shopingMalls.setSelected(false);
				department_stores.setSelected(false);
				pharmacies.setSelected(false);
				banks.setSelected(false);
				parks.setSelected(false);
				atm.setSelected(false);
				hotels.setSelected(false);
				movie_theaters.setSelected(false);
				night_clubs.setSelected(false);
				java.util.ArrayList<PlaceVo> list = getPlaceData(BMHConstants.NEWALL);
				PlaceVo vo = (PlaceVo) list.get(10);

				showPlaces(vo.getQueryString(), vo.getName());
			}
		});

		banks.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!v.isSelected()) {
					v.setSelected(true);
				}

				landmarks.setSelected(false);
				railways.setSelected(false);
				airport.setSelected(false);
				bus_stands.setSelected(false);
				texi_service.setSelected(false);
				school.setSelected(false);
				hospitals.setSelected(false);
				shopingMalls.setSelected(false);
				department_stores.setSelected(false);
				pharmacies.setSelected(false);
				restaurents.setSelected(false);
				parks.setSelected(false);
				atm.setSelected(false);
				hotels.setSelected(false);
				movie_theaters.setSelected(false);
				night_clubs.setSelected(false);

				java.util.ArrayList<PlaceVo> list = getPlaceData(BMHConstants.NEWALL);
				PlaceVo vo = (PlaceVo) list.get(11);

				showPlaces(vo.getQueryString(), vo.getName());
			}
		});

		parks.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!v.isSelected()) {
					v.setSelected(true);
				}

				landmarks.setSelected(false);
				railways.setSelected(false);
				airport.setSelected(false);
				bus_stands.setSelected(false);
				texi_service.setSelected(false);
				school.setSelected(false);
				hospitals.setSelected(false);
				shopingMalls.setSelected(false);
				department_stores.setSelected(false);
				pharmacies.setSelected(false);
				restaurents.setSelected(false);
				banks.setSelected(false);
				atm.setSelected(false);
				hotels.setSelected(false);
				movie_theaters.setSelected(false);
				night_clubs.setSelected(false);

				java.util.ArrayList<PlaceVo> list = getPlaceData(BMHConstants.NEWALL);
				PlaceVo vo = (PlaceVo) list.get(12);

				showPlaces(vo.getQueryString(), vo.getName());
			}
		});

		atm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!v.isSelected()) {
					v.setSelected(true);
				}

				landmarks.setSelected(false);
				railways.setSelected(false);
				airport.setSelected(false);
				bus_stands.setSelected(false);
				texi_service.setSelected(false);
				school.setSelected(false);
				hospitals.setSelected(false);
				shopingMalls.setSelected(false);
				department_stores.setSelected(false);
				pharmacies.setSelected(false);
				restaurents.setSelected(false);
				banks.setSelected(false);
				parks.setSelected(false);
				hotels.setSelected(false);
				movie_theaters.setSelected(false);
				night_clubs.setSelected(false);

				java.util.ArrayList<PlaceVo> list = getPlaceData(BMHConstants.NEWALL);
				PlaceVo vo = (PlaceVo) list.get(13);

				showPlaces(vo.getQueryString(), vo.getName());
			}
		});

		restaurents.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!v.isSelected()) {
					v.setSelected(true);
				}

				landmarks.setSelected(false);
				railways.setSelected(false);
				airport.setSelected(false);
				bus_stands.setSelected(false);
				texi_service.setSelected(false);
				school.setSelected(false);
				hospitals.setSelected(false);
				shopingMalls.setSelected(false);
				department_stores.setSelected(false);
				pharmacies.setSelected(false);
				atm.setSelected(false);
				banks.setSelected(false);
				parks.setSelected(false);
				hotels.setSelected(false);
				movie_theaters.setSelected(false);
				night_clubs.setSelected(false);

				java.util.ArrayList<PlaceVo> list = getPlaceData(BMHConstants.NEWALL);
				PlaceVo vo = (PlaceVo) list.get(14);

				showPlaces(vo.getQueryString(), vo.getName());
			}
		});

		hotels.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!v.isSelected()) {
					v.setSelected(true);
				}

				landmarks.setSelected(false);
				railways.setSelected(false);
				airport.setSelected(false);
				bus_stands.setSelected(false);
				texi_service.setSelected(false);
				school.setSelected(false);
				hospitals.setSelected(false);
				shopingMalls.setSelected(false);
				department_stores.setSelected(false);
				pharmacies.setSelected(false);
				atm.setSelected(false);
				banks.setSelected(false);
				parks.setSelected(false);
				restaurents.setSelected(false);
				movie_theaters.setSelected(false);
				night_clubs.setSelected(false);

				java.util.ArrayList<PlaceVo> list = getPlaceData(BMHConstants.NEWALL);
				PlaceVo vo = (PlaceVo) list.get(15);

				showPlaces(vo.getQueryString(), vo.getName());
			}
		});

		movie_theaters.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!v.isSelected()) {
					v.setSelected(true);
				}

				landmarks.setSelected(false);
				railways.setSelected(false);
				airport.setSelected(false);
				bus_stands.setSelected(false);
				texi_service.setSelected(false);
				school.setSelected(false);
				hospitals.setSelected(false);
				shopingMalls.setSelected(false);
				department_stores.setSelected(false);
				pharmacies.setSelected(false);
				atm.setSelected(false);
				banks.setSelected(false);
				parks.setSelected(false);
				restaurents.setSelected(false);
				hotels.setSelected(false);
				night_clubs.setSelected(false);

				java.util.ArrayList<PlaceVo> list = getPlaceData(BMHConstants.NEWALL);
				PlaceVo vo = (PlaceVo) list.get(16);

				showPlaces(vo.getQueryString(), vo.getName());
			}
		});

		night_clubs.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!v.isSelected()) {
					v.setSelected(true);
				}

				landmarks.setSelected(false);
				railways.setSelected(false);
				airport.setSelected(false);
				bus_stands.setSelected(false);
				texi_service.setSelected(false);
				school.setSelected(false);
				hospitals.setSelected(false);
				shopingMalls.setSelected(false);
				department_stores.setSelected(false);
				pharmacies.setSelected(false);
				atm.setSelected(false);
				banks.setSelected(false);
				parks.setSelected(false);
				restaurents.setSelected(false);
				hotels.setSelected(false);
				movie_theaters.setSelected(false);

				java.util.ArrayList<PlaceVo> list = getPlaceData(BMHConstants.NEWALL);
				PlaceVo vo = (PlaceVo) list.get(17);

				showPlaces(vo.getQueryString(), vo.getName());
			}
		});


		tvSelectedType.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// String a = (String) v.getTag();
				// if(!selectedType.equalsIgnoreCase(a)){
				showPlaceDialog(selectedCategory);
				// }
			}
		});
		spRadius.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				if (selectedRadius != ((position + 1) * 1000)) {
					selectedRadius = ((position + 1) * 1000);
					if (selectedType != null && selectedType.length() > 0)
						showPlaces(selectedType, selectedName);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});

		spRadius.setSelection(1, true);

		Toolbar t = setToolBar();

		if (selectedCategory == BMHConstants.NEEDS) {
			selectedType = arrNeedsQuery[0];
			selectedName = arrNeedsName[0];
			tvSelectedType.setText(selectedName);
			t.setTitle("Needs");

		} else if (selectedCategory == BMHConstants.TRANSPORT) {
			selectedType = arrTransportQuery[0];
			selectedName = arrTransportName[0];
			tvSelectedType.setText(selectedName);
			t.setTitle("Transport");

		} else if (selectedCategory == BMHConstants.LANDMARK) {
			t.setTitle("Neighbourhoods");
			initMap();

		}else if (selectedCategory == BMHConstants.NEWALL) {
			t.setTitle("Neighbourhoods");
			initMap();
		}

		if (selectedType != null && selectedType.length() > 0) {
			showPlaces(selectedType, selectedName);
		}
	}

	// OnClickListener onclick = new OnClickListener() {
	// @Override
	// public void onClick(View v) {
	// String a = (String) v.getTag();
	// if(!selectedType.equalsIgnoreCase(a)){
	//// selectedType = (String) v.getTag();
	// showPlaces(a);
	// }
	// }
	// };

	private void showPlaces(String type, String name) {
		String latCommaLong = getIntent().getStringExtra("lat_long");
		if (latCommaLong != null && latCommaLong.length() > 0) {
			startPlacesTask(type, name, latCommaLong);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.full_screen_map, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		switch (id) {
			case R.id.action_settings:
				break;
			case android.R.id.home:
				finish();
				break;
		}
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void startPlacesTask(final String type, final String name, final String latLong) {
		CustomAsyncTask loadingTask = new CustomAsyncTask(ctx, new AsyncListner() {
			@Override
			public void OnBackgroundTaskCompleted() {
				if (basevo == null && app != null) {
					// app.showAppMessage(ctx, "Something went wrong, Please Try
					// again.");
					showToast(getString(R.string.unable_to_connect_server));
				} else {
					if (basevo.getStatus().equalsIgnoreCase("OK")&& app != null) {
						ArrayList<PlacesLocationVO> arr = basevo.getArrLocations();
						if (arr != null && arr.size() > 0) {
							// place all the pins on map and draw an circle of
							// radus selected
							selectedType = type;
							selectedName = name;
							tvSelectedType.setText(selectedName + " (" + basevo.getArrLocations().size() + ")");

							zoomLevel = getZoomViaRaius((float) selectedRadius);
							initMap();
						}
					} else if (basevo.getStatus().equalsIgnoreCase("ZERO_RESULTS" )&& app != null) {
						if (selectedRadius != 8000) {
							showToast("Sorry, No results found for " + name + ". Try increasing the radius.");
						} else {
							showToast("Sorry, No results found for " + name);
						}
					} else {
						showToast(getString(R.string.something_went_wrong));
					}
				}
			}

			@Override
			public void DoBackgroundTask(String[] url) {
				LocationModel model = new LocationModel();
				try {
					basevo = model.getNearByPlaces(latLong, type, selectedRadius,
							getResources().getString(R.string.map_api_key));
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
		return "Near By";
	}

	private void initMap() {
		if (map == null) {
			SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapId);
			if (fm != null) {
				fm.getMapAsync(new OnMapReadyCallback() {
					@Override
					public void onMapReady(GoogleMap googleMap) {
						map = googleMap;
						//DO WHATEVER YOU WANT WITH GOOGLEMAP
						map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
						map.setTrafficEnabled(true);
						map.setIndoorEnabled(true);
						map.setBuildingsEnabled(true);
						map.getUiSettings().setZoomControlsEnabled(true);
					}
				});
				setMapAndZoom();
			}
		} else {
			setMapAndZoom();
		}
	}

	private void setMapAndZoom() {
		if (map != null) {
			map.clear();
			map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
			map.getUiSettings().setZoomControlsEnabled(true);
			// map.setPadding(0, 0, 0, 150);
			// getProjectsByLocationId();
			selectedCategory = getIntent().getIntExtra("type", BMHConstants.NEEDS);

			if (selectedCategory == BMHConstants.LANDMARK) {
				ArrayList<PlacesLocationVO> arr = getIntent().getParcelableArrayListExtra("btn_landmarks");
				tvSelectedType.setVisibility(View.GONE);
				rlSpiner.setVisibility(View.GONE);
				if (arr != null) {
					setPropertiesOnMap(arr);
					// zoomLevel = getZoomViaRaius((float)selectedRadius);
				}
			} else {
				if (basevo != null)
					setPropertiesOnMap(basevo.getArrLocations());
			}

		}
	}

	private void setPropertiesOnMap(ArrayList<PlacesLocationVO> arr) {

		// ArrayList<PlacesLocationVO> arr = basevo.getArrLocations();
		for (int i = 0; i < arr.size(); i++) {
			LatLng latlong = new LatLng(arr.get(i).getLat(), arr.get(i).getLon());
			map.addMarker(
					new MarkerOptions().position(latlong).title(arr.get(i).getName()).snippet(arr.get(i).getVicinity())
//							.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
							.icon(BitmapDescriptorFactory.fromResource(getMarker())));

		}

		String latCommaLong = getIntent().getStringExtra("lat_long");
		String[] arrCenter = latCommaLong.split(",");
		if (arrCenter.length > 0) {
			String projname = getIntent().getStringExtra("name");
			String projAddress = getIntent().getStringExtra("address");
			double l1 = Double.parseDouble(arrCenter[0]);
			double l2 = Double.parseDouble(arrCenter[1]);
			LatLng lat = new LatLng(l1, l2);
			map.moveCamera(CameraUpdateFactory.newLatLngZoom(lat, zoomLevel));
			CircleOptions c = new CircleOptions();
			c.center(lat);
			c.radius(selectedRadius);
			c.fillColor(Color.parseColor("#33000000"));
			c.strokeColor(Color.parseColor("#000000"));
			c.strokeWidth(2);
			map.addCircle(c);
			map.addMarker(new MarkerOptions().position(lat).title(projname).snippet(projAddress)
//					.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
					.icon(BitmapDescriptorFactory.fromResource(R.drawable.landmarks_marker)));

		}

	}

	private int getMarker() {
		// TODO Auto-generated method stub
		switch (selectedType) {
			case "landmark":
				return R.drawable.landmarks;

			case "btn_airports":
				return R.drawable.airport_marker;

			case "train_station":
				return R.drawable.railways_marker;

			case "bus_station":
				return R.drawable.bus_stand_marker;

			case "taxi_stand":
				return R.drawable.taxi_services_marker;

			case "btn_schools":
				return R.drawable.school_marker;

			case "hospital":
				return R.drawable.hospital_marker;

			case "shopping_mall":
				return R.drawable.shopping_malls_marker;

			case "grocery_or_supermarket,department_store,home_goods_store":
				return R.drawable.departmental_stores_marker;

			case "pharmacy":
				return R.drawable.pharmacies_marker;

			case "park":
				return R.drawable.park_marker;

			case "bank":
				return R.drawable.bank_marker;

			case "btn_atms":
				return R.drawable.atm_marker;

			case "restaurant":
				return R.drawable.restaurants_marker;

			case "lodging":
				return R.drawable.hotels_marker;
			case "movie_theater":
				return R.drawable.movie_theaters_marker;
			case "night_club":
				return R.drawable.night_clubs_marker;
		}
		return R.drawable.landmarks;


	}

	private float getZoomViaRaius(float radius) {
		radius = radius / DEF_REDIUS_CONS;
		float zoom = COTIANT - (radius);
		zoom = zoom + DEF_ZOOM_CONS;
		return zoom;

	}

	// public LatLngBounds boundsWithCenterAndLatLngDistance(LatLng center,
	// float latDistanceInMeters, float lngDistanceInMeters) {
	// latDistanceInMeters /= 2;
	// lngDistanceInMeters /= 2;
	// LatLngBounds.Builder builder = LatLngBounds.builder();
	// float[] distance = new float[1];
	// {
	// boolean foundMax = false;
	// double foundMinLngDiff = 0;
	// double assumedLngDiff = ASSUMED_INIT_LATLNG_DIFF;
	// do {
	// Location.distanceBetween(center.latitude, center.longitude,
	// center.latitude, center.longitude + assumedLngDiff, distance);
	// float distanceDiff = distance[0] - lngDistanceInMeters;
	// if (distanceDiff < 0) {
	// if (!foundMax) {
	// foundMinLngDiff = assumedLngDiff;
	// assumedLngDiff *= 2;
	// } else {
	// double tmp = assumedLngDiff;
	// assumedLngDiff += (assumedLngDiff - foundMinLngDiff) / 2;
	// foundMinLngDiff = tmp;
	// }
	// } else {
	// assumedLngDiff -= (assumedLngDiff - foundMinLngDiff) / 2;
	// foundMax = true;
	// }
	// } while (Math.abs(distance[0] - lngDistanceInMeters) >
	// lngDistanceInMeters * ACCURACY);
	// LatLng east = new LatLng(center.latitude, center.longitude +
	// assumedLngDiff);
	// builder.include(east);
	// LatLng west = new LatLng(center.latitude, center.longitude -
	// assumedLngDiff);
	// builder.include(west);
	// }
	// {
	// boolean foundMax = false;
	// double foundMinLatDiff = 0;
	// double assumedLatDiffNorth = ASSUMED_INIT_LATLNG_DIFF;
	// do {
	// Location.distanceBetween(center.latitude, center.longitude,
	// center.latitude + assumedLatDiffNorth, center.longitude, distance);
	// float distanceDiff = distance[0] - latDistanceInMeters;
	// if (distanceDiff < 0) {
	// if (!foundMax) {
	// foundMinLatDiff = assumedLatDiffNorth;
	// assumedLatDiffNorth *= 2;
	// } else {
	// double tmp = assumedLatDiffNorth;
	// assumedLatDiffNorth += (assumedLatDiffNorth - foundMinLatDiff) / 2;
	// foundMinLatDiff = tmp;
	// }
	// } else {
	// assumedLatDiffNorth -= (assumedLatDiffNorth - foundMinLatDiff) / 2;
	// foundMax = true;
	// }
	// } while (Math.abs(distance[0] - latDistanceInMeters) >
	// latDistanceInMeters * ACCURACY);
	// LatLng north = new LatLng(center.latitude + assumedLatDiffNorth,
	// center.longitude);
	// builder.include(north);
	// }
	// {
	// boolean foundMax = false;
	// double foundMinLatDiff = 0;
	// double assumedLatDiffSouth = ASSUMED_INIT_LATLNG_DIFF;
	// do {
	// Location.distanceBetween(center.latitude, center.longitude,
	// center.latitude - assumedLatDiffSouth, center.longitude, distance);
	// float distanceDiff = distance[0] - latDistanceInMeters;
	// if (distanceDiff < 0) {
	// if (!foundMax) {
	// foundMinLatDiff = assumedLatDiffSouth;
	// assumedLatDiffSouth *= 2;
	// } else {
	// double tmp = assumedLatDiffSouth;
	// assumedLatDiffSouth += (assumedLatDiffSouth - foundMinLatDiff) / 2;
	// foundMinLatDiff = tmp;
	// }
	// } else {
	// assumedLatDiffSouth -= (assumedLatDiffSouth - foundMinLatDiff) / 2;
	// foundMax = true;
	// }
	// } while (Math.abs(distance[0] - latDistanceInMeters) >
	// latDistanceInMeters * ACCURACY);
	// LatLng south = new LatLng(center.latitude - assumedLatDiffSouth,
	// center.longitude);
	// builder.include(south);
	// }
	// return builder.build();
	// }

	public void showPlaceDialog(int category) {
		LayoutInflater factory = LayoutInflater.from(ctx);
		final View dialogView = factory.inflate(R.layout.dialog_city_list_layout, null);
		final AlertDialog dialog = new AlertDialog.Builder(ctx).create();

		ListView listcity = (ListView) dialogView.findViewById(R.id.lv_locality_list);
		final PlacesMapListAdapter adapter = new PlacesMapListAdapter(ctx, getPlaceData(category));
		listcity.setAdapter(adapter);
		listcity.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				dialog.dismiss();
				PlaceVo vo = (PlaceVo) adapter.getItem(position);
				if (!selectedType.equalsIgnoreCase(vo.getQueryString())) {
					// selectedType = (String) v.getTag();
					showPlaces(vo.getQueryString(), vo.getName());
				}
			}
		});
		dialog.setView(dialogView);

		dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
		dialog.show();
	}

	private ArrayList<PlaceVo> getPlaceData(int category) {
		ArrayList<PlaceVo> arr = null;
		switch (category) {
			case BMHConstants.NEEDS:
				arr = new ArrayList<PlaceVo>();
				for (int i = 0; i < arrNeedsQuery.length; i++) {
					PlaceVo vo = new PlaceVo(arrNeedsName[i], arrNeedsQuery[i]);
					arr.add(vo);
				}
				break;
			case BMHConstants.TRANSPORT:
				arr = new ArrayList<PlaceVo>();
				for (int i = 0; i < arrTransportQuery.length; i++) {
					PlaceVo vo = new PlaceVo(arrTransportName[i], arrTransportQuery[i]);
					arr.add(vo);
				}
				break;
			case BMHConstants.NEWALL:

				arr = new ArrayList<PlaceVo>();
				for (int i = 0; i < arrTransportQuery.length; i++) {

					PlaceVo vo = new PlaceVo(arrTransportName[i], arrTransportQuery[i]);
					arr.add(vo);
				}
				break;



			default:
				break;
		}

		return arr;
	}
}

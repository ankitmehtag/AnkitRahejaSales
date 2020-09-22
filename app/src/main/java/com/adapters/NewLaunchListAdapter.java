package com.adapters;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.AppEnums.APIType;
import com.VO.NewLaunch;
import com.sp.BMHApplication;
import com.sp.ProjectDetailActivity;
import com.sp.R;
import com.sp.UnitDetailActivity;
import com.helper.BMHConstants;
import com.helper.IntentDataObject;
import com.helper.ParamsConstants;
import com.helper.UrlFactory;
import com.jsonparser.JsonParser;
import com.model.BhkUnitSpecification;
import com.squareup.picasso.Picasso;
import com.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class NewLaunchListAdapter extends BaseAdapter {
	private Activity context;
	private LayoutInflater mInflater;
	ArrayList<NewLaunch> arrNewLaunches;
	private BMHApplication app;
	private LayoutParams params;
	Utils utils = new Utils();
	private NewLaunch favpropertyList;
	private	NewLaunchListAdapter adapters;
	private OnClickListener favClick;
	
	private ImageView imageViewFav;
	// ArrayList<PropertyCaraouselVO> arrProperty;
	//List<ImageView> favViews = new ArrayList<ImageView>();
	
	protected Activity ctx;

	public NewLaunchListAdapter(Activity c, ArrayList<NewLaunch> arr,OnClickListener favClick) {
		context = c;
		app = (BMHApplication) c.getApplication();
		mInflater = LayoutInflater.from(c);
		arrNewLaunches = arr;
		ctx = c;
		this.favClick = favClick;
	}

	@Override
	public int getCount() {
		return arrNewLaunches.size();
	}

	@Override
	public Object getItem(int position) {
		return arrNewLaunches.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.fav_new_launch, null);
			holder = new ViewHolder();
			holder.img = (ImageView) convertView.findViewById(R.id.imageViewProj);
			holder.tv1 = (TextView) convertView.findViewById(R.id.tv_proj_list_name);
			holder.tv2 = (TextView) convertView.findViewById(R.id.tv_proj_builderName);
			holder.tv5 = (TextView) convertView.findViewById(R.id.tv_proj_list_price_onwards);
			holder.btnBook = (Button) convertView.findViewById(R.id.btnBookMyHouse);
			holder.address = (TextView) convertView.findViewById(R.id.tv_proj_address);
			holder.project_bhk = (TextView) convertView.findViewById(R.id.tv_proj_Bhks);
			holder.price_value = (TextView) convertView.findViewById(R.id.textViewMinPrice);
			holder.price_psf = (TextView) convertView.findViewById(R.id.tv_psf);
			holder.constraction = (TextView) convertView.findViewById(R.id.tv_status);
			holder.tv_returns = (TextView) convertView.findViewById(R.id.text_return);
			holder.possession = (TextView) convertView.findViewById(R.id.tvPossessionDate);
			holder.imageViewFav = (ImageView) convertView.findViewById(R.id.imageViewFav);
			holder.infra = (TextView) convertView.findViewById(R.id.tv_infra);
			holder.needs = (TextView) convertView.findViewById(R.id.tv_needs);
			holder.lifestyle = (TextView) convertView.findViewById(R.id.tv_lifestyle);
			holder.unitLayout = (LinearLayout) convertView.findViewById(R.id.bhk_unit);
			holder.tv_plc = (TextView) convertView.findViewById(R.id.tv_plc);
			holder.tv_facing = (TextView) convertView.findViewById(R.id.tv_facing);

			//holder.projectUnitsList = (ListView) convertView.findViewById(R.id.listView_bhk_type);
			holder.ll_bhk_type = (LinearLayout)convertView.findViewById(R.id.ll_bhk_type);
			//favViews.add(holder.imageViewFav);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		int width = app.getWidth(context);
		String url = "";
		if (arrNewLaunches.get(position).getPropertyType().equalsIgnoreCase("Unit")) {
			url = arrNewLaunches.get(position).getUnitImage();
			//holder.projectUnitsList.setAdapter(null);
			holder.unitLayout.setVisibility(View.GONE);
		} else {
			url = arrNewLaunches.get(position).getBanner_url();
			holder.unitLayout.setVisibility(View.VISIBLE);
			//JSONArray units = arrNewLaunches.get(position).getUnitSpecification();
			if(arrNewLaunches.get(position).getUnitSpecification() != null) {
				ArrayList<BhkUnitSpecification> units = (ArrayList<BhkUnitSpecification>) JsonParser.convertJsonToBean(APIType.BHK_UNIT_SPECIFICATION, arrNewLaunches.get(position).getUnitSpecification().toString());
				if (units == null) {
					holder.unitLayout.setVisibility(View.GONE);
				} else {
					ArrayList<List<BhkUnitSpecification>> bhkSpecification = Utils.chunks(units, 2);
					holder.ll_bhk_type.removeAllViews();
					holder.ll_bhk_type.setVisibility(View.VISIBLE);
					for (int i = 0; i < bhkSpecification.size(); i++) {
						List<BhkUnitSpecification> spec = bhkSpecification.get(i);
						if (spec != null && spec.size() > 0) {
							LinearLayout mLinearLayout = new LinearLayout(ctx);
							mLinearLayout.setOrientation(LinearLayout.VERTICAL);
							int padding = (int) Utils.dp2px(5, ctx);
							mLinearLayout.setPadding(padding, 0, padding, 0);
							LinearLayout.LayoutParams mLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
							mLinearLayout.setLayoutParams(mLayoutParams);

							for (int j = 0; j < spec.size(); j++) {
								View bhkView = mInflater.inflate(R.layout.row_bhk_type, null);
								TextView bhkType = (TextView) bhkView.findViewById(R.id.bhk_type);
								TextView bhkSize = (TextView) bhkView.findViewById(R.id.bhk_sqft);
								TextView bhkPrice = (TextView) bhkView.findViewById(R.id.bhk_price);
								bhkType.setText(spec.get(j).getWpcf_flat_typology());
								bhkSize.setText(spec.get(j).getArea_range() + " " + spec.get(j).getArea_range_unit());
								bhkPrice.setText(spec.get(j).getPrice_range());
								mLinearLayout.addView(bhkView);
							}
							holder.ll_bhk_type.addView(mLinearLayout);
						}
					}
				}
			}
		}
		
		if (url != null && !url.equalsIgnoreCase("")) {
			Picasso.with(context.getApplicationContext()).load(UrlFactory.getShortImageByWidthUrl(width, url))
		.placeholder(BMHConstants.PLACE_HOLDER).error(BMHConstants.NO_IMAGE).into(holder.img);

		}

		if (arrNewLaunches.get(position).isUser_favourite()) {
			holder.imageViewFav.setImageResource(R.drawable.favorite_filled);
		} else {
			holder.imageViewFav.setImageResource(R.drawable.favorite_outline);
		}
		holder.imageViewFav.setTag(R.integer.project_item,arrNewLaunches.get(position));
		holder.imageViewFav.setOnClickListener(favClick);
		/*holder.imageViewFav.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(context, "Fav click", Toast.LENGTH_SHORT).show();
			}
		});*/

		//---------------- UnitList
		
//		adapters = new NewLaunchListAdapter( arrNewLaunches.get(position).getUnitSpecification());
//		projectUnitsList.setAdapter(adapters);
		
		try{
		if (arrNewLaunches.get(position).getUnitSpecification().length() > 2) {
			params = new LayoutParams(LayoutParams.MATCH_PARENT, utils.UnitDP(160, this));
		} else {
			params = new LayoutParams(LayoutParams.MATCH_PARENT,
			utils.UnitDP(arrNewLaunches.get(position).getUnitSpecification().length() * 80, this));
		}
		}catch(Exception e){
			
		}

		/*if (arrNewLaunches.get(position).isUser_favourite()) {
			
			holder.imageViewFav.setImageResource(R.drawable.favorite_filled);
		} else {
			holder.imageViewFav.setImageResource(R.drawable.favorite_outline);
		}
*/
		holder.tv1.setText(
		Html.fromHtml(Html.fromHtml((String) arrNewLaunches.get(position).getDisplayName()).toString()));
		//holder.tv2.setText(Html.fromHtml(Html.fromHtml((String) arrNewLaunches.get(position).getBuilder_name()).toString()));
		holder.tv2.setText(Html.fromHtml(Html.fromHtml((String) arrNewLaunches.get(position).getProject_name()).toString()));

		holder.tv5.setText(context.getResources().getString(R.string.Rs) + " "
		+ app.getDecimalFormatedPrice(arrNewLaunches.get(position).getPrice()) + " onwards");

		holder.address.setText(arrNewLaunches.get(position).getExactlocation());
		holder.infra.setText("Infra" + "\n" + arrNewLaunches.get(position).getInfra());
		holder.needs.setText("Needs" + "\n" + arrNewLaunches.get(position).getNeeds());
		holder.lifestyle.setText("Lifestyle" + "\n" + arrNewLaunches.get(position).getLife_style());
		holder.constraction.setText(arrNewLaunches.get(position).getUnder_construction());
		holder.tv_returns.setText(arrNewLaunches.get(position).getPrice_trends());
		holder.possession.setText(arrNewLaunches.get(position).getPossession_date());
		holder.project_bhk.setText(arrNewLaunches.get(position).getShow_text());
		holder.price_value.setText(arrNewLaunches.get(position).getPriceValue());
		if(arrNewLaunches.get(position).getWpcf_flat_price_plc() != null && !arrNewLaunches.get(position).getWpcf_flat_price_plc().equalsIgnoreCase("NA")
				&& !arrNewLaunches.get(position).getWpcf_flat_price_plc().isEmpty()) {
			holder.tv_plc.setText("PLC: "+arrNewLaunches.get(position).getWpcf_flat_price_plc());
			holder.tv_plc.setVisibility(View.VISIBLE);
		}else{
			holder.tv_plc.setVisibility(View.GONE);
		}
		if(arrNewLaunches.get(position).getDirection_facing() != null && !arrNewLaunches.get(position).getDirection_facing().equalsIgnoreCase("NA")
				&& !arrNewLaunches.get(position).getDirection_facing().isEmpty()) {
			holder.tv_facing.setText("Direction:"+arrNewLaunches.get(position).getDirection_facing());
			holder.tv_facing.setVisibility(View.VISIBLE);
		}else{
			holder.tv_facing.setVisibility(View.GONE);
		}
		String priceTxt = arrNewLaunches.get(position).getProjectSqft() + " " + arrNewLaunches.get(position).getProp_price_persq_unit();
		float price = Utils.toFloat(arrNewLaunches.get(position).getProjectSqft());
		if(price > 0) priceTxt = Utils.priceFormat(price)  + " " + arrNewLaunches.get(position).getProp_price_persq_unit();
		holder.price_psf.setText(priceTxt);

		// ====================== Hide and visible type

		if (arrNewLaunches.get(position).getUnitSpecification() != null && arrNewLaunches.get(position).getUnitSpecification().length() > 0) {
			holder.ll_bhk_type.setVisibility(View.VISIBLE);
		} else {
			holder.ll_bhk_type.setVisibility(View.GONE);
		}
		convertView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (arrNewLaunches.get(position).getPropertyType().equalsIgnoreCase("unit")) {
					Intent i = new Intent(context, UnitDetailActivity.class);
					i.putExtra("unitId", arrNewLaunches.get(position).getUnitId());
					i.putExtra("unitTitle", arrNewLaunches.get(position).getShow_text());
					context.startActivity(i);
				} else {
					Intent i = new Intent(context, ProjectDetailActivity.class);
					i.putExtra("propertyId", arrNewLaunches.get(position).getID());
					context.startActivity(i);
					IntentDataObject mIntentDataObject = new IntentDataObject();
					mIntentDataObject.putData(ParamsConstants.ID, arrNewLaunches.get(position).getID());
					mIntentDataObject.putData(ParamsConstants.TYPE,ParamsConstants.BUY);
					Intent mIntent = new Intent(context, ProjectDetailActivity.class);
					mIntent.putExtra(IntentDataObject.OBJ, mIntentDataObject);
					context.startActivity(mIntent);
				}
			}
		});

		return convertView;

	}

	private Activity getApplicationContext() {
		// TODO Auto-generated method stub
		return null;
	}

	class ViewHolder {
		ImageView img;
		TextView tv1;
		TextView tv2;
		TextView tv3;
		TextView tv4;
		TextView tv5;
		Button btnBook;

		TextView infra;
		TextView needs;
		TextView lifestyle;

		TextView price_value;
		TextView possession;
		TextView project_bhk;
		TextView price_psf;

		TextView bhk_price;
		TextView bhk_price_1;
		TextView bhk_price_2;
		TextView bhk_price_3;
		TextView bhk_price_4;

		TextView constraction;
		TextView address;
		//ListView projectUnitsList;

		LinearLayout ll_bhk_type;
		TextView tv_returns;
		LinearLayout unitLayout;
		ImageView imageViewFav;

		TextView tv_plc,tv_facing;
		
	}

	public void toggleFav(String propertyId) {
		if (propertyId == null || propertyId.length() == 0) {
			return;
		}
		for (int i = 0; i < arrNewLaunches.size(); i++) {
			NewLaunch property = arrNewLaunches.get(i);
			if (property.getID().equals(propertyId)) {
				boolean fav = arrNewLaunches.get(i).isUser_favourite() ? false:true;
				arrNewLaunches.get(i).setUser_favourite(fav);
				notifyDataSetChanged();
			}
		}
	}
	private int getListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null)
			return 0;

		int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
		int totalHeight = 0;
		View view = null;

		for (int i = 0; i < listAdapter.getCount(); i++) {
			view = listAdapter.getView(i, view, listView);
			if (i == 0)
				view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, LayoutParams.WRAP_CONTENT));

			view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
			totalHeight += view.getMeasuredHeight();
		}
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight + listView.getDividerHeight() * (listAdapter.getCount() - 1);
		listView.setLayoutParams(params);
		listView.requestLayout();
		return totalHeight;
	}

}

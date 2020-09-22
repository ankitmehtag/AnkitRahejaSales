package com.adapters;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Context;
import androidx.viewpager.widget.ViewPager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.VO.PropertyCaraouselVO;
import com.sp.BMHApplication;
import com.sp.R;
import com.helper.BMHConstants;
import com.helper.UrlFactory;
import com.squareup.picasso.Picasso;
import com.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class BrokerProjectListAdapter extends BaseAdapter {
	private Context context;
	private LayoutInflater mInflater;
	public ArrayList<PropertyCaraouselVO> arrProperty;
	private BMHApplication app;
	ViewHolder holder;
	List<ImageView> favViews = new ArrayList<ImageView>();

	private HorizontalScrollView hsv;
	private Button btnPrevoius, btnNext;
	private LinearLayout llFav;
	private ImageView imageViewFav;
	private PropertyCaraouselVO propertyList;
	private Activity ctx;
	private OnClickListener favClick,listItemClick;
	public ArrayList<PropertyCaraouselVO> filterList = new ArrayList<>();

	public BrokerProjectListAdapter(Activity c, OnClickListener favClick, OnClickListener listItemClick, ArrayList<PropertyCaraouselVO> arr) {
		app = (BMHApplication) c.getApplication();
		context = c;
		mInflater = LayoutInflater.from(context);
		arrProperty = arr;
		if(arr != null) filterList.addAll(arr);
		ctx = c;
		this.favClick = favClick;
		this.listItemClick = listItemClick;
	}

	@Override
	public int getCount() {
		if(arrProperty != null)
			return arrProperty.size();
		else return 0;
	}

	@Override
	public Object getItem(int position) {
		if(arrProperty != null)
			return arrProperty.get(position);
		else return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public void updateListData(ArrayList<PropertyCaraouselVO> arrProperty) {
		this.arrProperty = arrProperty;
		notifyDataSetChanged();
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;

		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.broker_list_row, null);
			holder = new ViewHolder();
			holder.tv_name = (TextView) convertView.findViewById(R.id.tv_proj_list_name);
			holder.tv_price = (TextView) convertView.findViewById(R.id.tv_proj_list_price_onwards);
			holder.tv_address = (TextView) convertView.findViewById(R.id.tv_proj_address);
			holder.tv_Bhks = (TextView) convertView.findViewById(R.id.tv_proj_Bhks);
			holder.tv_proj_builderName = (TextView) convertView.findViewById(R.id.tv_proj_builderName);
			holder.propImage = (ImageView) convertView.findViewById(R.id.imageViewProj);
			holder.infra = (TextView) convertView.findViewById(R.id.tv_infra);
			holder.needs = (TextView) convertView.findViewById(R.id.tv_needs);
			holder.lifestyle = (TextView) convertView.findViewById(R.id.tv_lifestyle);
			holder.constrcution = (TextView) convertView.findViewById(R.id.tv_status);
			holder.tv_psf = (TextView) convertView.findViewById(R.id.tv_psf);
			holder.tv_rating = (TextView) convertView.findViewById(R.id.tv_rating);
			holder.tv_returns = (TextView) convertView.findViewById(R.id.text_return);
			holder.bhk_unit = (LinearLayout) convertView.findViewById(R.id.bhk_unit);
			holder.possession = (TextView) convertView.findViewById(R.id.tvPossessionDate);
			holder.tv_priceRange = (TextView) convertView.findViewById(R.id.textViewMinPrice);
			holder.projectUnitsList = (ListView) convertView.findViewById(R.id.listView_bhk_type);
			holder.ll_bhk_type = (LinearLayout)convertView.findViewById(R.id.ll_bhk_type);
			holder.tv_broker = (TextView)convertView.findViewById(R.id.tv_broker);
			//holder.vp_bhk_type  = new ViewPager (ctx);
			//holder.vp_bhk_type = (ViewPager) convertView.findViewById(R.id.vp_bhk_type);
			//holder.vp_bhk_type.setId(R. id.view_pager);
			//holder.bhk_unit.addView(holder.vp_bhk_type);
			holder.imageViewFav = (ImageView) convertView.findViewById(R.id.imageViewFav);

			favViews.add(holder.imageViewFav);

			hsv = (HorizontalScrollView) convertView.findViewById(R.id.scroll_view);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		PropertyCaraouselVO item = arrProperty.get(position);
		//JSONArray units = item.getUnitSpecification();
		/*ArrayList<BhkUnitSpecification> units = (ArrayList<BhkUnitSpecification>) JsonParser.convertJsonToBean(APIType.BHK_UNIT_SPECIFICATION,item.getUnitSpecification().toString());
		if (units == null) {
			holder.bhk_unit.setVisibility(View.GONE);
		} else {
			holder.bhk_unit.setVisibility(View.VISIBLE);
			ArrayList<List<BhkUnitSpecification>> bhkSpecification = Utils.chunks(units,2);
			*//*holder.projectUnitsList.setAdapter(new FavProjectUnitsAdapter(context, units));
			int height = getListViewHeightBasedOnChildren(holder.projectUnitsList);
			ViewGroup.LayoutParams params = holder.projectUnitsList.getLayoutParams();
			params.height = height;
			params.width = LayoutParams.MATCH_PARENT;
			holder.projectUnitsList.setLayoutParams(params);*//*

			//getListViewHeightBasedOnChildren(holder.projectUnitsList);
			holder.ll_bhk_type.removeAllViews();
			holder.ll_bhk_type.setVisibility(View.VISIBLE);
			for (int i = 0; i < bhkSpecification.size(); i++) {
				List<BhkUnitSpecification> spec = bhkSpecification.get(i);
				if(spec != null && spec.size() > 0) {
					LinearLayout mLinearLayout = new LinearLayout(ctx);
					mLinearLayout.setOrientation(LinearLayout.VERTICAL);
					int padding = (int) Utils.dp2px(5, ctx);
					mLinearLayout.setPadding(padding, 0, padding, 0);
					int width = app.getWidth(ctx);
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
			//holder.vp_bhk_type.setClipToPadding(false);
			//holder.vp_bhk_type.setPadding(50, 30, 50, 30);
			//holder.vp_bhk_type.setPageMargin(20);
			//BhkTypeAdapter adapter = new BhkTypeAdapter(((FragmentActivity)ctx).getSupportFragmentManager(),bhkSpecification);
			//holder.vp_bhk_type.setAdapter(adapter);
			//holder.vp_bhk_type.storeAdapter(adapter);
			//adapter.notifyDataSetChanged();


		}*/
		holder.tv_name.setText(Html.fromHtml(Html.fromHtml((String) item.getDisplay_name()).toString()));
		holder.tv_address.setText(item.getExactlocation());
		holder.tv_Bhks.setText(item.getUnit_type());
		holder.tv_proj_builderName.setText(item.getBuilder_name());
		holder.infra.setText(item.getInfra());
		holder.needs.setText(item.getNeeds());
		holder.lifestyle.setText(item.getLife_style());
		holder.constrcution.setText("(" + item.getUnder_construction() + ")");

		holder.tv_priceRange.setText(item.getProject_price_range());
		holder.possession.setText(item.getPossession_date());
		String priceTxt = item.getPsf() + " " + item.getPsf_unit();
		float price = Utils.toFloat(item.getPsf());
		if(price > 0) priceTxt = Utils.priceFormat(price) + " " + item.getPsf_unit();
		holder.tv_psf.setText(priceTxt);
		holder.tv_returns.setText(item.getPrice_trends());
		if(item.getRatings_average() != null && !item.getRatings_average().isEmpty())
			holder.tv_rating.setText(item.getRatings_average() + "/5");
		else
			holder.tv_rating.setText("0/5");
		if (item.isUser_favourite()) {
			holder.imageViewFav.setImageResource(R.drawable.favorite_filled);
		} else {
			holder.imageViewFav.setImageResource(R.drawable.favorite_outline);
		}
		holder.imageViewFav.setTag(R.integer.project_item,item);
		holder.imageViewFav.setOnClickListener(favClick);
		int width = app.getWidth(ctx);
		String url = item.getBanner_img();
		if (url != null && !url.isEmpty()) {
			Picasso.with(context.getApplicationContext()).load(UrlFactory.getShortImageByWidthUrl(width, url))
					.placeholder(BMHConstants.PLACE_HOLDER).error(BMHConstants.PLACE_HOLDER).into(holder.propImage);
		}

		/*if(position == 0){
			holder.tv_broker.setText("Brokerage 4000K ");

		}else if(position == 1){
			holder.tv_broker.setText(context.getString(R.string.unit_reserve_request)+context.getString(R.string.unit_reserve_request));

		}else{
			holder.tv_broker.setText(context.getString(R.string.unit_reserve_request)+context.getString(R.string.unit_reserve_request)+context.getString(R.string.unit_reserve_request));

		}*/
		if(item.getBrokerage_term() == null || item.getBrokerage_term().isEmpty()){
			holder.tv_broker.setText("Commission: N/A");
		}else{
			holder.tv_broker.setText("Commission: "+Html.fromHtml(item.getBrokerage_term()));
		}
		convertView.setTag(R.integer.project_item,item);
		convertView.setOnClickListener(listItemClick);
		return convertView;

	}

	public void toggleFav(String propertyId) {
		if (propertyId == null || propertyId.length() == 0) {
			return;
		}
		for (int i = 0; i < arrProperty.size(); i++) {
			PropertyCaraouselVO property = arrProperty.get(i);
			if (property.getId().equals(propertyId)) {
				boolean fav = arrProperty.get(i).isUser_favourite() ? false:true;
				arrProperty.get(i).setUser_favourite(fav);
				notifyDataSetChanged();
			}
		}
	}

	class ViewHolder {

		public Object btnNext;
		public Object btnPrevoius;
		TextView tv_name;
		TextView tv_price;
		TextView tv_address;
		TextView tv_Bhks;
		TextView tv_proj_builderName;
		ImageView propImage;
		TextView infra;
		TextView needs;
		TextView lifestyle;
		TextView constrcution;
		TextView bhk_1;
		TextView bhk_2;
		TextView bhk_3;
		TextView bhk_4;
		TextView bhk_5;
		TextView bhk_sqft;
		TextView bhk_sqft_1;
		TextView bhk_sqft_2;
		TextView bhk_sqft_3;
		TextView bhk_sqft_4;
		TextView bhk_price;
		TextView bhk_price_1;
		TextView bhk_price_2;
		TextView bhk_price_3;
		TextView bhk_price_4;
		TextView tv_psf;
		TextView possession;
		TextView tv_priceRange;
		LinearLayout bhk_one;
		LinearLayout bhk_two;
		LinearLayout bhk_three;
		LinearLayout bhk_four;
		LinearLayout bhk_five;
		ListView projectUnitsList;
		TextView tv_returns;
		ImageView imageViewFav;
		TextView tv_rating;
		LinearLayout bhk_unit;
		LinearLayout ll_bhk_type;
		ViewPager vp_bhk_type;
		TextView tv_broker;

	}

	private int getListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null)
			return 0;

		int desiredWidth = MeasureSpec.makeMeasureSpec(listView.getWidth(), MeasureSpec.UNSPECIFIED);
		int totalHeight = 0;
		View view = null;

		for (int i = 0; i < listAdapter.getCount(); i++) {
			view = listAdapter.getView(i, view, listView);
			if (i == 0)
				view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, LayoutParams.WRAP_CONTENT));

			view.measure(desiredWidth, MeasureSpec.UNSPECIFIED);
			totalHeight += view.getMeasuredHeight();
		}
		/*ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight + listView.getDividerHeight() * (listAdapter.getCount() - 1);
		listView.setLayoutParams(params);
		listView.requestLayout();*/
		totalHeight = totalHeight + listView.getDividerHeight() * (listAdapter.getCount() - 1);
		return totalHeight;
	}


	public void performFiltering(CharSequence constraint) {
		arrProperty.clear();
		if(filterList != null){
			if (constraint.length() == 0) {
				arrProperty.addAll(filterList);
			} else {
				final String filterPattern = constraint.toString().toLowerCase().trim();
				for (final PropertyCaraouselVO data : filterList) {
					String rating = "";
					if(data.getRatings_average() != null && !data.getRatings_average().isEmpty()) rating = data.getRatings_average() + "/5";
					else rating = "0/5";

					if (data.getDisplay_name() != null && data.getDisplay_name().toLowerCase().contains(filterPattern)
							|| data.getDisplay_name() != null && data.getDisplay_name().toLowerCase().contains(filterPattern)
							|| data.getExactlocation() != null && data.getExactlocation().toLowerCase().contains(filterPattern)
							|| data.getBuilder_name() != null && data.getBuilder_name().toLowerCase().contains(filterPattern)
							|| data.getUnder_construction() != null && data.getUnder_construction().toLowerCase().contains(filterPattern)
							|| data.getPossession_date() != null && data.getPossession_date().toLowerCase().contains(filterPattern)
							|| rating.toLowerCase().contains(filterPattern)
							|| data.getPrice_trends() != null && data.getPrice_trends().toLowerCase().contains(filterPattern)
							|| data.getBrokerage_term() != null && data.getBrokerage_term().toLowerCase().contains(filterPattern)
							) {
						arrProperty.add(data);
					}
				}
			}
			notifyDataSetChanged();
		}

	}

}

package com.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.AppEnums.APIType;
import com.VO.PropertyCaraouselVO;
import com.bumptech.glide.Glide;
import com.helper.IntentDataObject;
import com.helper.ParamsConstants;
import com.sp.BMHApplication;
import com.sp.ConnectivityReceiver;
import com.sp.LoginActivity;
import com.sp.ProjectDetailActivity;
import com.sp.ProjectsListActivity;
import com.sp.R;
import com.helper.BMHConstants;
import com.helper.UrlFactory;
import com.jsonparser.JsonParser;
import com.model.BhkUnitSpecification;
import com.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class PropertyListAdapter extends RecyclerView.Adapter<PropertyListAdapter.ViewHolder> {

    private Context context;
    private LayoutInflater mInflater;
    public ArrayList<PropertyCaraouselVO> arrProperty;
    private BMHApplication app;
    ViewHolder holder;
    private HorizontalScrollView hsv;
    private Activity ctx;
    PropertyCaraouselVO propertyModel;
    public ArrayList<PropertyCaraouselVO> filterList = new ArrayList<>();

    public PropertyListAdapter(Activity c, ArrayList<PropertyCaraouselVO> arr) {
        app = BMHApplication.getInstance();
        context = c;
        ctx = c;
        mInflater = LayoutInflater.from(context);
        arrProperty = arr;
        if (arr != null) {
            filterList.clear();
            filterList.addAll(arr);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_new_launch, parent, false);
        return new ViewHolder(v);
    }

    public void updateListData(ArrayList<PropertyCaraouselVO> arrProperty) {
        this.arrProperty = arrProperty;
        if (filterList != null) {
            filterList.clear();
            if (arrProperty != null)
                filterList.addAll(arrProperty);
        }
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (getItemCount() <= 0)
            return;
        propertyModel = arrProperty.get(position);
        ArrayList<BhkUnitSpecification> units = (ArrayList<BhkUnitSpecification>) JsonParser.convertJsonToBean(APIType.BHK_UNIT_SPECIFICATION, propertyModel.getUnitSpecification().toString());
        if (units == null) {
            holder.bhk_unit.setVisibility(View.GONE);
        } else {
            holder.bhk_unit.setVisibility(View.VISIBLE);
            ArrayList<List<BhkUnitSpecification>> bhkSpecification = Utils.chunks(units, 2);
            holder.ll_bhk_type.removeAllViews();
            for (int i = 0; i < bhkSpecification.size(); i++) {
                List<BhkUnitSpecification> spec = bhkSpecification.get(i);
                if (spec != null && spec.size() > 0) {
                    LinearLayout mLinearLayout = new LinearLayout(ctx);
                    mLinearLayout.setOrientation(LinearLayout.VERTICAL);
                    int padding = (int) Utils.dp2px(5, ctx);
                    mLinearLayout.setPadding(padding, 0, padding, 0);
                    int width = app.getWidth(ctx);
                    LinearLayout.LayoutParams mLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    mLinearLayout.setLayoutParams(mLayoutParams);

                    for (int j = 0; j < spec.size(); j++) {
                        View bhkView = mInflater.inflate(R.layout.row_bhk_type, null);
                        TextView bhkType = bhkView.findViewById(R.id.bhk_type);
                        TextView bhkSize = bhkView.findViewById(R.id.bhk_sqft);
                        TextView bhkPrice = bhkView.findViewById(R.id.bhk_price);
                        bhkType.setText(spec.get(j).getWpcf_flat_typology());
                        bhkSize.setText(spec.get(j).getArea_range() + " " + spec.get(j).getArea_range_unit());
                        bhkPrice.setText(spec.get(j).getPrice_range());
                        mLinearLayout.addView(bhkView);
                    }
                    holder.ll_bhk_type.addView(mLinearLayout);
                }
            }
            if (bhkSpecification.size() > 0) {
                holder.ll_bhk_type.setVisibility(View.VISIBLE);
            } else {
                holder.ll_bhk_type.setVisibility(View.GONE);
            }
        }
        holder.tv_name.setText(Html.fromHtml(Html.fromHtml((String) propertyModel.getDisplay_name()).toString()));
        holder.tv_address.setText(propertyModel.getExactlocation());
        holder.tv_Bhks.setText(propertyModel.getUnit_type());
        holder.tv_proj_builderName.setText(propertyModel.getBuilder_name());
        holder.infra.setText(propertyModel.getInfra());
        holder.needs.setText(propertyModel.getNeeds());
        holder.lifestyle.setText(propertyModel.getLife_style());
        holder.construction.setText("(" + propertyModel.getUnder_construction() + ")");

        holder.tv_priceRange.setText(propertyModel.getProject_price_range());
        holder.possession.setText(propertyModel.getPossession_date());
        String priceTxt = propertyModel.getPsf() + " " + propertyModel.getPsf_unit();
        float price = Utils.toFloat(propertyModel.getPsf());
        if (price > 0) priceTxt = Utils.priceFormat(price) + " " + propertyModel.getPsf_unit();
        holder.tv_psf.setText(priceTxt);
        holder.tv_returns.setText(propertyModel.getPrice_trends());
        if (propertyModel.getRatings_average() != null && !propertyModel.getRatings_average().isEmpty())
            holder.tv_rating.setText(propertyModel.getRatings_average() + "/5");
        else
            holder.tv_rating.setText("0/5");
        if (propertyModel.isUser_favourite()) {
            holder.imageViewFav.setImageResource(R.drawable.favorite_filled);
        } else {
            holder.imageViewFav.setImageResource(R.drawable.favorite_outline);
        }
        holder.imageViewFav.setTag(R.integer.project_item, propertyModel);

        int width = app.getWidth(ctx);
        String url = propertyModel.getBanner_img();
        if (url != null && !url.isEmpty()) {
            Glide.with(context.getApplicationContext()).load(UrlFactory.getShortImageByWidthUrl(width, url)).skipMemoryCache(false)
                    .error(BMHConstants.PLACE_HOLDER).into(holder.propImage);
        }

    }

    @Override
    public int getItemCount() {
        if (arrProperty != null)
            return arrProperty.size();
        else return 0;
    }

    public void toggleFav(String propertyId) {
        if (propertyId == null || propertyId.length() == 0) {
            return;
        }
        for (int i = 0; i < arrProperty.size(); i++) {
            PropertyCaraouselVO property = arrProperty.get(i);
            if (property.getId().equals(propertyId)) {
                boolean fav = arrProperty.get(i).isUser_favourite() ? false : true;
                arrProperty.get(i).setUser_favourite(fav);
                notifyDataSetChanged();
            }
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

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
        TextView construction;
        TextView tv_psf;
        TextView possession;
        TextView tv_priceRange;
        ListView projectUnitsList;
        TextView tv_returns;
        ImageView imageViewFav;
        TextView tv_rating;
        LinearLayout bhk_unit;
        LinearLayout ll_bhk_type;

        public ViewHolder(View convertView) {
            super(convertView);
            tv_name = convertView.findViewById(R.id.tv_proj_list_name);
            tv_price = convertView.findViewById(R.id.tv_proj_list_price_onwards);
            tv_address = convertView.findViewById(R.id.tv_proj_address);
            tv_Bhks = convertView.findViewById(R.id.tv_proj_Bhks);
            tv_proj_builderName = convertView.findViewById(R.id.tv_proj_builderName);
            propImage = convertView.findViewById(R.id.imageViewProj);
            infra = convertView.findViewById(R.id.tv_infra);
            needs = convertView.findViewById(R.id.tv_needs);
            lifestyle = convertView.findViewById(R.id.tv_lifestyle);
            construction = convertView.findViewById(R.id.tv_status);
            tv_psf = convertView.findViewById(R.id.tv_psf);
            tv_rating = convertView.findViewById(R.id.tv_rating);
            tv_returns = convertView.findViewById(R.id.text_return);
            bhk_unit = convertView.findViewById(R.id.bhk_unit);
            possession = convertView.findViewById(R.id.tvPossessionDate);
            tv_priceRange = convertView.findViewById(R.id.textViewMinPrice);
            projectUnitsList = convertView.findViewById(R.id.listView_bhk_type);
            ll_bhk_type = convertView.findViewById(R.id.ll_bhk_type);
            imageViewFav = convertView.findViewById(R.id.imageViewFav);
            imageViewFav.setOnClickListener(this);
            convertView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition(); // gets item position
            if (position != RecyclerView.NO_POSITION) {
                switch (view.getId()) {
                    case R.id.imageViewFav:
                        propertyModel = arrProperty.get(position);
                        if (app.getFromPrefs(BMHConstants.USERID_KEY) == null || app.getFromPrefs(BMHConstants.USERID_KEY).length() == 0) {
                            app.saveIntoPrefs(BMHConstants.VALUE, propertyModel.getId());
                            Intent i = new Intent(ctx, LoginActivity.class);
                            i.putExtra(LoginActivity.LOGIN_INTENT, false);// set false if login screen open from intent.
                            ctx.startActivityForResult(i, ProjectsListActivity.LOGIN_REQ_CODE);
                        } else {
                            if (ConnectivityReceiver.isConnected()) {
                                if (context instanceof ProjectsListActivity) {
                                    ((ProjectsListActivity) context).favRequest(propertyModel.getId());
                                }
                            } else {
                                Utils.showToast(ctx, ctx.getString(R.string.check_your_internet_connection));
                            }
                        }
                        break;
                    default:
                        propertyModel = arrProperty.get(position);
                        IntentDataObject mIntentDataObject = new IntentDataObject();
                        mIntentDataObject.putData(ParamsConstants.ID, propertyModel.getId());
                        mIntentDataObject.putData(ParamsConstants.TYPE, ParamsConstants.BUY);
                        Intent mIntent = new Intent(context, ProjectDetailActivity.class);
                        mIntent.putExtra(IntentDataObject.OBJ, mIntentDataObject);
                        context.startActivity(mIntent);
                        break;
                }
            }
        }
    }

    public void performFiltering(CharSequence constraint) {
        if (arrProperty == null) {
            return;
        }
        arrProperty.clear();
        if (filterList != null) {
            if (constraint.length() == 0) {
                arrProperty.addAll(filterList);
            } else {
                final String filterPattern = constraint.toString().toLowerCase().trim();
                for (final PropertyCaraouselVO data : filterList) {
                    String rating = "";
                    if (data.getRatings_average() != null && !data.getRatings_average().isEmpty())
                        rating = data.getRatings_average() + "/5";
                    else rating = "0/5";

                    String priceTxt = data.getPsf() + " " + data.getPsf_unit();
                    float price = Utils.toFloat(data.getPsf());
                    if (price > 0) priceTxt = Utils.priceFormat(price) + " " + data.getPsf_unit();

                    ArrayList<BhkUnitSpecification> units = (ArrayList<BhkUnitSpecification>) JsonParser.convertJsonToBean(APIType.BHK_UNIT_SPECIFICATION, data.getUnitSpecification().toString());

                    if (data.getDisplay_name() != null && data.getDisplay_name().toLowerCase().contains(filterPattern)
                            || data.getDisplay_name() != null && data.getDisplay_name().toLowerCase().contains(filterPattern)
                            || data.getExactlocation() != null && data.getExactlocation().toLowerCase().contains(filterPattern)
                            //|| data.getBuilder_name() != null && data.getBuilder_name().toLowerCase().contains(filterPattern)
                            || data.getUnder_construction() != null && data.getUnder_construction().toLowerCase().contains(filterPattern)
                            || data.getPossession_date() != null && data.getPossession_date().toLowerCase().contains(filterPattern)
                            || rating.toLowerCase().contains(filterPattern)
                            //|| data.getPrice_trends() != null && data.getPrice_trends().toLowerCase().contains(filterPattern)
                            || data.getBrokerage_term() != null && data.getBrokerage_term().toLowerCase().contains(filterPattern)

                            //|| data.getInfra() != null && data.getInfra().toLowerCase().contains(filterPattern)
                            //|| data.getNeeds() != null && data.getNeeds().toLowerCase().contains(filterPattern)
                            //|| data.getLife_style() != null && data.getLife_style().toLowerCase().contains(filterPattern)
                            || data.getProject_price_range() != null && data.getProject_price_range().toLowerCase().contains(filterPattern)
                            || priceTxt.toLowerCase().contains(filterPattern)

                            || isUnitSpecsExist(filterPattern, units)
                            ) {
                        arrProperty.add(data);
                    }
                }
                if (arrProperty.size() <= 0) {
                    ((ProjectsListActivity) context).showEmptyView(true);
                }
            }
            notifyDataSetChanged();
        }
    }

    private boolean isUnitSpecsExist(String searchTxt, ArrayList<BhkUnitSpecification> units) {
        if (units == null) return false;
        for (BhkUnitSpecification item : units) {
            if (item.getWpcf_flat_typology() != null && item.getWpcf_flat_typology().toLowerCase().contains(searchTxt)
                    || item.getArea_range() != null && item.getArea_range_unit() != null && (item.getArea_range() + " " + item.getArea_range_unit()).toLowerCase().contains(searchTxt)
                    || item.getPrice_range() != null && item.getPrice_range().toLowerCase().contains(searchTxt)) {
                return true;
            }
        }
        return false;
    }
}

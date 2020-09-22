package com.adapters;

import android.app.Activity;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.AppEnums.LocalityType;
import com.sp.BMHApplication;
import com.sp.R;
import com.helper.BMHConstants;
import com.utils.LocalityData;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Naresh on 07-Jan-17.
 */

public class LocalityListAdapter extends BaseAdapter implements Filterable {

    public ArrayList<LocalityData> locationLocalityData;
    private ArrayList<LocalityData> mFilterList;
    private Activity context = null;
    private LayoutInflater mInflater;
    private View.OnClickListener listItemClick = null;
    protected BMHApplication app;
    public static final String RECENT_SEARCH = "Recent Search";
    public static final String LOCALITY = "Locality";
    public static final String SUB_LOCALITY = "Sub Locality";
    public static final String BUILDER = "Builder";
    public static final String PROJECT = "Project";
    private Typeface font = null;

    public LocalityListAdapter(Activity context, ArrayList<LocalityData> locationLocalityData, View.OnClickListener listItemClick) {
        this.context = context;
        mInflater = LayoutInflater.from(context);
        this.listItemClick = listItemClick;
        setData(locationLocalityData);
        app =(BMHApplication) context.getApplicationContext();
        font = Typeface.createFromAsset(context.getAssets(), "fonts/regular.ttf");
    }


    public void setData(ArrayList<LocalityData> locationLocalityData){
        this.locationLocalityData = locationLocalityData;
        this.mFilterList = locationLocalityData;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if(locationLocalityData != null)return locationLocalityData.size();
        else return 0;
    }

    @Override
    public Object getItem(int position) {
        if(locationLocalityData != null)return locationLocalityData.get(position);
        else return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.locality_search_item, null);
            holder = new ViewHolder();
            holder.tv_header = (TextView)convertView.findViewById(R.id.tv_header);
            holder.ll_header = (LinearLayout) convertView.findViewById(R.id.ll_header);
            holder.tv_title = (TextView)convertView.findViewById(R.id.tv_title);
            holder.tv_subtitle = (TextView)convertView.findViewById(R.id.tv_subtitle);
            holder.view_line = (View)convertView.findViewById(R.id.view_line);
            holder.rl_row = (RelativeLayout)convertView.findViewById(R.id.rl_row);
            holder.tv_title.setTypeface(font);
            holder.tv_subtitle.setTypeface(font);
            holder.tv_header.setTypeface(font);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_title.setText(locationLocalityData.get(position).getTitle());
        String subtitle = locationLocalityData.get(position).getSubtitle();
        if(subtitle.equalsIgnoreCase(LocalityType.LOCATION.value)){
            holder.tv_subtitle.setText(app.getFromPrefs(BMHConstants.CITYNAME));
        }else if(subtitle.equalsIgnoreCase(LocalityType.SUBLOCATION.value)){
            holder.tv_subtitle.setText(locationLocalityData.get(position).getLocation_name());
        }else if(subtitle.equalsIgnoreCase(LocalityType.PROJECT.value)){
            holder.tv_subtitle.setText(locationLocalityData.get(position).getBuildername());
        }else if(subtitle.equalsIgnoreCase(LocalityType.BUILDER.value)){
            holder.tv_subtitle.setText(locationLocalityData.get(position).getSubtitle());
        }
        holder.rl_row.setTag(R.integer.locality_item, locationLocalityData.get(position));
        holder.rl_row.setOnClickListener(listItemClick);

        headerState(locationLocalityData.get(position),holder);

        return convertView;
    }

    private void headerState(LocalityData data,ViewHolder holder) {
        if(data.getHeader() != null && data.getHeader().length() > 0){
            holder.tv_header.setText(data.getHeader());
            holder.ll_header.setVisibility(View.VISIBLE);
            holder.view_line.setVisibility(View.INVISIBLE);
        }else{
            holder.ll_header.setVisibility(View.GONE);
            holder.view_line.setVisibility(View.VISIBLE);
        }
    }

    private ArrayList<LocalityData> getFilterResult(CharSequence charText){
        ArrayList<LocalityData> mList = new ArrayList<>();
        if (charText.length() < 2) {
            for(LocalityData result : mFilterList){
                if(result.getLocalityType().equalsIgnoreCase(LocalityType.LOCATION.value) || result.getLocalityType().equalsIgnoreCase(LocalityType.RECENT.value))
                    mList.add(result);
            }
        } else {
            for (LocalityData result : mFilterList) {
                if ((result.getTitle() != null && result.getTitle().toLowerCase(Locale.getDefault()).contains(charText))) {
                    mList.add(result);
                }
            }
        }
        return mList;
    }
    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint != null) {
                    ArrayList<LocalityData> tempresultList	 = getFilterResult(constraint.toString());
                    tempresultList = reFilterData(tempresultList);
                    filterResults.values = tempresultList;
                    filterResults.count = tempresultList.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    locationLocalityData = (ArrayList<LocalityData>) results.values;
                    notifyDataSetChanged();
                }
                else {
                    locationLocalityData.clear();
                    notifyDataSetChanged();
                }
            }
        };
        return filter;
    }

    class ViewHolder {
        private RelativeLayout rl_row;
        private TextView tv_title;
        private TextView tv_subtitle;
        private View view_line;
        private TextView tv_header;
        private LinearLayout ll_header;


    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }


    private ArrayList<LocalityData> reFilterData(ArrayList<LocalityData> dataList){
        ArrayList<LocalityData> allData = new ArrayList<>();
        ArrayList<LocalityData> recent = new ArrayList<>();
        ArrayList<LocalityData> locality = new ArrayList<>();
        ArrayList<LocalityData> subLocality = new ArrayList<>();
        ArrayList<LocalityData> project = new ArrayList<>();
        ArrayList<LocalityData> builder = new ArrayList<>();
        for(LocalityData data : dataList){
            if(data.getLocalityType().equalsIgnoreCase(LocalityType.SUBLOCATION.value)){
                data.setHeader("");
                subLocality.add(data);
            }else if(data.getLocalityType().equalsIgnoreCase(LocalityType.LOCATION.value)){
                data.setHeader("");
                locality.add(data);
            }else if(data.getLocalityType().equalsIgnoreCase(LocalityType.BUILDER.value)){
                data.setHeader("");
                builder.add(data);
            }else if(data.getLocalityType().equalsIgnoreCase(LocalityType.PROJECT.value)){
                data.setHeader("");
                project.add(data);
            }else if(data.getLocalityType().equalsIgnoreCase(LocalityType.RECENT.value)){
                data.setHeader("");
                recent.add(data);
            }

        }// end of loop
        allData.clear();
        if(recent.size() > 0) {
            recent.get(0).setHeader(RECENT_SEARCH);
            allData.addAll(recent);
        }
        if(locality.size() > 0){
            locality.get(0).setHeader(LOCALITY);
            allData.addAll(locality);
        }
        if(subLocality.size() > 0){
            subLocality.get(0).setHeader(SUB_LOCALITY);
            allData.addAll(subLocality);
        }
        if(builder.size() > 0){
            builder.get(0).setHeader(BUILDER);
            allData.addAll(builder);
        }
        if(project.size() > 0) {
            project.get(0).setHeader(PROJECT);
            allData.addAll(project);
        }
        return allData;
    }
}

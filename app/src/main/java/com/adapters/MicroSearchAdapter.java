package com.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.sp.R;
import com.model.MicroMarketData;

import java.util.ArrayList;


public class MicroSearchAdapter extends BaseAdapter {
    private Resources mResources = null;
    private  final String TAG = MicroSearchAdapter.class.getName();
    private Context mContext = null;
    private Typeface gothambook,icomoonfree = null;
    public ArrayList<MicroMarketData> dataList;
    public ArrayList<MicroMarketData> filterList = new ArrayList<>();

    public MicroSearchAdapter(Context mContext, ArrayList<MicroMarketData> dataList) {
        mResources = mContext.getResources();
        this.mContext = mContext;
        this.dataList = dataList;
        if(dataList != null) filterList.addAll(dataList);
    }

    @Override
    public int getCount() {
        if(dataList == null)
            return 0;
        else
            return dataList.size();
    }

    @Override
    public String getItem(int index) {
        return "";
    }

    protected void showToast(String msg){
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
    }

    /*@Override
    public Filter getFilter() {
        return mFilter;
    }
*/
    public void performFiltering(CharSequence constraint) {
        dataList.clear();
        if(filterList != null){
            if (constraint.length() == 0) {
                dataList.addAll(filterList);
            } else {
                final String filterPattern = constraint.toString().toLowerCase().trim();
                for (final MicroMarketData data : filterList) {
                    if (data.getMicromarket_name() != null && data.getMicromarket_name().toLowerCase().contains(filterPattern)) {
                        dataList.add(data);
                    }
                }
            }
            notifyDataSetChanged();
        }

    }

   /* public class CustomFilter extends Filter {
       // private ProjectSearchAdapter mAdapter;
        private CustomFilter(ProjectSearchAdapter mAdapter) {
            super();
           // this.mAdapter = mAdapter;
        }
        @Override
        protected Filter.FilterResults performFiltering(CharSequence constraint) {
            dataList.clear();
            final FilterResults results = new FilterResults();
            if(filterList != null){
                if (constraint.length() == 0) {
                    dataList.addAll(filterList);
                } else {
                    final String filterPattern = constraint.toString().toLowerCase().trim();
                    for (final MicroMarketData data : filterList) {
                        if (data.getProject_name() != null && data.getProject_name().toLowerCase().contains(filterPattern)) {
                            dataList.add(data);
                        }
                    }
                }
            }

            System.out.println("Count Number " + dataList.size());
            results.values = dataList;
            results.count = dataList.size();
            return results;
        }
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            dataList = (ArrayList<MicroMarketData>) results.values;
            notifyDataSetChanged();
        }
    }
*/
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if(convertView == null){
            holder = new ViewHolder();
            LayoutInflater mInflater = LayoutInflater.from(mContext);
            convertView = mInflater.inflate(R.layout.list_row, null);
            holder.tv_project = (TextView)convertView.findViewById(R.id.tv_project);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        if(dataList != null && dataList.size() > position) {
            holder.tv_project.setText(dataList.get(position).getMicromarket_name());
            convertView.setTag(R.integer.project_data, dataList.get(position));
        }
        return convertView;
    }


    private class ViewHolder{
        TextView tv_project;
    }
}
package com.filter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.TextView;

import com.sp.R;

import java.util.ArrayList;

/** Custom adapter for displaying an array of Planet objects. */
public class BuilderArrayAdapter extends ArrayAdapter<Builder> {

	private LayoutInflater inflater;
	public ArrayList<Builder> builderList = null;
	public ArrayList<Builder> tempList = null;
	private ItemFilter mFilter = new ItemFilter();
	
	Typeface fond;

	public BuilderArrayAdapter(Context context, ArrayList<Builder> builderList) {
		super(context, R.layout.search_listview, R.id.rowTextView, builderList);
		this.builderList = builderList;
		tempList = builderList;
		// Cache the LayoutInflate to avoid asking for a new one each time.
		inflater = LayoutInflater.from(context);
		
		fond = Typeface.createFromAsset(context.getAssets(), "fonts/regular.ttf");
	}

@Override
	public int getCount() {
		return tempList.size();
	}

	@Override
	public Builder getItem(int position) {
		return super.getItem(position);
	}

	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
	//	Builder builder = (Builder) this.getItem(position);
		CheckBox checkBox;
		TextView textView;

		// Create a new row view
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.search_listview, null);

			textView = (TextView) convertView.findViewById(R.id.rowTextView);
			checkBox = (CheckBox) convertView.findViewById(R.id.CheckBox01);
			
			textView.setTypeface(fond);

			convertView.setTag(new BuilderViewHolder(textView, checkBox));
			checkBox.setClickable(false);
			// If CheckBox is toggled, update the planet it is tagged with.
			/*checkBox.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					CheckBox cb = (CheckBox) v;
					Builder builder = (Builder) cb.getTag();
					builder.setChecked(cb.isChecked());
					cb.setActivated(builder.isChecked());

				}
			});*/
		}
		// Re-use existing row view
		else {

			BuilderViewHolder viewHolder = (BuilderViewHolder) convertView.getTag();
			checkBox = viewHolder.getCheckBox();
			textView = viewHolder.getTextView();
		}

		checkBox.setTag(tempList.get(position));

		// Display planet data
		checkBox.setChecked(tempList.get(position).isChecked());
		checkBox.setActivated(tempList.get(position).isChecked());
		textView.setText(tempList.get(position).getName());
		for(int i = 0; i < builderList.size(); i++){
			if(tempList.get(position).getId().equals(builderList.get(i).getId())){
				builderList.get(i).setChecked(tempList.get(position).isChecked());
			}
		}
		return convertView;
	}


	public Filter getFilter() {
		return mFilter;
	}

	private class ItemFilter extends Filter {
		@Override
		protected Filter.FilterResults performFiltering(CharSequence constraint) {
			String filterString = constraint.toString().toLowerCase();
			FilterResults results = new FilterResults();
			final ArrayList<Builder> list = builderList;
			int count = list.size();
			final ArrayList<Builder> nlist = new ArrayList<Builder>(count);
			Builder filterableString ;
			for (int i = 0; i < count; i++) {
				filterableString = list.get(i);
				if (filterableString.getName().toLowerCase().contains(filterString)) {
					nlist.add(filterableString);
				}
			}
			results.values = nlist;
			results.count = nlist.size();
			return results;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void publishResults(CharSequence constraint, FilterResults results) {
			tempList = (ArrayList<Builder>) results.values;
			notifyDataSetChanged();
		}

	}
}

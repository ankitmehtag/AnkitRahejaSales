package com.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.sp.R;

import java.util.ArrayList;

// Creating an Adapter Class
public class CustomSpinnerAdapter extends ArrayAdapter<String> {

	private Context ctx;
	ArrayList<String> data;

	public CustomSpinnerAdapter(Context context, int textViewResourceId,ArrayList<String> objects) {
		super(context, textViewResourceId, objects);
		ctx = context;
		data = objects;
		////System.out.println("hh getting zodiac size "+data.size());

	}

	public View getCustomView(int position, View convertView,ViewGroup parent) {

		LayoutInflater inflater = LayoutInflater.from(ctx);
		View layout = inflater.inflate(R.layout.textview_spinner, parent, false);
		TextView tvLanguage = layout.findViewById(R.id.textspinner);
		tvLanguage.setText(data.get(position));
//			}
		return layout;
	}

	// It gets a View that displays in the drop down popup the data at the
	// specified position
	@Override
	public View getDropDownView(int position, View convertView,
								ViewGroup parent) {

		return getCustomView(position, convertView, parent);
	}

	// It gets a View that displays the data at the specified position
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {


		View view = super.getView(position, convertView, parent);
		TextView text = view.findViewById(R.id.textspinner);
		text.setTextColor(Color.BLACK);


		return view;
	}

}
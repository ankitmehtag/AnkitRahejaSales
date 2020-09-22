package com.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.sp.R;
import com.VO.CommentsVO;

import java.util.ArrayList;

public class CommentsListAdapter extends BaseAdapter {
	private Context context;
	private LayoutInflater mInflater;
	ArrayList<CommentsVO> arrComments;

	public CommentsListAdapter(Activity c, ArrayList<CommentsVO> arr) {
		context = c;
		mInflater = LayoutInflater.from(context);
		arrComments = arr;
	}

	@Override
	public int getCount() {
		return arrComments.size();
	}

	@Override
	public Object getItem(int position) {
		return arrComments.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.comment_row, null);
			holder = new ViewHolder();
			holder.tv_title = (TextView) convertView.findViewById(R.id.tv_comment_title);
			holder.tv_description = (TextView) convertView.findViewById(R.id.tv_comment_description);
			holder.tv_userName = (TextView) convertView.findViewById(R.id.tvcomment_username);
			holder.tv_date = (TextView) convertView.findViewById(R.id.tvcommentDate);
			holder.ratingBar = (RatingBar) convertView.findViewById(R.id.ratingBarComment);
			convertView.setBackgroundColor(context.getResources().getColor(R.color.proj_detail_bg));
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		CommentsVO item = arrComments.get(position);
		if(item.getComment_title() != null && !item.getComment_title().isEmpty())
		holder.tv_title.setText(item.getComment_title());
		if(item.getComment_description() != null && !item.getComment_description().isEmpty())
		holder.tv_description.setText(item.getComment_description());
		if(item.getComment_user_name() != null && !item.getComment_user_name().isEmpty() && !item.getComment_user_name().equalsIgnoreCase("null"))
		holder.tv_userName.setText(item.getComment_user_name());
		if(item.getComment_date() != null && !item.getComment_date().isEmpty())
		holder.tv_date.setText(item.getComment_date());
		
		float rat = 0;
		try {
			rat = Float.parseFloat(item.getRating());
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		holder.ratingBar.setRating(rat);

		return convertView;

	}
	class ViewHolder {
		TextView tv_title;
		TextView tv_description;
		TextView tv_userName;
		TextView tv_date;
		RatingBar ratingBar;
	}

}

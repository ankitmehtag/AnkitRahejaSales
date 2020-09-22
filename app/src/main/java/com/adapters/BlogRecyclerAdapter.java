package com.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.activities.BlogDetailsActivity;
import com.bumptech.glide.Glide;
import com.helper.BMHConstants;
import com.helper.UrlFactory;
import com.model.BlogData;
import com.model.FavBlogModel;
import com.sp.R;
import com.utils.Utils;
import com.utils.WordUtils;

import java.util.ArrayList;

/**
 * Created by Mohit on 7/4/2018.
 */

public class BlogRecyclerAdapter extends RecyclerView.Adapter<BlogRecyclerAdapter.ViewHolder> {
    private Context context;
    private ArrayList<BlogData> blogDataArrayList;
    private ArrayList<FavBlogModel.Data> favBlogDataArrayList;
    private BlogData data;
    private String category_id;
    private FavBlogModel.Data favBlogData;

    public BlogRecyclerAdapter(Context c, ArrayList<BlogData> transactionsList,
                               String category_id, ArrayList<FavBlogModel.Data> favBlogDataArrayList) {
        context = c;
        this.category_id = category_id;
        this.blogDataArrayList = transactionsList;
        this.favBlogDataArrayList = favBlogDataArrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_blog_layout, parent, false);
        return new BlogRecyclerAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(BlogRecyclerAdapter.ViewHolder holder, int position) {
        if (blogDataArrayList != null && blogDataArrayList.size() > 0) {
            data = blogDataArrayList.get(position);
            if (data.isRead()) {
                holder.tv_blog_title.setText(WordUtils.capitalize(data.getTitle().toLowerCase()));
                holder.tv_blog_title.setTextColor(context.getResources().getColor(R.color.grey));
            } else {
                holder.tv_blog_title.setText(WordUtils.capitalize(data.getTitle().toLowerCase()));
                holder.tv_blog_title.setTextColor(Color.BLACK);
            }
            holder.tv_blog_description.setText(data.getContent());
            holder.tv_blog_date.setText(data.getDate());
            holder.iv_blog_image.setImageResource(R.drawable.no_image);
            String url = data.getImage();//TODO: assign url
            if (url != null && !url.equalsIgnoreCase("")) {
                Glide.with(context.getApplicationContext()).load(UrlFactory.getShortImageByWidthUrl((int) Utils.dp2px(120, context), url))
                        .placeholder(BMHConstants.PLACE_HOLDER).error(BMHConstants.NO_IMAGE).into(holder.iv_blog_image);
            }
        } else if (favBlogDataArrayList != null && favBlogDataArrayList != null) {
            favBlogData = favBlogDataArrayList.get(position);
            holder.tv_blog_title.setText(WordUtils.capitalize(favBlogData.getTitle().toLowerCase()));
            holder.tv_blog_description.setText(favBlogData.getContent());
            holder.tv_blog_date.setText(favBlogData.getDate());
            holder.iv_blog_image.setImageResource(R.drawable.no_image);
            String url = favBlogData.getImage();//TODO: assign url
            if (url != null && !url.equalsIgnoreCase("")) {
                Glide.with(context.getApplicationContext()).load(UrlFactory.getShortImageByWidthUrl((int) Utils.dp2px(120, context), url))
                        .placeholder(BMHConstants.PLACE_HOLDER).error(BMHConstants.NO_IMAGE).into(holder.iv_blog_image);
            }
        }
    }

    @Override
    public int getItemCount() {
        if (blogDataArrayList != null)
            return blogDataArrayList.size();
        else
            return favBlogDataArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView iv_blog_image;
        TextView tv_blog_title;
        TextView tv_blog_description;
        TextView tv_blog_date;
        LinearLayout blogRow;

        public ViewHolder(View convertView) {
            super(convertView);
            iv_blog_image = convertView.findViewById(R.id.iv_blog_image);
            tv_blog_title = convertView.findViewById(R.id.tv_blog_title);
            tv_blog_description = convertView.findViewById(R.id.tv_blog_description);
            tv_blog_date = convertView.findViewById(R.id.tv_date_time);
            blogRow = convertView.findViewById(R.id.blog_row);
            blogRow.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.blog_row) {
                try {
                    int position = getAdapterPosition(); // gets item position
                    if (position != RecyclerView.NO_POSITION) { // Check if an item was deleted, but the user clicked it before the UI removed it
                        data = blogDataArrayList.get(position);
                        Intent blogDetails = new Intent(context, BlogDetailsActivity.class);
                        blogDetails.putExtra("blog_id", data.getBlog_id());
                        blogDetails.putExtra("category_id", category_id);
                        context.startActivity(blogDetails);
                    }
                } catch (Exception e) {
                    System.out.println(e.toString());
                }
            }
        }
    }

    public void setFilter(ArrayList<BlogData> filteredList) {
        this.blogDataArrayList = filteredList;
        notifyDataSetChanged();
    }
}

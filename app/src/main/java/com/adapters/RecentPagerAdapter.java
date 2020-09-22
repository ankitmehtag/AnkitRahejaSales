package com.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Parcelable;
import androidx.viewpager.widget.PagerAdapter;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.activities.BlogDetailsActivity;
import com.bumptech.glide.Glide;
import com.helper.UrlFactory;
import com.model.RecentBlogModel;
import com.sp.R;
import com.utils.Utils;

import java.util.ArrayList;

/**
 * Created by Mohit on 7/7/2018.
 */

public class RecentPagerAdapter extends PagerAdapter {
    Context context;
    private ArrayList<RecentBlogModel.Data.Recent> recent;

    public RecentPagerAdapter(Context context, ArrayList<RecentBlogModel.Data.Recent> recent) {
        this.context = context;
        this.recent = recent;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return recent.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, final int position) {
        LayoutInflater inflater = LayoutInflater.from(context);
        final View imageLayout = inflater.inflate(R.layout.recycler_blog_layout, view, false);
        assert imageLayout != null;
        final ImageView imageView = (ImageView) imageLayout
                .findViewById(R.id.iv_blog_image);
        final TextView title = (TextView) imageLayout.findViewById(R.id.tv_blog_title);
        final TextView description = (TextView) imageLayout.findViewById(R.id.tv_blog_description);
        final TextView date = (TextView) imageLayout.findViewById(R.id.tv_date_time);
        String blog_url = recent.get(position).getImage();
        Glide.with(context).load(UrlFactory.getShortImageByWidthUrl((int) Utils.dp2px(120, context),
                blog_url)).into(imageView);
        title.setText(recent.get(position).getTitle());
        if (recent.get(position).isRead()) {
            title.setText(recent.get(position).getTitle());
            title.setTextColor(Color.GRAY);
        } else {
            title.setText(recent.get(position).getTitle());
            title.setTextColor(Color.BLACK);
        }
        date.setVisibility(View.GONE);
        description.setText(Html.fromHtml(recent.get(position).getContent()));
        view.addView(imageLayout, 0);
        imageLayout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent pagerIntent = new Intent(context, BlogDetailsActivity.class);
                pagerIntent.putExtra("blog_id", recent.get(position).getBlog_id());
                context.startActivity(pagerIntent);
            }
        });
        return imageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }
}
